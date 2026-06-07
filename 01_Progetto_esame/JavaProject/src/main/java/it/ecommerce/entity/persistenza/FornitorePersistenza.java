package it.ecommerce.entity.persistenza;

public interface FornitorePersistenza {

    ProdottoDAO prodottoDAO();

    CategoriaDAO categoriaDAO();

    UtenteDAO utenteDAO();

    OrdineDAO ordineDAO();

    NotificaDAO notificaDAO();

    UnitaDiLavoro apreUnitaDiLavoro();

    default <R> R inTransazione(AzioneTransazionale<R> azione) {
        UnitaDiLavoro unita = apreUnitaDiLavoro();
        try {
            unita.inizia();
            R risultato = azione.esegui();
            unita.conferma();
            return risultato;
        } catch (RuntimeException errore) {
            unita.annulla();
            throw errore;
        } finally {
            unita.chiudi();
        }
    }
}
