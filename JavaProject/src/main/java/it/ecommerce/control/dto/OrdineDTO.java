package it.ecommerce.control.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdineDTO(
        Long id,
        LocalDateTime dataCreazione,
        BigDecimal totaleComplessivo,
        String indirizzoSpedizione,
        StatoOrdineDTO stato,
        List<RigaOrdineDTO> righe) {
}
