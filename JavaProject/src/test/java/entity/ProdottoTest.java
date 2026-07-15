package entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdottoTest {

	private Prodotto prodotto(boolean disponibile, long quantitaMagazzino) {
		return new Prodotto("Mouse", "Mouse ottico", 25.0, quantitaMagazzino, disponibile, false, new Categoria("Informatica"));
	}

	@Test
	@DisplayName("Disponibile, quantità entro la giacenza: vendibile")
	void vendibileSottoLaGiacenza() {
		assertTrue(prodotto(true, 10).isVendibilePer(1));
	}

	@Test
	@DisplayName("Disponibile, quantità pari alla giacenza: vendibile")
	void vendibileAllaGiacenza() {
		assertTrue(prodotto(true, 10).isVendibilePer(10));
	}

	@Test
	@DisplayName("Disponibile, quantità oltre la giacenza: non vendibile")
	void nonVendibileOltreLaGiacenza() {
		assertFalse(prodotto(true, 10).isVendibilePer(11));
	}

	@Test
	@DisplayName("Non disponibile, quantità entro la giacenza: non vendibile")
	void nonVendibileSeNonDisponibile() {
		assertFalse(prodotto(false, 10).isVendibilePer(1));
	}

	@Test
	@DisplayName("Quantità zero: non vendibile")
	void quantitaZeroNonVendibile() {
		assertFalse(prodotto(true, 10).isVendibilePer(0));
	}

	@Test
	@DisplayName("Quantità negativa: non vendibile")
	void quantitaNegativaNonVendibile() {
		assertFalse(prodotto(true, 10).isVendibilePer(-1));
	}

	@Test
	@DisplayName("scarica riduce la giacenza della quantità acquistata")
	void scaricaRiduceLaGiacenza() {
		Prodotto p = prodotto(true, 10);

		p.scarica(3);

		assertEquals(7, p.getQuantitaMagazzino());
	}
}
