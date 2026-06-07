package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreCatalogo {

    private final FornitorePersistenza fornitore;

    public GestoreCatalogo() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreCatalogo(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public List<Prodotto> consultaCatalogo() {
        return fornitore.inTransazione(() -> fornitore.prodottoDAO().tuttiNelCatalogo());
    }

    public List<Prodotto> offerte() {
        return fornitore.inTransazione(() -> fornitore.prodottoDAO().inOfferta());
    }

    public List<Prodotto> cerca(String termine) {
        return fornitore.inTransazione(() -> fornitore.prodottoDAO().cerca(termine));
    }
}
