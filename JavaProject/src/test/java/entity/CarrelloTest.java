package entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTestH2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrelloTest extends BaseTestH2 {

	private Carrello carrelloNuovo() {
		return new Carrello(creaCliente("mario@test.it", "password"));
	}

	@Test
	@DisplayName("Il totale di un carrello vuoto è zero")
	void totaleDiUnCarrelloVuoto() {
		assertEquals(0.0, carrelloNuovo().calcolaTotale());
	}

	@Test
	@DisplayName("Il totale di un carrello con più righe è la somma dei subtotali")
	void totaleConPiuRighe() {
		Carrello carrello = carrelloNuovo();

		carrello.aggiungiProdotto(creaProdotto("Mouse", 25.0, 10, true), 2);
		carrello.aggiungiProdotto(creaProdotto("Tastiera", 40.0, 10, true), 1);

		assertEquals(90.0, carrello.calcolaTotale());
	}

	@Test
	@DisplayName("Un carrello nuovo è vuoto")
	void carrelloNuovoEVuoto() {
		assertTrue(carrelloNuovo().isEmpty());
	}

	@Test
	@DisplayName("Si può aggiungere una quantità entro la giacenza")
	void puoAggiungereEntroLaGiacenza() {
		Carrello carrello = carrelloNuovo();

		assertTrue(carrello.puoAggiungere(creaProdotto("Mouse", 25.0, 10, true), 10));
	}

	@Test
	@DisplayName("Non si può aggiungere una quantità oltre la giacenza")
	void nonPuoAggiungereOltreLaGiacenza() {
		Carrello carrello = carrelloNuovo();

		assertFalse(carrello.puoAggiungere(creaProdotto("Mouse", 25.0, 10, true), 11));
	}

	@Test
	@DisplayName("puoAggiungere tiene conto di quanto è già nel carrello")
	void puoAggiungereConsideraLAccumulo() {
		Carrello carrello = carrelloNuovo();
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		carrello.aggiungiProdotto(mouse, 6);

		assertFalse(carrello.puoAggiungere(mouse, 6));
	}

	@Test
	@DisplayName("Aggiungere due volte lo stesso prodotto somma le quantità su una sola riga")
	void aggiungereDueVolteSommaSuUnaRiga() {
		Carrello carrello = carrelloNuovo();
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);

		carrello.aggiungiProdotto(mouse, 2);
		carrello.aggiungiProdotto(mouse, 3);

		assertEquals(1, carrello.getRighe().size());
		assertEquals(5, carrello.getRighe().get(0).getQuantita());
	}

	@Test
	@DisplayName("Modificare la quantità a zero rimuove la riga dal carrello")
	void modificareAZeroRimuoveLaRiga() {
		Carrello carrello = carrelloNuovo();
		Prodotto mouse = creaProdotto("Mouse", 25.0, 10, true);
		carrello.aggiungiProdotto(mouse, 2);

		carrello.modificaQuantita(mouse, 0);

		assertTrue(carrello.isEmpty());
	}

	@Test
	@DisplayName("Non si può modificare un prodotto che non è nel carrello")
	void nonPuoModificareUnProdottoAssente() {
		Carrello carrello = carrelloNuovo();

		assertFalse(carrello.puoModificare(creaProdotto("Mouse", 25.0, 10, true), 2));
	}

	@Test
	@DisplayName("Un carrello con tutte le righe entro la giacenza è confermabile")
	void tutteLeRigheVendibili() {
		Carrello carrello = carrelloNuovo();
		carrello.aggiungiProdotto(creaProdotto("Mouse", 25.0, 10, true), 2);

		assertTrue(carrello.haTutteRigheVendibili());
	}

	@Test
	@DisplayName("Un carrello con una riga su prodotto non disponibile non è confermabile")
	void rigaNonVendibileRendeIlCarrelloNonConfermabile() {
		Carrello carrello = carrelloNuovo();
		carrello.aggiungiProdotto(creaProdotto("Mouse", 25.0, 10, false), 2);

		assertFalse(carrello.haTutteRigheVendibili());
	}

	@Test
	@DisplayName("svuota rimuove tutte le righe")
	void svuotaRimuoveTutteLeRighe() {
		Carrello carrello = carrelloNuovo();
		carrello.aggiungiProdotto(creaProdotto("Mouse", 25.0, 10, true), 2);

		carrello.svuota();

		assertTrue(carrello.isEmpty());
	}

	@Test
	@DisplayName("riversaIn trasferisce le righe del carrello sull'ordine")
	void riversaInTrasferisceLeRighe() {
		Cliente cliente = creaCliente("mario@test.it", "password");
		Carrello carrello = new Carrello(cliente);
		carrello.aggiungiProdotto(creaProdotto("Mouse", 25.0, 10, true), 2);
		Ordine ordine = new Ordine(cliente);

		carrello.riversaIn(ordine);

		assertEquals(50.0, ordine.getTotaleComplessivo());
	}
}
