package control;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.*;

public class ControllerOrdini {

	public static final int CARRELLO_VUOTO = 0;
	public static final int ERRORE_DI_SISTEMA = 1;
	public static final int RIGA_NON_VENDIBILE = 2;
	public static final int ORDINE_CONFERMATO = 3;

	public static int confermaOrdine(long idCliente) {
		RegistroCarrello reg_c = new RegistroCarrello();
		RegistroOrdini reg_o = new RegistroOrdini();

		Carrello carrello = reg_c.cercaCarrelloPerCliente(idCliente);

		if (carrello == null) {return ERRORE_DI_SISTEMA;}

		if (carrello.isEmpty()) {return CARRELLO_VUOTO;}

		if (!carrello.haTutteRigheVendibili()) {return RIGA_NON_VENDIBILE;}

		boolean esito = reg_o.registraOrdineDa(carrello);

		if(!esito){return ERRORE_DI_SISTEMA;}

		return ORDINE_CONFERMATO;
	}

	public static List<String[]> getOrdini() {
		RegistroOrdini reg = new RegistroOrdini();
		List<Ordine> ordini = reg.cercaOrdini();
		List<String[]> righe = new ArrayList<>();

		for (Ordine ordine : ordini){
			righe.add(toArray(ordine));
		}
		return righe;
	}

	public static List<String[]> getOrdini(long idCliente) {

		RegistroOrdini reg = new RegistroOrdini();
		List<Ordine> ordini = reg.cercaOrdini(idCliente);
		List<String[]> righe = new ArrayList<>();

		for (Ordine ordine : ordini){
			righe.add(toArray(ordine));
		}
		return righe;
	}

	private static String[] toArray(Ordine ordine) {
		return new String[]{
			String.valueOf(ordine.getId()),
			ordine.getDataCreazione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
			String.format(Locale.ROOT, "%.2f", ordine.getTotaleComplessivo()),
			ordine.getIndirizzoSpedizione(),
			ordine.getStato().name(),
			String.valueOf(ordine.getCliente().getId()),
		};
	}
}
