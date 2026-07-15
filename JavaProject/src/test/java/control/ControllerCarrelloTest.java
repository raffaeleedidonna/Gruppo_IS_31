package control;

import entity.Cliente;
import entity.Prodotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTestH2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerCarrelloTest extends BaseTestH2 {

	private static final long ID_PRODOTTO_INESISTENTE = 9999L;
	private static final long ID_CLIENTE_INESISTENTE = 9999L;

	@Test
	@DisplayName("TCBB11 - Aggiunta con quantità negativa")
	void aggiuntaConQuantitaNegativa() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.QUANTITA_NON_VALIDA,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), -1));
	}

	@Test
	@DisplayName("TCBB12 - Aggiunta con quantità zero")
	void aggiuntaConQuantitaZero() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.QUANTITA_NON_VALIDA,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 0));
	}

	@Test
	@DisplayName("TCBB13 - Aggiunta al limite inferiore della giacenza")
	void aggiuntaAlLimiteInferiore() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.PRODOTTO_AGGIUNTO,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 1));
	}

	@Test
	@DisplayName("TCBB14 - Aggiunta con quantità interna all'intervallo")
	void aggiuntaConQuantitaInterna() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.PRODOTTO_AGGIUNTO,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 5));
	}

	@Test
	@DisplayName("TCBB15 - Aggiunta al limite superiore, pari alla giacenza")
	void aggiuntaAlLimiteSuperiore() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.PRODOTTO_AGGIUNTO,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 10));
	}

	@Test
	@DisplayName("TCBB16 - Aggiunta oltre il limite superiore")
	void aggiuntaOltreIlLimiteSuperiore() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.NON_AGGIUNGIBILE,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 11));
	}

	@Test
	@DisplayName("TCBB17 - Aggiunta di un prodotto inesistente")
	void aggiuntaDiProdottoInesistente() {
		Cliente cliente = creaCliente("mario@test.it", "password");

		assertEquals(ControllerCarrello.PRODOTTO_NON_ESISTENTE,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), ID_PRODOTTO_INESISTENTE, 1));
	}

	@Test
	@DisplayName("TCBB18 - Aggiunta di un prodotto non disponibile")
	void aggiuntaDiProdottoNonDisponibile() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, false);

		assertEquals(ControllerCarrello.NON_AGGIUNGIBILE,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 1));
	}

	@Test
	@DisplayName("TCBB19 - Due aggiunte che insieme superano la giacenza")
	void aggiunteRipetuteCheSuperanoLaGiacenza() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 6);

		assertEquals(ControllerCarrello.NON_AGGIUNGIBILE,
			ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 6));
	}

	@Test
	@DisplayName("TCBB20 - Modifica a una quantità entro la giacenza")
	void modificaEntroLaGiacenza() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertTrue(ControllerCarrello.modificaQuantita(cliente.getId(), mouse.getId(), 5));
		assertEquals(125.0, totaleDi(cliente.getId()));
	}

	@Test
	@DisplayName("TCBB21 - Modifica a zero, che rimuove il prodotto dal carrello")
	void modificaAZeroRimuoveIlProdotto() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertTrue(ControllerCarrello.modificaQuantita(cliente.getId(), mouse.getId(), 0));
		assertTrue(ControllerCarrello.getCarrello(cliente.getId()).isEmpty());
	}

	@Test
	@DisplayName("TCBB22 - Modifica a una quantità oltre la giacenza")
	void modificaOltreLaGiacenza() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertFalse(ControllerCarrello.modificaQuantita(cliente.getId(), mouse.getId(), 11));
	}

	@Test
	@DisplayName("TCBB23 - Modifica a una quantità negativa")
	void modificaConQuantitaNegativa() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertFalse(ControllerCarrello.modificaQuantita(cliente.getId(), mouse.getId(), -1));
	}

	@Test
	@DisplayName("TCBB24 - Modifica di un prodotto non presente nel carrello")
	void modificaDiProdottoNonInCarrello() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		Prodotto tastiera = creaProdotto("Tastiera", 40.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertFalse(ControllerCarrello.modificaQuantita(cliente.getId(), tastiera.getId(), 2));
	}

	@Test
	@DisplayName("TCBB25 - Modifica di un prodotto inesistente")
	void modificaDiProdottoInesistente() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertFalse(ControllerCarrello.modificaQuantita(cliente.getId(), ID_PRODOTTO_INESISTENTE, 2));
	}

	@Test
	@DisplayName("TCWB1 - Aggiunta di un prodotto valido per un cliente senza carrello")
	void aggiuntaPerClienteSenzaCarrello() {
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertEquals(ControllerCarrello.ERRORE_DI_SISTEMA,
			ControllerCarrello.aggiungiAlCarrello(ID_CLIENTE_INESISTENTE, mouse.getId(), 1));
	}

	@Test
	@DisplayName("TCWB2 - Lettura del carrello di un cliente inesistente")
	void letturaCarrelloDiClienteInesistente() {
		assertTrue(ControllerCarrello.getCarrello(ID_CLIENTE_INESISTENTE).isEmpty());
	}

	@Test
	@DisplayName("TCWB3 - Modifica di un prodotto valido per un cliente senza carrello")
	void modificaPerClienteSenzaCarrello() {
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		assertFalse(ControllerCarrello.modificaQuantita(ID_CLIENTE_INESISTENTE, mouse.getId(), 1));
	}

	@Test
	@DisplayName("Il carrello di un cliente appena registrato è vuoto")
	void carrelloDiUnClienteNuovoEVuoto() {
		Cliente cliente = creaCliente("mario@test.it", "password");

		assertTrue(ControllerCarrello.getCarrello(cliente.getId()).isEmpty());
	}

	@Test
	@DisplayName("getCarrello espone id, nome, prezzo, quantità, subtotale e totale di ogni riga")
	void getCarrelloEsponeIlSubtotaleDiRiga() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		List<String[]> righe = ControllerCarrello.getCarrello(cliente.getId());

		assertEquals(1, righe.size());
		assertEquals(6, righe.get(0).length);
		assertEquals("Mouse", righe.get(0)[1]);
		assertEquals(50.0, Double.parseDouble(righe.get(0)[4]));
		assertEquals(50.0, Double.parseDouble(righe.get(0)[5]));
	}

	@Test
	@DisplayName("Ogni riga espone lo stesso totale complessivo del carrello")
	void ogniRigaEsponeIlTotaleComplessivo() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		Prodotto tastiera = creaProdotto("Tastiera", 40.0, 10, true);

		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), tastiera.getId(), 1);

		List<String[]> righe = ControllerCarrello.getCarrello(cliente.getId());

		assertEquals(2, righe.size());
		for (String[] riga : righe) {
			assertEquals(90.0, Double.parseDouble(riga[5]));
		}
	}

	// Il totale viaggia ripetuto in ogni riga: con carrello vuoto non c'è nessuna riga che lo porti.
	// Stessa lettura che fa FormCarrello.caricaCarrello().
	private static double totaleDi(long idCliente) {
		List<String[]> righe = ControllerCarrello.getCarrello(idCliente);
		return righe.isEmpty() ? 0 : Double.parseDouble(righe.get(0)[5]);
	}
}
