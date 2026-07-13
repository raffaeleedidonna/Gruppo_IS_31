package entity;

public class Cliente extends Utente {
	private Carrello carrello;

	protected Cliente() {
		super();
	}

	public Cliente(String email, String passwordHash, String nome, String cognome, String indirizzo, byte[] immagineProfilo) {
		super(email, passwordHash, nome, cognome, indirizzo, immagineProfilo);
		carrello = new Carrello(this);
	}

	public Ruolo ruolo() {
		return Ruolo.CLIENTE;
	}

}
