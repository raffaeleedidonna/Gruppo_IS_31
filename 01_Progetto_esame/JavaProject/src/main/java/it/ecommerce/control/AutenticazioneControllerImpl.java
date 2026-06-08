package it.ecommerce.control;

import it.ecommerce.control.dto.CredenzialiDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.RegistrazioneDTO;
import it.ecommerce.control.dto.UtenteDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreAutenticazione;

public class AutenticazioneControllerImpl implements AutenticazioneController {

    private final GestoreAutenticazione gestore;

    public AutenticazioneControllerImpl(GestoreAutenticazione gestore) {
        this.gestore = gestore;
    }

    @Override
    public EsitoDTO<UtenteDTO> autentica(CredenzialiDTO credenziali) {
        try {
            Utente utente = gestore.autentica(credenziali.email(), credenziali.password());
            return EsitoDTO.successo("Benvenuto " + utente.getProfilo().getNome() + "!", aDTO(utente));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public EsitoDTO<UtenteDTO> registra(RegistrazioneDTO dati) {
        try {
            Utente utente = gestore.registra(dati.nome(), dati.cognome(), dati.email(), dati.password(),
                    dati.indirizzoSpedizione(), dati.immagineProfilo());
            return EsitoDTO.successo("Registrazione completata. Ora puoi accedere.", aDTO(utente));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    private UtenteDTO aDTO(Utente utente) {
        return new UtenteDTO(utente.getId(), utente.getEmail(), utente.getProfilo().getNome(),
                utente.getProfilo().getCognome(), utente.ruolo());
    }
}
