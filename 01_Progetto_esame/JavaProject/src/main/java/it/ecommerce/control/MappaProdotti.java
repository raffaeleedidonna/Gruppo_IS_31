package it.ecommerce.control;

import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;

final class MappaProdotti {

    private MappaProdotti() {
    }

    static ProdottoDTO aDTO(ProdottoCatalogo voce) {
        Prodotto prodotto = voce.getProdotto();
        Categoria categoria = prodotto.getCategoria();
        return new ProdottoDTO(
                prodotto.getId(),
                prodotto.getNome(),
                prodotto.getDescrizione(),
                voce.getPrezzoAttuale(),
                voce.getQuantitaMagazzino(),
                voce.isDisponibile(),
                voce.isInOfferta(),
                categoria.getId(),
                categoria.getNome());
    }
}
