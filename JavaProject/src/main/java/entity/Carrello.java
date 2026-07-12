package entity;

import java.util.ArrayList;

public class Carrello {
	private long id;
	private Cliente cliente;
	private ArrayList<RigaCarrello> righe = new ArrayList<RigaCarrello>();

	protected Carrello() {}

	public Carrello(Cliente cliente) {
    this.cliente = cliente;
    this.righe = new ArrayList<RigaCarrello>();
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
