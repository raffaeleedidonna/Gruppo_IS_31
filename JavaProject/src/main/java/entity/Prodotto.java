package entity;

import jakarta.persistence.*;

@Entity
public class Prodotto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nome;
	private String descrizione;
	private double prezzo;
	private long quantitaMagazzino;
	private boolean disponibile;
	private boolean inOfferta;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	protected Prodotto() {}

	public Prodotto(String nome, String descrizione, double prezzo, long quantitaMagazzino, boolean disponibile, boolean inOfferta, Categoria categoria) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.quantitaMagazzino = quantitaMagazzino;
		this.disponibile = disponibile;
		this.inOfferta = inOfferta;
		this.categoria = categoria;
	}

	public void scarica(long quantita) { quantitaMagazzino -= quantita; }

	public boolean isVendibilePer(long quantita) {
		return disponibile && quantita > 0 && quantita <= quantitaMagazzino;
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
