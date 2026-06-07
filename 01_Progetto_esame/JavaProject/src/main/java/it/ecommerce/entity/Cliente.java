package it.ecommerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Utente {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    protected Cliente() {
    }

    public Cliente(String email, String passwordInChiaro, String nome, String cognome, Profilo profilo) {
        super(email, passwordInChiaro, nome, cognome, profilo);
    }

    @Override
    public String ruolo() {
        return "CLIENTE";
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public Carrello carrelloCorrente() {
        if (carrello == null) {
            carrello = new Carrello();
        }
        return carrello;
    }
}
