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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	public boolean scarica(long quantita) {
		throw new UnsupportedOperationException();
	}

	//Getter
	public long getId(){return id;}
	public long getQuantitaMagazzino(){ return quantitaMagazzino; }
	public boolean isDisponibile(){ return disponibile; }

}
