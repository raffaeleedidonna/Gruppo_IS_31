package it.ecommerce.control;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.coordinatore.GestoreProfilo;

public class ProfiloControllerImpl implements ProfiloController {

    private final GestoreProfilo gestore;

    public ProfiloControllerImpl() {
        this(new GestoreProfilo());
    }

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
            Profilo profilo = gestore.aggiorna(utenteId, modifiche.datiAnagrafici(),
                    modifiche.indirizzoSpedizionePrincipale(), modifiche.immagineProfilo());
            return EsitoDTO.successo("Profilo aggiornato con successo.", aDTO(profilo));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    private ProfiloDTO aDTO(Profilo profilo) {
        if (profilo == null) {
            return new ProfiloDTO(null, null, null);
        }
        return new ProfiloDTO(profilo.getDatiAnagrafici(), profilo.getIndirizzoSpedizionePrincipale(),
                profilo.getImmagineProfilo());
    }
}
