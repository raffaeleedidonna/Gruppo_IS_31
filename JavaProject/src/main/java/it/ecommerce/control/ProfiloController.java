package it.ecommerce.control;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;

public interface ProfiloController {

    ProfiloDTO mostraProfilo(Long utenteId);

    EsitoDTO<ProfiloDTO> aggiornaProfilo(Long utenteId, ProfiloDTO modifiche);
}
