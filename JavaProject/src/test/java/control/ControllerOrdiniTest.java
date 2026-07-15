package control;

import entity.Cliente;
import entity.Prodotto;
import entity.StatoOrdine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTestH2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerOrdiniTest extends BaseTestH2 {

	private static final long ID_CLIENTE_INESISTENTE = 9999L;

	@Test
	@DisplayName("TCBB26 - Conferma di un carrello con tutte le righe disponibili")
	void confermaDiUnCarrelloValido() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		assertTrue(ControllerOrdini.confermaOrdine(cliente.getId()));
	}

	@Test
	@DisplayName("TCBB27 - Conferma di un carrello vuoto")
	void confermaDiUnCarrelloVuoto() {
		Cliente cliente = creaCliente("mario@test.it", "password");

		assertFalse(ControllerOrdini.confermaOrdine(cliente.getId()));
	}

	@Test
	@DisplayName("TCBB28 - Conferma con una riga non più disponibile")
	void confermaConRigaNonDisponibile() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		rendiNonDisponibile(mouse.getId());

		assertFalse(ControllerOrdini.confermaOrdine(cliente.getId()));
	}

	@Test
	@DisplayName("TCBB29 - Conferma per un cliente senza carrello")
	void confermaPerClienteInesistente() {
		assertFalse(ControllerOrdini.confermaOrdine(ID_CLIENTE_INESISTENTE));
	}

	@Test
	@DisplayName("TCBB30 - Elenco ordini quando non ce n'è nessuno")
	void elencoOrdiniVuoto() {
		assertTrue(ControllerOrdini.getOrdini().isEmpty());
	}

	@Test
	@DisplayName("TCBB31 - Elenco ordini dopo una conferma")
	void elencoOrdiniDopoUnaConferma() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);
		ControllerOrdini.confermaOrdine(cliente.getId());

		List<String[]> ordini = ControllerOrdini.getOrdini();

		assertEquals(1, ordini.size());
		assertEquals(6, ordini.get(0).length);
	}

	@Test
	@DisplayName("L'ordine confermato nasce nello stato INSERITO col totale del carrello")
	void lOrdineNasceInseritoColTotaleGiusto() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		ControllerOrdini.confermaOrdine(cliente.getId());

		String[] ordine = ControllerOrdini.getOrdini().get(0);
		assertEquals(StatoOrdine.INSERITO.name(), ordine[4]);
		assertEquals(50.0, Double.parseDouble(ordine[2]));
	}

	@Test
	@DisplayName("Confermare un ordine scarica la giacenza di magazzino")
	void laConfermaScaricaIlMagazzino() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		ControllerOrdini.confermaOrdine(cliente.getId());

		assertEquals(8, ricarica(Prodotto.class, mouse.getId()).getQuantitaMagazzino());
	}

	@Test
	@DisplayName("Confermare un ordine svuota il carrello")
	void laConfermaSvuotaIlCarrello() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		ControllerCarrello.aggiungiAlCarrello(cliente.getId(), mouse.getId(), 2);

		ControllerOrdini.confermaOrdine(cliente.getId());

		assertTrue(ControllerCarrello.getCarrello(cliente.getId()).isEmpty());
	}
}
