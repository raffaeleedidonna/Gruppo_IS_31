package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.OrdineRepository;
import it.ecommerce.entity.persistenza.UtenteRepository;

public class GestoreMonitoraggio {

    private static final int LIMITE_PIU_VENDUTI = 5;

    private final GestoreTransazioni transazioni;
    private final OrdineRepository ordini;
    private final CatalogoRepository catalogo;
    private final UtenteRepository utenti;

    public GestoreMonitoraggio(GestoreTransazioni transazioni, OrdineRepository ordini,
                               CatalogoRepository catalogo, UtenteRepository utenti) {
        this.transazioni = transazioni;
        this.ordini = ordini;
        this.catalogo = catalogo;
        this.utenti = utenti;
    }

    public StatistichePiattaforma calcola() {
        return transazioni.inTransazione(() -> {
            List<Ordine> elenco = ordini.tutti();
            long numeroProdotti = catalogo.conta();
            long numeroClienti = utenti.contaClienti();

            BigDecimal fatturato = elenco.stream()
                    .filter(ordine -> ordine.getStato() != StatoOrdine.ANNULLATO)
                    .map(Ordine::getTotaleComplessivo)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<StatoOrdine, Long> ordiniPerStato = elenco.stream()
                    .collect(Collectors.groupingBy(Ordine::getStato, Collectors.counting()));

            return new StatistichePiattaforma(
                    elenco.size(),
                    numeroProdotti,
                    numeroClienti,
                    fatturato,
                    ordiniPerStato,
                    prodottiPiuVenduti(elenco));
        });
    }

    private List<ProdottoCatalogo> prodottiPiuVenduti(List<Ordine> elenco) {
        Map<Prodotto, Integer> quantitaVendute = new LinkedHashMap<>();
        for (Ordine ordine : elenco) {
            if (ordine.getStato() == StatoOrdine.ANNULLATO) {
                continue;
            }
            for (RigaOrdine riga : ordine.getRighe()) {
                quantitaVendute.merge(riga.getProdotto(), riga.getQuantitaAcquistata(), Integer::sum);
            }
        }
        List<ProdottoCatalogo> piuVenduti = new ArrayList<>();
        quantitaVendute.entrySet().stream()
                .sorted((primo, secondo) -> Integer.compare(secondo.getValue(), primo.getValue()))
                .forEach(voce -> catalogo.vocePerProdotto(voce.getKey().getId())
                        .ifPresent(piuVenduti::add));
        return piuVenduti.stream().limit(LIMITE_PIU_VENDUTI).toList();
    }
}
