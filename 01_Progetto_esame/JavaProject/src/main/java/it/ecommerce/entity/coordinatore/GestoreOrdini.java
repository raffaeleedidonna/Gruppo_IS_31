package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.OrdineRepository;
import it.ecommerce.entity.servizi.MessaggioNotifica;
import it.ecommerce.entity.servizi.ServizioNotifiche;

public class GestoreOrdini {

    private final GestoreTransazioni transazioni;
    private final OrdineRepository ordini;
    private final CatalogoRepository catalogo;
    private final ServizioNotifiche notifiche;

    public GestoreOrdini(GestoreTransazioni transazioni, OrdineRepository ordini,
                         CatalogoRepository catalogo, ServizioNotifiche notifiche) {
        this.transazioni = transazioni;
        this.ordini = ordini;
        this.catalogo = catalogo;
        this.notifiche = notifiche;
    }

    public List<Ordine> elencoOrdini() {
        return transazioni.inTransazione(ordini::tutti);
    }

    public Ordine dettaglio(Long ordineId) {
        return transazioni.inTransazione(() -> {
            Ordine ordine = ordini.perId(ordineId).orElse(null);
            return ordine == null ? null : inizializza(ordine);
        });
    }

    public Ordine aggiornaStato(Long ordineId, StatoOrdine nuovoStato) {
        MessaggioNotifica[] notificaDaInviare = {null};
        Ordine aggiornato = transazioni.inTransazione(() -> {
            Ordine ordine = ordini.perId(ordineId)
                    .orElseThrow(() -> new EccezioneValidazione("Ordine non trovato."));
            if (nuovoStato == StatoOrdine.ANNULLATO && ordine.getStato() != StatoOrdine.ANNULLATO) {
                annulla(ordine);
            } else {
                ordine.cambiaStato(nuovoStato);
            }
            Ordine salvato = inizializza(ordini.salva(ordine));
            notificaDaInviare[0] = new MessaggioNotifica(salvato.getCliente().getEmail(),
                    "Lo stato dell'ordine " + salvato.getId() + " è stato aggiornato a "
                            + salvato.getStato() + ".");
            return salvato;
        });
        notifiche.invia(notificaDaInviare[0]);
        return aggiornato;
    }

    private void annulla(Ordine ordine) {
        for (RigaOrdine riga : ordine.getRighe()) {
            catalogo.vocePerProdotto(riga.getProdotto().getId())
                    .ifPresent(voce -> voce.incrementaMagazzino(riga.getQuantitaAcquistata()));
        }
        ordine.cambiaStato(StatoOrdine.ANNULLATO);
    }

    private Ordine inizializza(Ordine ordine) {
        ordine.getRighe().size();
        return ordine;
    }
}
