package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;

public record VistaRigaCarrello(
        Long rigaId,
        Long prodottoId,
        String nome,
        BigDecimal prezzoAttuale,
        int quantita,
        int quantitaDisponibile) {
}
