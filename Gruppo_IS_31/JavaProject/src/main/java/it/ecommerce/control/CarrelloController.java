package it.ecommerce.control;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;

public interface CarrelloController {

    EsitoDTO<CarrelloDTO> aggiungiAlCarrello(Long clienteId, Long prodottoId, int quantita);

    EsitoDTO<CarrelloDTO> modificaQuantita(Long clienteId, Long rigaCarrelloId, int quantitaDesiderata);

    CarrelloDTO carrello(Long clienteId);
}
