package it.ecommerce.control.dto;

import java.math.BigDecimal;

public record RigaCarrelloDTO(
        Long id,
        Long prodottoId,
        String prodottoNome,
        BigDecimal prezzoAttuale,
        int quantita,
        int quantitaDisponibile) {
}
