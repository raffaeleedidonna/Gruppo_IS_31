package control;

import entity.Carrello;
import entity.Prodotto;
import entity.RegistroCarrello;
import entity.RegistroProdotti;

public class ControllerCarrello {

	public static final int PRODOTTO_AGGIUNTO = 0;
	public static final int NON_DISPONIBILE = 1;
	public static final int PRODOTTO_NON_ESISTENTE = 2;

	public static boolean aggiungiAlCarrello(long idCliente, long idProdotto, long quantita) {

		RegistroCarrello regc = new RegistroCarrello();

		Carrello c = regc.cercaCarrelloPerCliente(idCliente);

		RegistroProdotti regp = new RegistroProdotti();

		Prodotto p = regp.cercaProdottoPerId(idProdotto);

		if (p == null || c == null || quantita <= 0){
			return false;
		}

		long quantitaGiaPresente = c.getQuantitaProdotto(idProdotto);

		if (quantita + quantitaGiaPresente > p.getQuantitaMagazzino()){
			return false;
		}

		//Questo è l'esito che sarà mandato alla boundary
		return c.aggiungiProdotto(p, quantita) && regc.registraCarrello(c);

	}
}