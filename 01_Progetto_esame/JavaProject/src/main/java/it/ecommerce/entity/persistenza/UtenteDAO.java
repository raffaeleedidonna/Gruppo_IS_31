package it.ecommerce.entity.persistenza;

import java.util.Optional;

import it.ecommerce.entity.Utente;

public interface UtenteDAO {

    Utente salva(Utente utente);

    Optional<Utente> perEmail(String email);

    Optional<Utente> perId(Long id);

    boolean esisteEmail(String email);

    long contaClienti();
}
