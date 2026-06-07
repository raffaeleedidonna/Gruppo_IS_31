package it.ecommerce.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AMMINISTRATORE")
public class Amministratore extends Utente {

    protected Amministratore() {
    }

    public Amministratore(String email, String passwordInChiaro, String nome, String cognome, Profilo profilo) {
        super(email, passwordInChiaro, nome, cognome, profilo);
    }

    @Override
    public String ruolo() {
        return "AMMINISTRATORE";
    }
}
