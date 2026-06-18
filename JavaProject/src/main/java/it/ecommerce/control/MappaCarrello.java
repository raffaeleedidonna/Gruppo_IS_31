package it.ecommerce.control;

import java.math.BigDecimal;
import java.util.List;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.RigaCarrelloDTO;
import it.ecommerce.entity.coordinatore.VistaCarrello;
import it.ecommerce.entity.coordinatore.VistaRigaCarrello;

final class MappaCarrello {

    private MappaCarrello() {
    }

    static CarrelloDTO aDTO(VistaCarrello carrello) {
        if (carrello == null) {
            return new CarrelloDTO(null, List.of(), BigDecimal.ZERO);
        }
        List<RigaCarrelloDTO> righe = carrello.righe().stream()
                .map(MappaCarrello::aRigaDTO)
                .toList();
        return new CarrelloDTO(carrello.carrelloId(), righe, carrello.totale());
    }

    private static RigaCarrelloDTO aRigaDTO(VistaRigaCarrello riga) {
        return new RigaCarrelloDTO(
                riga.rigaId(),
                riga.prodottoId(),
                riga.nome(),
                riga.prezzoAttuale(),
                riga.quantita(),
                riga.quantitaDisponibile());
    }
}
