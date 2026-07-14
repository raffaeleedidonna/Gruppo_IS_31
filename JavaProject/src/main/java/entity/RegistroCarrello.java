package entity;

import java.util.Map;

import database.GestorePersistenza;

public class RegistroCarrello {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public Carrello cercaCarrelloPerCliente(long idCliente) {
		return gestorePersistenza.cercaPrimoPerCampi(
				Carrello.class,
				Map.of("cliente.id", idCliente)
		);
	}

	public boolean registraCarrello(Carrello carrello) {
		return gestorePersistenza.aggiornaTutti(carrello);
	}
}
