package entity;

import database.GestorePersistenza;

import java.util.Map;

public class RegistroCarrello {
	public GestorePersistenza gestorePersistenza;

	public Carrello cercaCarrelloPerCliente(long idCliente) {

		return gestorePersistenza.cercaPrimoPerCampi(
				Carrello.class,
				Map.of("cliente.id", idCliente)
		);
	}


	public boolean registraCarrello(Carrello carrello) {
		return gestorePersistenza.salva(carrello);
	}
}