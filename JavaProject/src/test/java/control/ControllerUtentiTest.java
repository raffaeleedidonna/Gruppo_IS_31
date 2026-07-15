package control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import support.BaseTestH2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerUtentiTest extends BaseTestH2 {

	@TempDir
	Path cartellaTemporanea;

	@Test
	@DisplayName("TCBB1 - Registrazione con email non ancora registrata")
	void registrazioneConEmailNuova() {
		assertTrue(ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", null));
	}

	@Test
	@DisplayName("TCBB2 - Registrazione con email già registrata")
	void registrazioneConEmailGiaPresente() {
		creaCliente("mario@test.it", "password");

		assertFalse(ControllerUtenti.registra("mario@test.it", "altra", "Mario", "Rossi", "Via Roma 1", null));
	}

	@Test
	@DisplayName("TCBB3 - Registrazione con percorso immagine non leggibile")
	void registrazioneConImmagineNonLeggibile() throws IOException {
		Path file = cartellaTemporanea.resolve("non-una-immagine.txt");
		Files.writeString(file, "questo non è un file immagine");

		assertTrue(ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", file.toString()));
	}

	@Test
	@DisplayName("TCBB4 - Registrazione con immagine di profilo valida")
	void registrazioneConImmagineValida() throws IOException {
		Path file = cartellaTemporanea.resolve("profilo.png");
		ImageIO.write(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), "png", new File(file.toString()));

		assertTrue(ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", file.toString()));
	}

	@Test
	@DisplayName("TCWB4 - Registrazione con percorso immagine vuoto")
	void registrazioneConPercorsoImmagineVuoto() {
		assertTrue(ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", "   "));
	}

	@Test
	@DisplayName("TCWB5 - Registrazione con percorso immagine inesistente")
	void registrazioneConPercorsoImmagineInesistente() {
		String inesistente = cartellaTemporanea.resolve("non-esiste.png").toString();

		assertTrue(ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", inesistente));
	}

	@Test
	@DisplayName("TCBB5 - Autenticazione di un cliente con credenziali corrette")
	void autenticazioneClienteCorretta() {
		creaCliente("mario@test.it", "password");

		String[] esito = ControllerUtenti.autentica("mario@test.it", "password");

		assertNotNull(esito);
		assertEquals("CLIENTE", esito[1]);
	}

	@Test
	@DisplayName("TCBB6 - Autenticazione di un amministratore con credenziali corrette")
	void autenticazioneAmministratoreCorretta() {
		creaAmministratore("admin@test.it", "admin");

		String[] esito = ControllerUtenti.autentica("admin@test.it", "admin");

		assertNotNull(esito);
		assertEquals("AMMINISTRATORE", esito[1]);
	}

	@Test
	@DisplayName("TCBB7 - Autenticazione con email non registrata")
	void autenticazioneConEmailInesistente() {
		assertNull(ControllerUtenti.autentica("nessuno@test.it", "password"));
	}

	@Test
	@DisplayName("TCBB8 - Autenticazione con password errata")
	void autenticazioneConPasswordErrata() {
		creaCliente("mario@test.it", "password");

		assertNull(ControllerUtenti.autentica("mario@test.it", "sbagliata"));
	}

	@Test
	@DisplayName("La password non viene salvata in chiaro")
	void laPasswordVieneSalvataComeHash() {
		ControllerUtenti.registra("mario@test.it", "password", "Mario", "Rossi", "Via Roma 1", null);

		assertNull(ControllerUtenti.autentica("mario@test.it", hash("password")));
	}
}
