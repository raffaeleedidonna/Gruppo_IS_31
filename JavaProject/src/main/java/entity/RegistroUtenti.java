package entity;

import database.GestorePersistenza;

public class RegistroUtenti {
	public GestorePersistenza gestorePersistenza;

	public boolean registraCliente(String email, String passwordHash, String nome, String cognome, String indirizzo, String percorsoImmagineProfilo) {
		throw new UnsupportedOperationException();
	}

	public Utente cercaUtentePerEmail(String email) {
		throw new UnsupportedOperationException();
	}

	public Utente cercaUtentePerCredenziali(String email, String passwordHash) {
		throw new UnsupportedOperationException();
	}
}