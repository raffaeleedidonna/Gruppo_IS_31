package it.ecommerce.control;

import it.ecommerce.control.dto.CredenzialiDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.RegistrazioneDTO;
import it.ecommerce.control.dto.UtenteDTO;

public interface AutenticazioneController {

    EsitoDTO<UtenteDTO> autentica(CredenzialiDTO credenziali);

    EsitoDTO<UtenteDTO> registra(RegistrazioneDTO dati);
}
