package entity;

import java.util.Map;
import database.GestorePersistenza;

public class RegistroUtenti {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public boolean registraCliente(String email, String passwordHash, String nome, String cognome, String indirizzo, byte[] immagineProfilo) {
		Cliente cliente = new Cliente(email, passwordHash, nome, cognome, indirizzo, immagineProfilo);
		return gestorePersistenza.salva(cliente);
	}

	public Utente cercaUtentePerEmail(String email) {
		return gestorePersistenza.cercaPrimoPerCampi(Utente.class, Map.of("email", email));
	}

	public Utente cercaUtentePerCredenziali(String email, String passwordHash) {
		return gestorePersistenza.cercaPrimoPerCampi(Utente.class, Map.of("email", email, "passwordHash", passwordHash));
	}
}
