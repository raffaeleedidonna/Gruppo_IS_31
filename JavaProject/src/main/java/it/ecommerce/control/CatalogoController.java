package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;

public interface CatalogoController {

    List<ProdottoDTO> consultaCatalogo();

    EsitoDTO<List<ProdottoDTO>> visualizzaOfferte();

    EsitoDTO<List<ProdottoDTO>> cercaProdotto(String termine);
}
