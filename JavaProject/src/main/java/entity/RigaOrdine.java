package entity;

public class RigaOrdine {
	private long id;
	private long quantitaAcquistata;
	private double prezzoDiAcquisto;
	private Prodotto prodotto;

	protected RigaOrdine() {}

	public RigaOrdine(Prodotto prodotto, long quantitaAcquistata) {
		this.prodotto = prodotto;
		this.quantitaAcquistata = quantitaAcquistata;
		this.prezzoDiAcquisto = prodotto.getPrezzo();
	}

	public Prodotto getProdotto() { return prodotto; }

	public double calcolaSubtotale() { return prezzoDiAcquisto * quantitaAcquistata;}

	public void scarica() { prodotto.scarica(quantitaAcquistata); }
}
