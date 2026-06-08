package it.ecommerce.entity.persistenza;

import java.util.List;
import java.util.Optional;

import it.ecommerce.entity.Ordine;

public interface OrdineRepository {

    Ordine salva(Ordine ordine);

    Optional<Ordine> perId(Long id);

    List<Ordine> tutti();

    List<Ordine> perCliente(Long clienteId);
}
