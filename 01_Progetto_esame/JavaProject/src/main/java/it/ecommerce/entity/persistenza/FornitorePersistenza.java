package it.ecommerce.entity.persistenza;

public interface FornitorePersistenza {

    GestoreTransazioni gestoreTransazioni();

    CatalogoRepository catalogoRepository();

    CategoriaRepository categoriaRepository();

    UtenteRepository utenteRepository();

    OrdineRepository ordineRepository();

    NotificaRepository notificaRepository();
}
