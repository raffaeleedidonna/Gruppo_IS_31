package it.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profilo")
public class Profilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String datiAnagrafici;

    @Column
    private String indirizzoSpedizionePrincipale;

    @Column
    private String immagineProfilo;

    public Profilo() {
    }

    public Profilo(String datiAnagrafici, String indirizzoSpedizionePrincipale, String immagineProfilo) {
        this.datiAnagrafici = datiAnagrafici;
        this.indirizzoSpedizionePrincipale = indirizzoSpedizionePrincipale;
        this.immagineProfilo = immagineProfilo;
    }

    public void aggiorna(String datiAnagrafici, String indirizzoSpedizionePrincipale, String immagineProfilo) {
        this.datiAnagrafici = datiAnagrafici;
        this.indirizzoSpedizionePrincipale = indirizzoSpedizionePrincipale;
        this.immagineProfilo = immagineProfilo;
    }

    public Long getId() {
        return id;
    }

    public String getDatiAnagrafici() {
        return datiAnagrafici;
    }

    public String getIndirizzoSpedizionePrincipale() {
        return indirizzoSpedizionePrincipale;
    }

    public String getImmagineProfilo() {
        return immagineProfilo;
    }
}
