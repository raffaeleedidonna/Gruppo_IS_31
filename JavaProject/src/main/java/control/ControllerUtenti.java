package control;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;

import entity.RegistroUtenti;
import entity.Utente;

public class ControllerUtenti {

	private static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}

	private static byte[] leggiBytesImmagine(String percorso) {
		byte[] bytes = null;
		if (percorso != null && !percorso.isBlank()) {
				try {
						if (ImageIO.read(new File(percorso)) != null) {
								bytes = Files.readAllBytes(Path.of(percorso));
						}
				} catch (IOException e) {
						// se non leggibile si procede senza (bytes resta null)
				}
		}
		return bytes;
	}

	public static boolean registra(String email, String password, String nome, String cognome, String indirizzo, String percorsoImmagineProfilo) {
		RegistroUtenti reg = new RegistroUtenti();
		Utente utente = reg.cercaUtentePerEmail(email);

		if (utente!=null) {return false;}

		byte[] bytes = leggiBytesImmagine(percorsoImmagineProfilo);
		boolean registrato = reg.registraCliente(email, hashPassword(password), nome, cognome, indirizzo, bytes);

		if (registrato) {
			GestoreNotifiche.getInstance().invia(email, "Benvenuto",
				"La tua registrazione è andata a buon fine.");
		}
		return registrato;
	}

	public static String[] autentica(String email, String password) {
		RegistroUtenti reg = new RegistroUtenti();

		Utente utente = reg.cercaUtentePerCredenziali(email, hashPassword(password));

		if (utente != null) {
			return new String[] {String.valueOf(utente.getId()), utente.ruolo().name()};
		}

		return null;
	}
}
