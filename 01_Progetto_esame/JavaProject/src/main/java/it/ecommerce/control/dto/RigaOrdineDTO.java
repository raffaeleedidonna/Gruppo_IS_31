package it.ecommerce.control.dto;

import java.math.BigDecimal;

public record RigaOrdineDTO(
        Long id,
        Long prodottoId,
        String prodottoNome,
        int quantitaAcquistata,
        BigDecimal prezzoDiAcquisto) {
}
