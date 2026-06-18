package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.StatoOrdine;

public record StatistichePiattaforma(
        long numeroOrdini,
        long numeroProdotti,
        long numeroClienti,
        BigDecimal fatturatoTotale,
        Map<StatoOrdine, Long> ordiniPerStato,
        List<ProdottoCatalogo> prodottiPiuVenduti) {
}
