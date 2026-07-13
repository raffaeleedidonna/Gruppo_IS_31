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

  public boolean puoAggiungere(Prodotto prodotto, long quantita) {
    long totale = quantitaInCarrello(prodotto) + quantita;
    return prodotto.isVendibilePer(totale);
  }

  private long quantitaInCarrello(Prodotto prodotto) {
    RigaCarrello riga = trovaRiga(prodotto);
    return riga == null ? 0 : riga.getQuantita();
  }

  private RigaCarrello trovaRiga(Prodotto prodotto) {
    for (RigaCarrello riga : righe) if (riga.haProdotto(prodotto))
  return riga;
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
