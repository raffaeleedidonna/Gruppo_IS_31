package control;
import database.GestorePersistenza;
import entity.RegistroOrdini;
import entity.Ordine;

import java.util.ArrayList;
import java.util.List;

public class ControllerOrdini {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();
	public static boolean confermaOrdine(long idCliente) {
		throw new UnsupportedOperationException();
	}

	public static List<String[]> getOrdini() {
		RegistroOrdini reg = new RegistroOrdini();
		List<Ordine> ordini = reg.cercaOrdini();
		List<String[]> righe = new ArrayList<>();

		for(Ordine ordine : ordini){
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