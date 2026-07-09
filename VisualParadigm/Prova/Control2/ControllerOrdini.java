package Control2;

import Entity2.RegistroOrdini;

public class ControllerOrdini {

	/**
	 * 
	 * @param clienteId
	 */
	public boolean confermaOrdine(long clienteId) {

		RegistroOrdini reg = new RegistroOrdini();

		Carrello carrello = reg.cercaCarrelloPerCliente(clienteId);

		if (carrello.righe.len() == 0){
			return false;
		}

		for (RigaCarrello riga : righe){

			if (riga.quantita > riga.prodotto.quantitaMagazzino || riga.quantita < 0 || !riga.prodotto.disponibile){

				return false;

			}

			riga.prodotto.quantitaMagazzino -= riga.quantita;

		}

		Ordine ordine = new Ordine(righe_ordine, dataCreazione, totale, indirizzoSpedizione);

		carrello.svuota_carrello();

		reg.registraOrdine(ordine, carrello);


	}

	/**
	 * 
	 * @param clienteId
	 */
	public List<String[]> getStoricoOrdini(long clienteId) {
		// TODO - implement ControllerOrdini.getStoricoOrdini
		throw new UnsupportedOperationException();
	}

}