package it.ecommerce.entity.persistenza;

import java.util.List;
import java.util.Optional;

import it.ecommerce.entity.ProdottoCatalogo;

public interface CatalogoRepository {

    ProdottoCatalogo salva(ProdottoCatalogo voce);

    void rimuovi(ProdottoCatalogo voce);

    Optional<ProdottoCatalogo> vocePerProdotto(Long prodottoId);

    List<ProdottoCatalogo> tutte();

    List<ProdottoCatalogo> inOfferta();

    List<ProdottoCatalogo> cerca(String termine);

    boolean esisteProdottoPerNome(String nome);

    long conta();
}
