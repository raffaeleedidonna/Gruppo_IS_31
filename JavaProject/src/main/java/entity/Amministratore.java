package entity;

import jakarta.persistence.*;

@Entity
public class Amministratore extends Utente {

	protected Amministratore() {
		super();
	}

	public Amministratore(String email, String passwordHash, String nome, String cognome, String indirizzo, byte[] immagineProfilo) {
		super(email, passwordHash, nome, cognome, indirizzo, immagineProfilo);
	}

	public Ruolo ruolo() {
		return Ruolo.AMMINISTRATORE;
	}
}
