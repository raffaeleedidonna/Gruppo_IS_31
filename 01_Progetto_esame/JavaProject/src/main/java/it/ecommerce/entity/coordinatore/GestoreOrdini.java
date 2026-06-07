package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreOrdini {

    private final FornitorePersistenza fornitore;

    public GestoreOrdini() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreOrdini(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public List<Ordine> elencoOrdini() {
        return fornitore.inTransazione(() -> fornitore.ordineDAO().tutti());
    }

    public Ordine dettaglio(Long ordineId) {
        return fornitore.inTransazione(() -> {
            Ordine ordine = fornitore.ordineDAO().perId(ordineId).orElse(null);
            return ordine == null ? null : inizializza(ordine);
        });
    }

    public Ordine aggiornaStato(Long ordineId, StatoOrdine nuovoStato) {
        return fornitore.inTransazione(() -> {
            Ordine ordine = fornitore.ordineDAO().perId(ordineId)
                    .orElseThrow(() -> new EccezioneValidazione("Ordine non trovato."));
            if (nuovoStato == StatoOrdine.ANNULLATO && ordine.getStato() != StatoOrdine.ANNULLATO) {
                annulla(ordine);
            } else {
                ordine.cambiaStato(nuovoStato);
            }
            return inizializza(fornitore.ordineDAO().salva(ordine));
        });
    }

    private void annulla(Ordine ordine) {
        for (RigaOrdine riga : ordine.getRighe()) {
            riga.getProdotto().incrementaMagazzino(riga.getQuantitaAcquistata());
        }
        ordine.cambiaStato(StatoOrdine.ANNULLATO);
    }

    private Ordine inizializza(Ordine ordine) {
        ordine.getRighe().size();
        return ordine;
    }
}
