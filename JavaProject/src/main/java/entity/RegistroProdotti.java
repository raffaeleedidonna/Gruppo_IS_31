package entity;

import database.GestorePersistenza;

import java.util.List;

public class RegistroProdotti {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public Prodotto cercaProdottoPerId(long idProdotto) {
		return gestorePersistenza.trovaPerId(Prodotto.class, idProdotto);
  }

	public List<Prodotto> cercaProdottiInCatalogo() {
		throw new UnsupportedOperationException();
	}
}
