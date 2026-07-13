package entity;

import jakarta.persistence.*;

@Entity
public class RigaCarrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long quantita;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prodotto_id", nullable = false)
	private Prodotto prodotto;

	protected RigaCarrello() {};

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

	public boolean verificaDisponibilitaProdotto() {
		throw new UnsupportedOperationException();
	}

	public void incrementaQuantita(long quantita){
		this.quantita += quantita;
	}

	public long getQuantita(){return quantita;}
}
