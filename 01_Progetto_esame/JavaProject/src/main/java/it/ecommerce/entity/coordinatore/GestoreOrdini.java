package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.OrdineRepository;

public class GestoreOrdini {

    private final GestoreTransazioni transazioni;
    private final OrdineRepository ordini;
    private final CatalogoRepository catalogo;

    public GestoreOrdini(GestoreTransazioni transazioni, OrdineRepository ordini, CatalogoRepository catalogo) {
        this.transazioni = transazioni;
        this.ordini = ordini;
        this.catalogo = catalogo;
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
        return transazioni.inTransazione(() -> {
            Ordine ordine = ordini.perId(ordineId)
                    .orElseThrow(() -> new EccezioneValidazione("Ordine non trovato."));
            if (nuovoStato == StatoOrdine.ANNULLATO && ordine.getStato() != StatoOrdine.ANNULLATO) {
                annulla(ordine);
            } else {
                ordine.cambiaStato(nuovoStato);
            }
            return inizializza(ordini.salva(ordine));
        });
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
