package it.ecommerce.entity.persistenza;

import java.util.List;

import it.ecommerce.entity.Notifica;

public interface NotificaDAO {

    Notifica salva(Notifica notifica);

    List<Notifica> perCliente(Long clienteId);
}
