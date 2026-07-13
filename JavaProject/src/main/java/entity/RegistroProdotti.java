package entity;

import database.GestorePersistenza;

import java.util.List;
import java.util.Map;

public class RegistroProdotti {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public Prodotto cercaProdottoPerId(long idProdotto) {
		return gestorePersistenza.trovaPerId(Prodotto.class, idProdotto);
	}

	public List<Prodotto> cercaProdottiInCatalogo() {

		return gestorePersistenza.cercaPerCampi(Prodotto.class, Map.of("disponibile", true));
	}
}
