package entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RigaCarrelloTest {

	private Prodotto prodotto(boolean disponibile, long quantitaMagazzino) {
		return new Prodotto("Mouse", "Mouse ottico", 25.0, quantitaMagazzino, disponibile, false, new Categoria("Informatica"));
	}

	@Test
	@DisplayName("Il subtotale è prezzo per quantità")
	void subtotaleEPrezzoPerQuantita() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertEquals(50.0, riga.calcolaSubtotale());
	}

	@Test
	@DisplayName("incrementaQuantita somma alla quantità già presente")
	void incrementaSommaAllaQuantita() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		riga.incrementaQuantita(3);

		assertEquals(5, riga.getQuantita());
	}

	@Test
	@DisplayName("Si può impostare una quantità entro la giacenza")
	void puoImpostareEntroLaGiacenza() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertTrue(riga.puoImpostare(10));
	}

	@Test
	@DisplayName("Non si può impostare una quantità oltre la giacenza")
	void nonPuoImpostareOltreLaGiacenza() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertFalse(riga.puoImpostare(11));
	}

	@Test
	@DisplayName("Si può impostare zero, che equivale a rimuovere la riga")
	void puoImpostareZeroPerRimuovere() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertTrue(riga.puoImpostare(0));
	}

	@Test
	@DisplayName("Non si può impostare una quantità negativa")
	void nonPuoImpostareQuantitaNegativa() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertFalse(riga.puoImpostare(-1));
	}

	@Test
	@DisplayName("Una riga entro la giacenza è vendibile")
	void rigaEntroLaGiacenzaVendibile() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		assertTrue(riga.isVendibile());
	}

	@Test
	@DisplayName("Una riga su prodotto non disponibile non è vendibile")
	void rigaSuProdottoNonDisponibileNonVendibile() {
		RigaCarrello riga = new RigaCarrello(prodotto(false, 10), 2);

		assertFalse(riga.isVendibile());
	}

	@Test
	@DisplayName("La riga d'ordine generata conserva quantità e prezzo del momento")
	void creaRigaOrdineConservaQuantitaEPrezzo() {
		RigaCarrello riga = new RigaCarrello(prodotto(true, 10), 2);

		RigaOrdine rigaOrdine = riga.creaRigaOrdine();

		assertEquals(50.0, rigaOrdine.calcolaSubtotale());
	}
}
