package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.coordinatore.GestoreCatalogo;

public class CatalogoControllerImpl implements CatalogoController {

    private final GestoreCatalogo gestore;

    public CatalogoControllerImpl() {
        this(new GestoreCatalogo());
    }

    public CatalogoControllerImpl(GestoreCatalogo gestore) {
        this.gestore = gestore;
    }

    @Override
    public List<ProdottoDTO> consultaCatalogo() {
        return gestore.consultaCatalogo().stream().map(MappaProdotti::aDTO).toList();
    }

    @Override
    public EsitoDTO<List<ProdottoDTO>> visualizzaOfferte() {
        List<ProdottoDTO> offerte = gestore.offerte().stream().map(MappaProdotti::aDTO).toList();
        if (offerte.isEmpty()) {
            return EsitoDTO.errore("Nessuna offerta disponibile al momento.");
        }
        return EsitoDTO.successo("Offerte disponibili.", offerte);
    }

    @Override
    public EsitoDTO<List<ProdottoDTO>> cercaProdotto(String termine) {
        List<ProdottoDTO> risultati = gestore.cerca(termine).stream().map(MappaProdotti::aDTO).toList();
        if (risultati.isEmpty()) {
            return EsitoDTO.errore("Nessun prodotto corrispondente alla ricerca.");
        }
        return EsitoDTO.successo("Prodotti trovati.", risultati);
    }
}
