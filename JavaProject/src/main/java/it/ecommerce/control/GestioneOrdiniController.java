package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;

public interface GestioneOrdiniController {

    EsitoDTO<List<OrdineDTO>> elencoOrdini();

    OrdineDTO dettaglioOrdine(Long ordineId);

    EsitoDTO<OrdineDTO> aggiornaStato(Long ordineId, StatoOrdineDTO nuovoStato);
}
