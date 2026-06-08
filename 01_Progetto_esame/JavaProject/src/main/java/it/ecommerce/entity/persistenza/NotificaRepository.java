package it.ecommerce.entity.persistenza;

import java.util.List;

import it.ecommerce.entity.Notifica;

public interface NotificaRepository {

    Notifica salva(Notifica notifica);

    List<Notifica> perCliente(Long clienteId);
}
