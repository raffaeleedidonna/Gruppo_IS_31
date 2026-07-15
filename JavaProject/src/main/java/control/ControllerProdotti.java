package control;

import entity.Prodotto;
import entity.RegistroProdotti;
import entity.RigaCarrello;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ControllerProdotti {

	public static List<String[]> getProdotti() {
		RegistroProdotti reg = new RegistroProdotti();
		List<Prodotto> prodotti = reg.cercaProdottiInCatalogo();
		List<String[]> righe = new ArrayList<>();

		for (Prodotto p : prodotti) {
			righe.add(toArray(p));
		}

		return righe;
	}

	private static String[] toArray(Prodotto p) {
		return new String[]{
			String.valueOf(p.getId()),
			p.getNome(),
			p.getDescrizione(),
			String.format(Locale.ROOT, "%.2f", p.getPrezzo()),
			String.valueOf(p.getQuantitaMagazzino()),
			p.isDisponibile() ? "Sì" : "No",
			p.isInOfferta() ? "Sì" : "No",
			String.valueOf(p.getCategoria().getNome()),
		};
	}
}
