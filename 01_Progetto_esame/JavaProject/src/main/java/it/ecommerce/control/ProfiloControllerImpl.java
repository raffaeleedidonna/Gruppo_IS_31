package it.ecommerce.control;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreProfilo;

public class ProfiloControllerImpl implements ProfiloController {

    private final GestoreProfilo gestore;

    public ProfiloControllerImpl(GestoreProfilo gestore) {
        this.gestore = gestore;
    }

    @Override
    public ProfiloDTO mostraProfilo(Long utenteId) {
        return aDTO(gestore.mostra(utenteId));
    }

    @Override
    public EsitoDTO<ProfiloDTO> aggiornaProfilo(Long utenteId, ProfiloDTO modifiche) {
        try {
            Utente utente = gestore.aggiorna(utenteId, modifiche.nome(), modifiche.cognome(),
                    modifiche.indirizzoSpedizionePrincipale(), modifiche.immagineProfilo());
            return EsitoDTO.successo("Profilo aggiornato con successo.", aDTO(utente));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    private ProfiloDTO aDTO(Utente utente) {
        Profilo profilo = utente.getProfilo();
        if (profilo == null) {
            return new ProfiloDTO(null, null, utente.getEmail(), null, null);
        }
        return new ProfiloDTO(profilo.getNome(), profilo.getCognome(), utente.getEmail(),
                profilo.getIndirizzoSpedizionePrincipale(), profilo.getImmagineProfilo());
    }
}
