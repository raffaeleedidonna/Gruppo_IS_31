package entity;

import java.util.ArrayList;
import java.util.List;

public class Carrello {

	private long id;

	private Cliente cliente;

	private List<RigaCarrello> righe = new ArrayList<RigaCarrello>();

	protected Carrello() {}

	public Carrello(Cliente cliente) {
		this.cliente = cliente;
		this.righe = new ArrayList<RigaCarrello>();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public boolean puoAggiungere(Prodotto prodotto, long quantita) {
		long totale = quantitaInCarrello(prodotto) + quantita;
		return prodotto.isVendibilePer(totale);
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
}
