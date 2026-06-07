package it.ecommerce.database;

import java.math.BigDecimal;

import it.ecommerce.entity.Amministratore;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.persistenza.FornitorePersistenza;

final class SeedSviluppo {

    private SeedSviluppo() {
    }

    static void popolaSeNecessario(FornitorePersistenza fornitore) {
        fornitore.inTransazione(() -> {
            popolaCatalogo(fornitore);
            popolaUtenti(fornitore);
            return null;
        });
    }

    private static void popolaCatalogo(FornitorePersistenza fornitore) {
        if (!fornitore.categoriaDAO().tutte().isEmpty()) {
            return;
        }
        Categoria elettronica = fornitore.categoriaDAO().salva(new Categoria("Elettronica"));
        Categoria libri = fornitore.categoriaDAO().salva(new Categoria("Libri"));
        Categoria casa = fornitore.categoriaDAO().salva(new Categoria("Casa"));

        salvaProdotto(fornitore, "Cuffie Bluetooth", "Cuffie over-ear con cancellazione del rumore",
                new BigDecimal("79.90"), 25, true, true, elettronica);
        salvaProdotto(fornitore, "Mouse Wireless", "Mouse ergonomico ricaricabile",
                new BigDecimal("24.50"), 60, true, false, elettronica);
        salvaProdotto(fornitore, "Il Signore degli Anelli", "Edizione integrale con copertina rigida",
                new BigDecimal("34.00"), 15, true, true, libri);
        salvaProdotto(fornitore, "Lampada da Scrivania", "Lampada LED con luce regolabile",
                new BigDecimal("39.99"), 0, false, false, casa);
        salvaProdotto(fornitore, "Set di Tazze", "Set di sei tazze in ceramica",
                new BigDecimal("19.90"), 40, true, false, casa);
    }

    private static void popolaUtenti(FornitorePersistenza fornitore) {
        if (!fornitore.utenteDAO().esisteEmail("admin@ecommerce.it")) {
            fornitore.utenteDAO().salva(new Amministratore("admin@ecommerce.it", "admin",
                    "Anna", "Bianchi", new Profilo()));
        }
        if (!fornitore.utenteDAO().esisteEmail("cliente@ecommerce.it")) {
            fornitore.utenteDAO().salva(new Cliente("cliente@ecommerce.it", "cliente",
                    "Marco", "Rossi", new Profilo("Marco Rossi", "Via Roma 1, Milano", null)));
        }
    }

    private static void salvaProdotto(FornitorePersistenza fornitore, String nome, String descrizione,
                                      BigDecimal prezzo, int quantita, boolean disponibile,
                                      boolean inOfferta, Categoria categoria) {
        Prodotto prodotto = new Prodotto(nome, descrizione, prezzo, quantita, disponibile, inOfferta);
        prodotto.setCategoria(categoria);
        fornitore.prodottoDAO().salva(prodotto);
    }
}
