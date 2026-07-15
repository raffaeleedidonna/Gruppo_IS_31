package control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTestH2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerProdottiTest extends BaseTestH2 {

	@Test
	@DisplayName("TCBB9 - Catalogo senza prodotti")
	void catalogoVuoto() {
		assertTrue(ControllerProdotti.getProdotti().isEmpty());
	}

	@Test
	@DisplayName("TCBB10 - Catalogo con prodotti disponibili")
	void catalogoConProdottiDisponibili() {
		creaProdotto("Mouse", 25.0, 10, true);
		creaProdotto("Tastiera", 40.0, 5, true);

		assertEquals(2, ControllerProdotti.getProdotti().size());
	}

	@Test
	@DisplayName("Il catalogo espone solo i prodotti disponibili")
	void ilCatalogoEscludeIProdottiNonDisponibili() {
		creaProdotto("Mouse", 25.0, 10, true);
		creaProdotto("Tastiera", 40.0, 5, false);

		List<String[]> catalogo = ControllerProdotti.getProdotti();

		assertEquals(1, catalogo.size());
		assertEquals("Mouse", catalogo.get(0)[1]);
	}

	@Test
	@DisplayName("Ogni riga del catalogo espone gli attributi del prodotto e la sua categoria")
	void ogniRigaEsponeGliAttributiDelProdotto() {
		creaProdotto("Mouse", 25.0, 10, true, creaCategoria("Informatica"));

		String[] riga = ControllerProdotti.getProdotti().get(0);

		assertEquals(8, riga.length);
		assertEquals("Mouse", riga[1]);
		assertEquals(25.0, Double.parseDouble(riga[3]));
		assertEquals(10, Long.parseLong(riga[4]));
		assertEquals("Informatica", riga[7]);
	}
}
