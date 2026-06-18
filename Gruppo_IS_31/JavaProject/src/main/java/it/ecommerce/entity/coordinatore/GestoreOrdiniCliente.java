package it.ecommerce.entity.coordinatore;

import java.util.ArrayList;
import java.util.List;

import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.OrdineRepository;
import it.ecommerce.entity.persistenza.UtenteRepository;
import it.ecommerce.entity.servizi.MessaggioNotifica;
import it.ecommerce.entity.servizi.ServizioNotifiche;

public class GestoreOrdiniCliente {

    private final GestoreTransazioni transazioni;
    private final UtenteRepository utenti;
    private final OrdineRepository ordini;
    private final ServizioNotifiche notifiche;
    private final CatalogoRepository catalogo;

    public GestoreOrdiniCliente(GestoreTransazioni transazioni, UtenteRepository utenti,
                                OrdineRepository ordini, ServizioNotifiche notifiche,
                                CatalogoRepository catalogo) {
        this.transazioni = transazioni;
        this.utenti = utenti;
        this.ordini = ordini;
        this.notifiche = notifiche;
        this.catalogo = catalogo;
    }

    public EsitoConfermaOrdine confermaOrdine(Long clienteId, String indirizzoRichiesto) {
        MessaggioNotifica[] notificaDaInviare = {null};
        EsitoConfermaOrdine esito = transazioni.inTransazione(() -> {
            Cliente cliente = clienteValido(clienteId);
            Carrello carrello = cliente.getCarrello();
            if (carrello == null || carrello.isVuoto()) {
                return new EsitoConfermaOrdine(false, "Il carrello è vuoto.", null);
            }
            if (!disponibilitaSufficiente(carrello)) {
                adeguaCarrello(carrello);
                utenti.salva(cliente);
                return new EsitoConfermaOrdine(false,
                        "Disponibilità insufficiente per alcuni prodotti. "
                                + "Il carrello è stato aggiornato con le quantità disponibili.", null);
            }
            Ordine ordine = new Ordine(cliente, indirizzoDestinazione(cliente, indirizzoRichiesto));
            for (RigaCarrello riga : carrello.getRighe()) {
                ProdottoCatalogo voce = voceRichiesta(riga.getProdotto().getId());
                ordine.aggiungiRiga(riga.getProdotto(), riga.getQuantita(), voce.getPrezzoAttuale());
                voce.decrementaMagazzino(riga.getQuantita());
            }
            ordine.assicuraNonVuoto();
            Ordine salvato = ordini.salva(ordine);
            notificaDaInviare[0] = new MessaggioNotifica(cliente.getEmail(),
                    "Ordine " + salvato.getId() + " confermato con successo.");
            carrello.svuota();
            utenti.salva(cliente);
            inizializza(salvato);
            return new EsitoConfermaOrdine(true,
                    "Ordine creato con successo. Identificativo: " + salvato.getId() + ".", salvato);
        });
        if (notificaDaInviare[0] != null) {
            notifiche.invia(notificaDaInviare[0]);
        }
        return esito;
    }

    public List<Ordine> storicoOrdini(Long clienteId) {
        return transazioni.inTransazione(() -> {
            clienteValido(clienteId);
            return ordini.perCliente(clienteId);
        });
    }

    public Ordine dettaglioOrdine(Long clienteId, Long ordineId) {
        return transazioni.inTransazione(() -> {
            Ordine ordine = ordini.perId(ordineId).orElse(null);
            if (ordine == null || !ordine.appartieneA(clienteId)) {
                return null;
            }
            return inizializza(ordine);
        });
    }

    private boolean disponibilitaSufficiente(Carrello carrello) {
        return carrello.getRighe().stream().allMatch(riga -> {
            ProdottoCatalogo voce = voceDi(riga.getProdotto());
            return voce != null && voce.disponibilitaSufficiente(riga.getQuantita());
        });
    }

    private void adeguaCarrello(Carrello carrello) {
        for (RigaCarrello riga : new ArrayList<>(carrello.getRighe())) {
            ProdottoCatalogo voce = voceDi(riga.getProdotto());
            int massimo = (voce != null && voce.isDisponibile()) ? voce.getQuantitaMagazzino() : 0;
            if (riga.getQuantita() > massimo) {
                if (massimo <= 0) {
                    carrello.rimuoviRiga(riga);
                } else {
                    riga.impostaQuantita(massimo);
                }
            }
        }
    }

    private ProdottoCatalogo voceDi(Prodotto prodotto) {
        return catalogo.vocePerProdotto(prodotto.getId()).orElse(null);
    }

    private ProdottoCatalogo voceRichiesta(Long prodottoId) {
        return catalogo.vocePerProdotto(prodottoId)
                .orElseThrow(() -> new EccezioneValidazione("Prodotto non più disponibile a catalogo."));
    }

    private String indirizzoDestinazione(Cliente cliente, String indirizzoRichiesto) {
        if (indirizzoRichiesto != null && !indirizzoRichiesto.isBlank()) {
            return indirizzoRichiesto;
        }
        Profilo profilo = cliente.getProfilo();
        return profilo == null ? null : profilo.getIndirizzoSpedizionePrincipale();
    }

    private Cliente clienteValido(Long clienteId) {
        Utente utente = utenti.perId(clienteId)
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
