package control;

import entity.Carrello;
import entity.Prodotto;
import entity.RegistroCarrello;
import entity.RegistroProdotti;

public class ControllerCarrello {

	public static final int PRODOTTO_AGGIUNTO = 0;
	public static final int NON_AGGIUNGIBILE = 1;
	public static final int PRODOTTO_NON_ESISTENTE = 2;
  public static final int QUANTITA_NON_VALIDA = 3;
  public static final int ERRORE_DI_SISTEMA = 4;

	public static int aggiungiAlCarrello(long idCliente, long idProdotto, long quantita) {

		RegistroCarrello regc = new RegistroCarrello();
    RegistroProdotti regp = new RegistroProdotti();

		Carrello c = regc.cercaCarrelloPerCliente(idCliente);
		Prodotto p = regp.cercaProdottoPerId(idProdotto);

		if (p == null) {return PRODOTTO_NON_ESISTENTE;}
    // Il carrello non dovrebbe poter essere null perchè inizializzato a registrazione utente.
    // Potremmo valutare di ritentare l'inizializzazione altrimenti il problema si ripresenterebbe.
    if (c == null) {return ERRORE_DI_SISTEMA;}
    // Teoricamente dovrebbe essere la boundary ad enforcare <=0 ma restiamo difensivi.
    if (quantita <= 0) {return QUANTITA_NON_VALIDA;}
    // Fondiamo in un solo errore scorta insufficiente e prodotto non disponibile.
    if (!c.puoAggiungere(p, quantita)) {return NON_AGGIUNGIBILE;}

    c.aggiungiProdotto(p, quantita);

    return regc.registraCarrello(c) ? PRODOTTO_AGGIUNTO : ERRORE_DI_SISTEMA;
	}
}
