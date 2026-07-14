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

	public boolean haProdotto(Prodotto prodotto) {
		return this.prodotto.getId() == prodotto.getId();
	}

	public double calcolaSubtotale() {
		throw new UnsupportedOperationException();
	}

	public void incrementaQuantita(long quantita){
		this.quantita += quantita;
	}

	public boolean isVendibile() { return prodotto.isVendibilePer(quantita); }

	public RigaOrdine creaRigaOrdine() { return new RigaOrdine(prodotto, quantita); }

	//Getter
	public long getQuantita(){return quantita;}
	public Prodotto getProdotto(){return prodotto;}

}
