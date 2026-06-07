package it.ecommerce.control;

import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Prodotto;

final class MappaProdotti {

    private MappaProdotti() {
    }

    static ProdottoDTO aDTO(Prodotto prodotto) {
        Categoria categoria = prodotto.getCategoria();
        return new ProdottoDTO(
                prodotto.getId(),
                prodotto.getNome(),
                prodotto.getDescrizione(),
                prodotto.getPrezzoAttuale(),
                prodotto.getQuantitaMagazzino(),
                prodotto.isDisponibile(),
                prodotto.isInOfferta(),
                categoria.getId(),
                categoria.getNome());
    }
}
