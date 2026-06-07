package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.coordinatore.GestoreOrdini;

public class GestioneOrdiniControllerImpl implements GestioneOrdiniController {

    private final GestoreOrdini gestore;

    public GestioneOrdiniControllerImpl() {
        this(new GestoreOrdini());
    }

    public GestioneOrdiniControllerImpl(GestoreOrdini gestore) {
        this.gestore = gestore;
    }

    @Override
    public EsitoDTO<List<OrdineDTO>> elencoOrdini() {
        List<OrdineDTO> ordini = gestore.elencoOrdini().stream()
                .map(MappaOrdini::aDTOIntestazione)
                .toList();
        if (ordini.isEmpty()) {
            return EsitoDTO.errore("Nessun ordine presente nel sistema.");
        }
        return EsitoDTO.successo("Elenco ordini.", ordini);
    }

    @Override
    public OrdineDTO dettaglioOrdine(Long ordineId) {
        Ordine ordine = gestore.dettaglio(ordineId);
        return ordine == null ? null : MappaOrdini.aDTOCompleto(ordine);
    }

    @Override
    public EsitoDTO<OrdineDTO> aggiornaStato(Long ordineId, StatoOrdineDTO nuovoStato) {
        try {
            Ordine ordine = gestore.aggiornaStato(ordineId, MappaOrdini.daStatoDTO(nuovoStato));
            String messaggio = nuovoStato == StatoOrdineDTO.ANNULLATO
                    ? "Ordine annullato con successo"
                    : "Stato dell'ordine aggiornato con successo.";
            return EsitoDTO.successo(messaggio, MappaOrdini.aDTOCompleto(ordine));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }
}
