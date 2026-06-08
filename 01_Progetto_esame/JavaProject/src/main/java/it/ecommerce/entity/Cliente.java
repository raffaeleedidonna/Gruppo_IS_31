package it.ecommerce.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Utente {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    @OneToMany(mappedBy = "cliente")
    private List<Ordine> ordini = new ArrayList<>();

    protected Cliente() {
    }

    public Cliente(String email, String passwordInChiaro, Profilo profilo) {
        super(email, passwordInChiaro, profilo);
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

    public List<Ordine> getOrdini() {
        return ordini;
    }
}
