package control;

import entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

		if (p == null) {
			return PRODOTTO_NON_ESISTENTE;
		}
		// Il carrello non dovrebbe poter essere null perchè inizializzato a registrazione utente.
		// Potremmo valutare di ritentare l'inizializzazione altrimenti il problema si ripresenterebbe.
		if (c == null) {
			return ERRORE_DI_SISTEMA;
		}
		// Teoricamente dovrebbe essere la boundary ad enforcare <=0 ma restiamo difensivi.
		if (quantita <= 0) {
			return QUANTITA_NON_VALIDA;
		}
		// Fondiamo in un solo errore scorta insufficiente e prodotto non disponibile.
		if (!c.puoAggiungere(p, quantita)) {
			return NON_AGGIUNGIBILE;
		}

		c.aggiungiProdotto(p, quantita);

		return regc.registraCarrello(c) ? PRODOTTO_AGGIUNTO : ERRORE_DI_SISTEMA;
	}

	public static List<String[]> getCarrello(long idCliente) {

		RegistroCarrello reg = new RegistroCarrello();

		Carrello c = reg.cercaCarrelloPerCliente(idCliente);

		if (c == null) {
			return Collections.emptyList();
		}

		List<RigaCarrello> righe = c.getRighe();

		List<String[]> dati = new ArrayList<>(righe.size());

		double totale = c.calcolaTotale();

		for (RigaCarrello riga : righe) {
			dati.add(toArray(riga, totale));
		}

		return dati;

	}

	public static boolean modificaQuantita(long idCliente, long idProdotto, long nuovaQuantita){

		RegistroCarrello regc = new RegistroCarrello();

		RegistroProdotti regp = new RegistroProdotti();

		Carrello c = regc.cercaCarrelloPerCliente(idCliente);

		Prodotto p = regp.cercaProdottoPerId(idProdotto);

		if (c == null || p == null) {
			return false;
		}

		if(!c.puoModificare(p, nuovaQuantita)){
			return false;
		}

		c.modificaQuantita(p, nuovaQuantita);

		return regc.registraCarrello(c);

	}

	private static String[] toArray(RigaCarrello riga, double totale) {
		Prodotto p = riga.getProdotto();
		return new String[]{
			String.valueOf(p.getId()),
			p.getNome(),
			String.format(Locale.ROOT, "%.2f", p.getPrezzo()),
			String.valueOf(riga.getQuantita()),
			String.format(Locale.ROOT, "%.2f", riga.calcolaSubtotale()),
			String.format(Locale.ROOT, "%.2f", totale)
		};
	}



}
