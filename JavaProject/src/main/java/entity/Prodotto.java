package entity;

public class Prodotto {
	private long id;
	private String nome;
	private String descrizione;
	private double prezzo;
	private long quantitaMagazzino;
	private boolean disponibile;
	private boolean inOfferta;
	private Categoria categoria;

	public boolean scarica(long quantita) {
		throw new UnsupportedOperationException();
	}

	//Getter
	public long getId(){return id;}
	public long getQuantitaMagazzino(){ return quantitaMagazzino; }

}
