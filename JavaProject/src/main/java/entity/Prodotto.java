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

	public void scarica(long quantita) { quantitaMagazzino -= quantita; }

	public boolean isVendibilePer(long quantita) {
		return disponibile && quantita <= quantitaMagazzino;
	}

	//Getter
	public long getId() {return id;}

	public String getNome() {return nome;}

	public String getDescrizione() {return descrizione;}

	public double getPrezzo() {return prezzo;}

	public long getQuantitaMagazzino() {return quantitaMagazzino;}

	public boolean isDisponibile() {return disponibile;}

	public boolean isInOfferta() {return inOfferta;}

	public Categoria getCategoria() {return categoria;}

}
