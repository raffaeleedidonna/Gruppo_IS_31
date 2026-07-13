package entity;

import database.GestorePersistenza;

import java.util.List;
import java.util.Map;

public class RegistroProdotti {
	public GestorePersistenza gestorePersistenza;

	public Prodotto cercaProdottoPerId(long idProdotto) {
		return gestorePersistenza.cercaPrimoPerCampi(
				Prodotto.class,
				Map.of("prodotto.id", idProdotto)
		);
	}

	public List<Prodotto> cercaProdottiInCatalogo() {
		throw new UnsupportedOperationException();
	}
}