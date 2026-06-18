package it.ecommerce.control;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.coordinatore.GestoreCarrello;
import it.ecommerce.entity.coordinatore.VistaCarrello;

public class CarrelloControllerImpl implements CarrelloController {

    private final GestoreCarrello gestore;

    public CarrelloControllerImpl(GestoreCarrello gestore) {
        this.gestore = gestore;
    }

    @Override
    public EsitoDTO<CarrelloDTO> aggiungiAlCarrello(Long clienteId, Long prodottoId, int quantita) {
        try {
            VistaCarrello carrello = gestore.aggiungiAlCarrello(clienteId, prodottoId, quantita);
            return EsitoDTO.successo("Prodotto aggiunto al carrello.", MappaCarrello.aDTO(carrello));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public EsitoDTO<CarrelloDTO> modificaQuantita(Long clienteId, Long rigaCarrelloId, int quantitaDesiderata) {
        try {
            VistaCarrello carrello = gestore.modificaQuantita(clienteId, rigaCarrelloId, quantitaDesiderata);
            return EsitoDTO.successo("Carrello aggiornato.", MappaCarrello.aDTO(carrello));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public CarrelloDTO carrello(Long clienteId) {
        return MappaCarrello.aDTO(gestore.carrello(clienteId));
    }
}
