package it.ecommerce.entity.persistenza;

import java.util.List;
import java.util.Optional;

import it.ecommerce.entity.Categoria;

public interface CategoriaRepository {

    Categoria salva(Categoria categoria);

    Optional<Categoria> perId(Long id);

    Optional<Categoria> perNome(String nome);

    List<Categoria> tutte();
}
