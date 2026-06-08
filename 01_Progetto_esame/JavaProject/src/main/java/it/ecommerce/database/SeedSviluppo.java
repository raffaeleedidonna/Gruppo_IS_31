package it.ecommerce.database;

import java.math.BigDecimal;
import java.util.List;

import it.ecommerce.entity.Amministratore;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.FornitorePersistenza;

final class SeedSviluppo {

    private SeedSviluppo() {
    }

    static void popolaSeNecessario(FornitorePersistenza fornitore) {
        fornitore.gestoreTransazioni().inTransazione(() -> {
            popolaCatalogo(fornitore);
            popolaUtenti(fornitore);
            popolaOrdini(fornitore);
            return null;
        });
    }

    private static void popolaCatalogo(FornitorePersistenza fornitore) {
        if (!fornitore.categoriaRepository().tutte().isEmpty()) {
            return;
        }
        Categoria elettronica = fornitore.categoriaRepository().salva(new Categoria("Elettronica"));
        Categoria libri = fornitore.categoriaRepository().salva(new Categoria("Libri"));
        Categoria casa = fornitore.categoriaRepository().salva(new Categoria("Casa"));

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
        if (!fornitore.utenteRepository().esisteEmail("admin@ecommerce.it")) {
            fornitore.utenteRepository().salva(new Amministratore("admin@ecommerce.it", "admin",
                    new Profilo("Anna", "Bianchi", null, null)));
        }
        if (!fornitore.utenteRepository().esisteEmail("cliente@ecommerce.it")) {
            fornitore.utenteRepository().salva(new Cliente("cliente@ecommerce.it", "cliente",
                    new Profilo("Marco", "Rossi", "Via Roma 1, Milano", null)));
        }
    }

    private static void popolaOrdini(FornitorePersistenza fornitore) {
        if (!fornitore.ordineRepository().tutti().isEmpty()) {
            return;
        }
        Utente utente = fornitore.utenteRepository().perEmail("cliente@ecommerce.it").orElse(null);
        if (!(utente instanceof Cliente cliente)) {
            return;
        }
        List<ProdottoCatalogo> voci = fornitore.catalogoRepository().tutte();
        if (voci.isEmpty()) {
            return;
        }
        salvaOrdine(fornitore, cliente, voci.get(0), 2, StatoOrdine.CONSEGNATO);
        if (voci.size() > 1) {
            salvaOrdine(fornitore, cliente, voci.get(1), 1, StatoOrdine.SPEDITO);
        }
    }

    private static void salvaProdotto(FornitorePersistenza fornitore, String nome, String descrizione,
                                      BigDecimal prezzo, int quantita, boolean disponibile,
                                      boolean inOfferta, Categoria categoria) {
        Prodotto prodotto = new Prodotto(nome, descrizione, categoria);
        ProdottoCatalogo voce = new ProdottoCatalogo(prodotto, prezzo, quantita, disponibile, inOfferta);
        fornitore.catalogoRepository().salva(voce);
    }

    private static void salvaOrdine(FornitorePersistenza fornitore, Cliente cliente,
                                    ProdottoCatalogo voce, int quantita, StatoOrdine stato) {
        Profilo profilo = cliente.getProfilo();
        String indirizzo = profilo == null ? null : profilo.getIndirizzoSpedizionePrincipale();
        Ordine ordine = new Ordine(cliente, indirizzo);
        ordine.aggiungiRiga(voce.getProdotto(), quantita, voce.getPrezzoAttuale());
        voce.decrementaMagazzino(quantita);
        ordine.cambiaStato(stato);
        fornitore.ordineRepository().salva(ordine);
    }
}
