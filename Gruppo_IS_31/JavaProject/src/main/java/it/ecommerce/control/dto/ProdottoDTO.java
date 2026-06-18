package it.ecommerce.control.dto;

import java.math.BigDecimal;

public record ProdottoDTO(
        Long id,
        String nome,
        String descrizione,
        BigDecimal prezzoAttuale,
        int quantitaMagazzino,
        boolean disponibile,
        boolean inOfferta,
        Long categoriaId,
        String categoriaNome) {
}
