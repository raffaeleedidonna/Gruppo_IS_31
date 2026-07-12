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

	public boolean scarica(long quantita) {
		throw new UnsupportedOperationException();
	}
}