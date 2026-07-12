package entity;

public class RigaOrdine {
	private long id;
	private long quantitaAcquistata;
	private double prezzoDiAcquisto;
	public Prodotto prodotto;

	protected RigaOrdine() {
		throw new UnsupportedOperationException();
	}

	public RigaOrdine(Prodotto prodotto, long quantitaAcquistata, double prezzoDiAcquisto) {
		throw new UnsupportedOperationException();
	}

	public double calcolaSubtotale() {
		throw new UnsupportedOperationException();
	}
}