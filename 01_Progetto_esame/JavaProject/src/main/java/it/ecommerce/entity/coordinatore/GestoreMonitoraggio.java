package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreMonitoraggio {

    private static final int LIMITE_PIU_VENDUTI = 5;

    private final FornitorePersistenza fornitore;

    public GestoreMonitoraggio() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreMonitoraggio(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public StatistichePiattaforma calcola() {
        return fornitore.inTransazione(() -> {
            List<Ordine> ordini = fornitore.ordineDAO().tutti();
            long numeroProdotti = fornitore.prodottoDAO().tuttiNelCatalogo().size();
            long numeroClienti = fornitore.utenteDAO().contaClienti();

            BigDecimal fatturato = ordini.stream()
                    .filter(ordine -> ordine.getStato() != StatoOrdine.ANNULLATO)
                    .map(Ordine::getTotaleComplessivo)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<StatoOrdine, Long> ordiniPerStato = ordini.stream()
                    .collect(Collectors.groupingBy(Ordine::getStato, Collectors.counting()));

            return new StatistichePiattaforma(
                    ordini.size(),
                    numeroProdotti,
                    numeroClienti,
                    fatturato,
                    ordiniPerStato,
                    prodottiPiuVenduti(ordini));
        });
    }

    private List<Prodotto> prodottiPiuVenduti(List<Ordine> ordini) {
        Map<Prodotto, Integer> quantitaVendute = new LinkedHashMap<>();
        for (Ordine ordine : ordini) {
            if (ordine.getStato() == StatoOrdine.ANNULLATO) {
                continue;
            }
            for (RigaOrdine riga : ordine.getRighe()) {
                quantitaVendute.merge(riga.getProdotto(), riga.getQuantitaAcquistata(), Integer::sum);
            }
        }
        return quantitaVendute.entrySet().stream()
                .sorted((primo, secondo) -> Integer.compare(secondo.getValue(), primo.getValue()))
                .limit(LIMITE_PIU_VENDUTI)
                .map(Map.Entry::getKey)
                .toList();
    }
}
