package control;
import java.util.ArrayList;
import java.util.List;

import entity.Carrello;
import entity.Ordine;
import entity.RegistroCarrello;
import entity.RegistroOrdini;

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
			String[] riga = new String[]{
					String.valueOf(ordine.getId()),
					ordine.getDataCreazione().toString(),
					String.valueOf(ordine.getTotaleComplessivo()),
					ordine.getIndirizzoSpedizione(),
					ordine.getStato().name()
			};
			righe.add(riga);
		}
		return righe;
	}
}
