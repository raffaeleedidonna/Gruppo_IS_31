package it.ecommerce.entity.persistenza;

import java.util.List;
import java.util.Optional;

import it.ecommerce.entity.Prodotto;

public interface ProdottoDAO {

    Prodotto salva(Prodotto prodotto);

    Optional<Prodotto> perId(Long id);

    List<Prodotto> tuttiNelCatalogo();

    List<Prodotto> inOfferta();

    List<Prodotto> cerca(String termine);

    boolean esistePerNome(String nome);

    List<Prodotto> perCategoria(Long categoriaId);
}
