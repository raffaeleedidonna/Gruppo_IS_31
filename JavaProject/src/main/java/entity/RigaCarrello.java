package entity;

public class RigaCarrello {
	private long id;
	private long quantita;
	public Prodotto prodotto;

	protected RigaCarrello() {
		throw new UnsupportedOperationException();
	}

	public RigaCarrello(Prodotto prodotto, long quantità) {
		throw new UnsupportedOperationException();
	}

	public double calcolaSubtotale() {
		throw new UnsupportedOperationException();
	}

	public boolean verificaDisponibilitaProdotto() {
		throw new UnsupportedOperationException();
	}
}