package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;

public interface OrdineClienteController {

    EsitoDTO<OrdineDTO> confermaOrdine(Long clienteId, String indirizzoSpedizione);

    EsitoDTO<List<OrdineDTO>> storicoOrdini(Long clienteId);

    OrdineDTO dettaglioOrdine(Long clienteId, Long ordineId);
}
