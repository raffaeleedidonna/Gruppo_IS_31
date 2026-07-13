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

  public Cliente getCliente() {
    return cliente;
  }

	public boolean aggiungiProdotto(Prodotto prodotto, long quantita) {
		throw new UnsupportedOperationException();
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

	public boolean haScorteSufficienti() {
		for(RigaCarrello riga : righe) {
      if(!riga.haScortaSufficiente()) {return false;} 
    }
    return true;
	}

	public void riversaIn(Ordine ordine) {
		for (RigaCarrello riga : righe) {
      ordine.aggiungiRiga(riga.creaRigaOrdine());
    }
	}
}
