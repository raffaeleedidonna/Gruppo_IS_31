package it.ecommerce.entity.persistenza;

import it.ecommerce.entity.servizi.ServizioNotifiche;

public interface FornitorePersistenza {

    GestoreTransazioni gestoreTransazioni();

    CatalogoRepository catalogoRepository();

    CategoriaRepository categoriaRepository();

    UtenteRepository utenteRepository();

    OrdineRepository ordineRepository();

    ServizioNotifiche servizioNotifiche();
}
