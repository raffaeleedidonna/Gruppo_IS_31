package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "cliente_id", unique = true)
	private Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "carrello_id")
	private List<RigaCarrello> righe = new ArrayList<RigaCarrello>();

	protected Carrello() {}

	public Carrello(Cliente cliente) {
		this.cliente = cliente;
		this.righe = new ArrayList<RigaCarrello>();
	}

	public boolean puoAggiungere(Prodotto prodotto, long quantita) {
		long totale = quantitaInCarrello(prodotto) + quantita;
		return prodotto.isVendibilePer(totale);
	}

	public boolean puoModificare(Prodotto prodotto, long nuovaQuantita){

		RigaCarrello riga = trovaRiga(prodotto);

		if (riga == null){
			return false;
		}
		return riga.puoImpostare(nuovaQuantita);
	}

	public void modificaQuantita(Prodotto prodotto, long nuovaQuantita){

		RigaCarrello riga = trovaRiga(prodotto);

		riga.impostaQuantita(nuovaQuantita);

		if(nuovaQuantita == 0){
			righe.remove(riga);
		}

	}

	private long quantitaInCarrello(Prodotto prodotto) {
		RigaCarrello riga = trovaRiga(prodotto);
		return riga == null ? 0 : riga.getQuantita();
	}

	private RigaCarrello trovaRiga(Prodotto prodotto) {
		for (RigaCarrello riga : righe) {
			if (riga.haProdotto(prodotto)) {return riga;}
		}
		return null;
	}

	public void aggiungiProdotto(Prodotto prodotto, long quantita) {
		RigaCarrello riga = trovaRiga(prodotto);
		if (riga != null) riga.incrementaQuantita(quantita);
		else righe.add(new RigaCarrello(prodotto, quantita));
	}

	public double calcolaTotale() {
		throw new UnsupportedOperationException();
	}

	public void svuota() {
		righe.clear();
	}

	public boolean isEmpty() {
		return righe.isEmpty();
	}

	public boolean haTutteRigheVendibili() {
		for (RigaCarrello riga : righe) {
			if (!riga.isVendibile()) {return false;}
		}
		return true;
	}

	public void riversaIn(Ordine ordine) {
		for (RigaCarrello riga : righe) {
			ordine.aggiungiRiga(riga.creaRigaOrdine());
		}
	}


	//Getter
	public Cliente getCliente() {
		return cliente;
	}

	public List<RigaCarrello> getRighe() {return righe;}
}
