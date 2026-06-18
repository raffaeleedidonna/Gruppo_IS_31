package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.CategoriaDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;

public interface GestioneProdottiController {

    List<ProdottoDTO> elencoProdotti();

    EsitoDTO<ProdottoDTO> aggiungiProdotto(ProdottoDTO nuovo);

    ProdottoDTO dettaglioProdotto(Long prodottoId);

    EsitoDTO<ProdottoDTO> modificaProdotto(ProdottoDTO modificato);

    EsitoDTO<Void> rimuoviDalCatalogo(Long prodottoId);

    List<CategoriaDTO> categorie();
}
