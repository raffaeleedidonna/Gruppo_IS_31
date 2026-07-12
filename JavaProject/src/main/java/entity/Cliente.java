package entity;

public class Cliente extends Utente {
	public Carrello carrello;

	protected Cliente() {
		throw new UnsupportedOperationException();
	}

	public Cliente(String email, String passwordHash, String nome, String cognome, String indirizzo, String immagineProfilo) {
		throw new UnsupportedOperationException();
	}

	public String ruolo() {
		throw new UnsupportedOperationException();
	}
}