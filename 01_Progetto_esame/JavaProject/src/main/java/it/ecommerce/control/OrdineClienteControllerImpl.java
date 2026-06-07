package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.coordinatore.EsitoConfermaOrdine;
import it.ecommerce.entity.coordinatore.GestoreOrdiniCliente;

public class OrdineClienteControllerImpl implements OrdineClienteController {

    private final GestoreOrdiniCliente gestore;

    public OrdineClienteControllerImpl() {
        this(new GestoreOrdiniCliente());
    }

    public OrdineClienteControllerImpl(GestoreOrdiniCliente gestore) {
        this.gestore = gestore;
    }

    @Override
    public EsitoDTO<OrdineDTO> confermaOrdine(Long clienteId, String indirizzoSpedizione) {
        try {
            EsitoConfermaOrdine esito = gestore.confermaOrdine(clienteId, indirizzoSpedizione);
            if (esito.confermato()) {
                return EsitoDTO.successo(esito.messaggio(), MappaOrdini.aDTOCompleto(esito.ordine()));
            }
            return EsitoDTO.errore(esito.messaggio());
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public EsitoDTO<List<OrdineDTO>> storicoOrdini(Long clienteId) {
        try {
            List<OrdineDTO> ordini = gestore.storicoOrdini(clienteId).stream()
                    .map(MappaOrdini::aDTOIntestazione)
                    .toList();
            if (ordini.isEmpty()) {
                return EsitoDTO.errore("Non hai ancora effettuato ordini.");
            }
            return EsitoDTO.successo("Storico ordini.", ordini);
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public OrdineDTO dettaglioOrdine(Long clienteId, Long ordineId) {
        Ordine ordine = gestore.dettaglioOrdine(clienteId, ordineId);
        return ordine == null ? null : MappaOrdini.aDTOCompleto(ordine);
    }
}
