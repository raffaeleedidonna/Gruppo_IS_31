package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", unique = true, nullable = false)
	private Cliente cliente;

	@OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RigaCarrello> righe = new ArrayList<RigaCarrello>();

	protected Carrello() {}

	public Carrello(Cliente cliente) {
    this.cliente = cliente;
    this.righe = new ArrayList<RigaCarrello>();
	}

	public long getQuantitaProdotto(long idProdotto){

		for (RigaCarrello riga : righe) {

			if (riga.getProdotto().getId() == idProdotto){

				return riga.getQuantita();
			}
		}

		return 0;
	}

	public boolean aggiungiProdotto(Prodotto prodotto, long quantita) {

		//Verifico se esiste già una riga per il prodotto richiesto, se si aumento la quantità nel carrello

		for (RigaCarrello riga : righe){
			if(riga.getProdotto().getId() == prodotto.getId()){
				riga.incrementaQuantita(quantita);
				return true;
			}
		}

		//Se non è stato trovato, creo una riga per quel prodotto e la aggiungo alla lista di righe
		RigaCarrello riga = new RigaCarrello(prodotto, quantita);

		boolean esito = righe.add(riga);

		return esito;
	}

	public double calcolaTotale() {
		throw new UnsupportedOperationException();
	}

	public boolean svuotaCarrello() {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public boolean verificaDisponibilitaRighe() {
		throw new UnsupportedOperationException();
	}

	public boolean riversaIn(Ordine ordine) {
		throw new UnsupportedOperationException();
	}
}
