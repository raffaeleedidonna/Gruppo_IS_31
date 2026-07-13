package entity;

public class Prodotto {
	private long id;
	private String nome;
	private String descrizione;
	private double prezzo;
	private long quantitaMagazzino;
	private boolean disponibile;
	private boolean inOfferta;
	public Categoria categoria;
  
  public double getPrezzo() {return prezzo;}
  
  public boolean haScortaPer(long quantita) { return quantita <= quantitaMagazzino; }

	public void scarica(long quantita) { quantitaMagazzino -= quantita; }
}
