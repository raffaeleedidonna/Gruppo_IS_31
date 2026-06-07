package it.ecommerce.entity.coordinatore;

import java.util.ArrayList;
import java.util.List;

import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Notifica;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreOrdiniCliente {

    private final FornitorePersistenza fornitore;

    public GestoreOrdiniCliente() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreOrdiniCliente(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public EsitoConfermaOrdine confermaOrdine(Long clienteId, String indirizzoRichiesto) {
        return fornitore.inTransazione(() -> {
            Cliente cliente = clienteValido(clienteId);
            Carrello carrello = cliente.getCarrello();
            if (carrello == null || carrello.isVuoto()) {
                return new EsitoConfermaOrdine(false, "Il carrello è vuoto.", null);
            }
            if (!disponibilitaSufficiente(carrello)) {
                adeguaCarrello(carrello);
                fornitore.utenteDAO().salva(cliente);
                return new EsitoConfermaOrdine(false,
                        "Disponibilità insufficiente per alcuni prodotti. "
                                + "Il carrello è stato aggiornato con le quantità disponibili.", null);
            }
            Ordine ordine = new Ordine(cliente, indirizzoDestinazione(cliente, indirizzoRichiesto));
            for (RigaCarrello riga : carrello.getRighe()) {
                Prodotto prodotto = riga.getProdotto();
                ordine.aggiungiRiga(prodotto, riga.getQuantita(), prodotto.getPrezzoAttuale());
                prodotto.decrementaMagazzino(riga.getQuantita());
            }
            Ordine salvato = fornitore.ordineDAO().salva(ordine);
            carrello.svuota();
            fornitore.utenteDAO().salva(cliente);
            fornitore.notificaDAO().salva(new Notifica(cliente,
                    "Ordine " + salvato.getId() + " confermato con successo."));
            inizializza(salvato);
            return new EsitoConfermaOrdine(true,
                    "Ordine creato con successo. Identificativo: " + salvato.getId() + ".", salvato);
        });
    }

    public List<Ordine> storicoOrdini(Long clienteId) {
        return fornitore.inTransazione(() -> {
            clienteValido(clienteId);
            return fornitore.ordineDAO().perCliente(clienteId);
        });
    }

    public Ordine dettaglioOrdine(Long clienteId, Long ordineId) {
        return fornitore.inTransazione(() -> {
            Ordine ordine = fornitore.ordineDAO().perId(ordineId).orElse(null);
            if (ordine == null || !ordine.appartieneA(clienteId)) {
                return null;
            }
            return inizializza(ordine);
        });
    }

    private boolean disponibilitaSufficiente(Carrello carrello) {
        return carrello.getRighe().stream()
                .allMatch(riga -> riga.getProdotto().disponibilitaSufficiente(riga.getQuantita()));
    }

    private void adeguaCarrello(Carrello carrello) {
        for (RigaCarrello riga : new ArrayList<>(carrello.getRighe())) {
            Prodotto prodotto = riga.getProdotto();
            int massimo = prodotto.isDisponibile() ? prodotto.getQuantitaMagazzino() : 0;
            if (riga.getQuantita() > massimo) {
                if (massimo <= 0) {
                    carrello.rimuoviRiga(riga);
                } else {
                    riga.impostaQuantita(massimo);
                }
            }
        }
    }

    private String indirizzoDestinazione(Cliente cliente, String indirizzoRichiesto) {
        if (indirizzoRichiesto != null && !indirizzoRichiesto.isBlank()) {
            return indirizzoRichiesto;
        }
        Profilo profilo = cliente.getProfilo();
        return profilo == null ? null : profilo.getIndirizzoSpedizionePrincipale();
    }

    private Cliente clienteValido(Long clienteId) {
        Utente utente = fornitore.utenteDAO().perId(clienteId)
                .orElseThrow(() -> new EccezioneValidazione("Utente non trovato."));
        if (!(utente instanceof Cliente cliente)) {
            throw new EccezioneValidazione("Operazione consentita solo ai clienti.");
        }
        return cliente;
    }

    private Ordine inizializza(Ordine ordine) {
        ordine.getRighe().size();
        return ordine;
    }
}
