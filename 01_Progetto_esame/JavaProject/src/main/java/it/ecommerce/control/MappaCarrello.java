package it.ecommerce.control;

import java.math.BigDecimal;
import java.util.List;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.RigaCarrelloDTO;
import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.RigaCarrello;

final class MappaCarrello {

    private MappaCarrello() {
    }

    static CarrelloDTO aDTO(Carrello carrello) {
        if (carrello == null) {
            return new CarrelloDTO(null, List.of(), BigDecimal.ZERO);
        }
        List<RigaCarrelloDTO> righe = carrello.getRighe().stream()
                .map(MappaCarrello::aRigaDTO)
                .toList();
        return new CarrelloDTO(carrello.getId(), righe, carrello.totale());
    }

    private static RigaCarrelloDTO aRigaDTO(RigaCarrello riga) {
        Prodotto prodotto = riga.getProdotto();
        return new RigaCarrelloDTO(
                riga.getId(),
                prodotto.getId(),
                prodotto.getNome(),
                prodotto.getPrezzoAttuale(),
                riga.getQuantita(),
                prodotto.getQuantitaMagazzino());
    }
}
