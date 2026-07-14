package entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String email;

	private String passwordHash;
	private String indirizzo;

	@Lob
	private byte[] immagineProfilo;

	private String nome;
	private String cognome;

	protected Utente() {}

	protected Utente(String email, String passwordHash, String nome, String cognome, String indirizzo, byte[] immagineProfilo) {
		this.email=email;
		this.passwordHash=passwordHash;
		this.nome=nome;
		this.cognome=cognome;
		this.indirizzo=indirizzo;
		this.immagineProfilo=immagineProfilo;
	}

	public abstract Ruolo ruolo();

	public long getId() {return id;}

	public String getIndirizzo() {return indirizzo;}
}
