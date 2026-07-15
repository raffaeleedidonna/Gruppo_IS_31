package control;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.*;

public class ControllerOrdini {
	public static boolean confermaOrdine(long idCliente) {
		RegistroCarrello reg_c = new RegistroCarrello();
		RegistroOrdini reg_o = new RegistroOrdini();

		Carrello carrello = reg_c.cercaCarrelloPerCliente(idCliente);

		if (carrello == null || carrello.isEmpty()) {return false;}

		if (!carrello.haTutteRigheVendibili()) {return false;}

		return reg_o.registraOrdineDa(carrello);
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
