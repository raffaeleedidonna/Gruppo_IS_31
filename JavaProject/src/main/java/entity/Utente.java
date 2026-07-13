package entity;

public abstract class Utente {
	private long id;
	private String email;
	private String passwordHash;
	private String indirizzo;
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

	public long getId() {return this.id;}

	public String getIndirizzo() { return this.indirizzo; }
}
