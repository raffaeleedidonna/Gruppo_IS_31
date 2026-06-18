package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;

public class GestoreCatalogo {

    private final GestoreTransazioni transazioni;
    private final CatalogoRepository catalogo;

    public GestoreCatalogo(GestoreTransazioni transazioni, CatalogoRepository catalogo) {
        this.transazioni = transazioni;
        this.catalogo = catalogo;
    }

    public List<ProdottoCatalogo> consultaCatalogo() {
        return transazioni.inTransazione(catalogo::tutte);
    }

    public List<ProdottoCatalogo> offerte() {
        return transazioni.inTransazione(catalogo::inOfferta);
    }

    public List<ProdottoCatalogo> cerca(String termine) {
        return transazioni.inTransazione(() -> catalogo.cerca(termine));
    }
}
