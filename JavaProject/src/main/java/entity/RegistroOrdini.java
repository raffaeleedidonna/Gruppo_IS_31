package entity;

import java.util.ArrayList;
import java.util.List;

import database.GestorePersistenza;

public class RegistroOrdini {
	private GestorePersistenza gestorePersistenza = new GestorePersistenza();

	public boolean registraOrdineDa(Carrello carrello) {
		Ordine ordine = new Ordine(carrello.getCliente());
    carrello.riversaIn(ordine);
    List<Prodotto> prodottiScaricati = ordine.scaricaMagazzino();
    carrello.svuota();

    List<Object> daPersistere = new ArrayList<>();
    daPersistere.add(ordine);
    daPersistere.add(carrello);
    daPersistere.addAll(prodottiScaricati);

    return gestorePersistenza.salvaTutti(daPersistere.toArray());
	}

	public List<Ordine> cercaOrdini() {
		throw new UnsupportedOperationException();
	}
}
