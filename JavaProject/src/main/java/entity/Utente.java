package entity;

public abstract class Utente {
	private long id;
	private String email;
	private String passwordHash;
	private String indirizzo;
	private String immagineProfilo;
	private String nome;
	private String cognome;

	protected Utente(String email, String passwordHash, String nome, String cognome, String indirizzo, String immagineProfilo) {
		throw new UnsupportedOperationException();
	}

	public abstract String ruolo();
}