package it.ecommerce.control;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.control.dto.StatistichePiattaformaDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;
import it.ecommerce.entity.coordinatore.GestoreMonitoraggio;
import it.ecommerce.entity.coordinatore.StatistichePiattaforma;

public class MonitoraggioControllerImpl implements MonitoraggioController {

    private final GestoreMonitoraggio gestore;

    public MonitoraggioControllerImpl() {
        this(new GestoreMonitoraggio());
    }

    public MonitoraggioControllerImpl(GestoreMonitoraggio gestore) {
        this.gestore = gestore;
    }

    @Override
    public StatistichePiattaformaDTO statistiche() {
        StatistichePiattaforma statistiche = gestore.calcola();
        Map<StatoOrdineDTO, Long> ordiniPerStato = new LinkedHashMap<>();
        statistiche.ordiniPerStato().forEach((stato, conteggio) ->
                ordiniPerStato.put(MappaOrdini.aStatoDTO(stato), conteggio));
        List<ProdottoDTO> prodottiPiuVenduti = statistiche.prodottiPiuVenduti().stream()
                .map(MappaProdotti::aDTO)
                .toList();
        return new StatistichePiattaformaDTO(
                statistiche.numeroOrdini(),
                statistiche.numeroProdotti(),
                statistiche.numeroClienti(),
                statistiche.fatturatoTotale(),
                ordiniPerStato,
                prodottiPiuVenduti);
    }
}
