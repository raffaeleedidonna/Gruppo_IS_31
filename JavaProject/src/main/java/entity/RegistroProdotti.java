package entity;

import database.GestorePersistenza;

public class RegistroProdotti {
	public GestorePersistenza gestorePersistenza;

	public Prodotto cercaProdottoPerId(long idProdotto) {
		throw new UnsupportedOperationException();
	}

	public List<Prodotto> cercaProdottiInCatalogo() {
		throw new UnsupportedOperationException();
	}
}