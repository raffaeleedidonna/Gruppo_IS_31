package entity;

import java.util.ArrayList;
import entity.RigaCarrello;

public class Carrello {
	private long id;
	public Cliente cliente;
	public ArrayList<RigaCarrello> righe = new ArrayList<RigaCarrello>();

	protected Carrello() {
		throw new UnsupportedOperationException();
	}

	public Carrello(Cliente cliente) {
		throw new UnsupportedOperationException();
	}

	public boolean aggiungiProdotto(Prodotto prodotto, long quantita) {
		throw new UnsupportedOperationException();
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