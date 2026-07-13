package entity;

public class RigaCarrello {
	private long id;
	private long quantita;
	private Prodotto prodotto;

	protected RigaCarrello() {}

	public RigaCarrello(Prodotto prodotto, long quantita) {
		this.prodotto = prodotto;
    this.quantita = quantita;
	}

	public double calcolaSubtotale() {
		throw new UnsupportedOperationException();
	}

	public boolean isVendibile() { return prodotto.isVendibilePer(quantita); }

  public RigaOrdine creaRigaOrdine() { return new RigaOrdine(prodotto, quantita); }
}
