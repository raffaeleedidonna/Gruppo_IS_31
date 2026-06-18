package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.RigaOrdineDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;

final class MappaOrdini {

    private MappaOrdini() {
    }

    static OrdineDTO aDTOIntestazione(Ordine ordine) {
        return new OrdineDTO(
                ordine.getId(),
                ordine.getDataCreazione(),
                ordine.getTotaleComplessivo(),
                ordine.getIndirizzoSpedizione(),
                aStatoDTO(ordine.getStato()),
                List.of());
    }

    static OrdineDTO aDTOCompleto(Ordine ordine) {
        List<RigaOrdineDTO> righe = ordine.getRighe().stream()
                .map(MappaOrdini::aRigaDTO)
                .toList();
        return new OrdineDTO(
                ordine.getId(),
                ordine.getDataCreazione(),
                ordine.getTotaleComplessivo(),
                ordine.getIndirizzoSpedizione(),
                aStatoDTO(ordine.getStato()),
                righe);
    }

    static StatoOrdineDTO aStatoDTO(StatoOrdine stato) {
        return StatoOrdineDTO.valueOf(stato.name());
    }

    static StatoOrdine daStatoDTO(StatoOrdineDTO stato) {
        return StatoOrdine.valueOf(stato.name());
    }

    private static RigaOrdineDTO aRigaDTO(RigaOrdine riga) {
        Prodotto prodotto = riga.getProdotto();
        return new RigaOrdineDTO(
                riga.getId(),
                prodotto.getId(),
                prodotto.getNome(),
                riga.getQuantitaAcquistata(),
                riga.getPrezzoDiAcquisto());
    }
}
