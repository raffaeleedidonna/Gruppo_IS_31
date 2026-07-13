package entity;

import database.GestorePersistenza;

import java.util.List;
import java.util.Map;

public class RegistroOrdini {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public boolean registraOrdineDa(Carrello carrello, String indirizzoSpedizione) {
		throw new UnsupportedOperationException();
	}

	public List<Ordine> cercaOrdini() {
		return gestorePersistenza.cercaPerCampi(Ordine.class, Map.of());
	}
}