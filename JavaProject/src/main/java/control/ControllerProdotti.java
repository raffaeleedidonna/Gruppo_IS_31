package control;

import entity.Prodotto;
import entity.RegistroProdotti;

import java.util.ArrayList;
import java.util.List;

public class ControllerProdotti {

	public static List<String[]> getProdotti() {
		RegistroProdotti reg = new RegistroProdotti();
		List<Prodotto> prodotti = reg.cercaProdottiInCatalogo();
		List<String[]> righe = new ArrayList<>();

		for (Prodotto p : prodotti) {
			String[] riga = new String[] {
				String.valueOf(p.getId()),
				p.getNome(),
				p.getDescrizione(),
				String.valueOf(p.getPrezzo()),
				String.valueOf(p.getQuantitaMagazzino()),
				String.valueOf(p.isDisponibile()),
				String.valueOf(p.isInOfferta()),
				String.valueOf(p.getCategoria().getNome()),
			};

			righe.add(riga);
		}

		return righe;
	}

}
