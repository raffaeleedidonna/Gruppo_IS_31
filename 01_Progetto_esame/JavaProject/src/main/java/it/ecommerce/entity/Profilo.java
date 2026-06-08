package it.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "profilo")
public class Profilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column
    private String indirizzoSpedizionePrincipale;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String immagineProfilo;

    protected Profilo() {
    }

    public Profilo(String nome, String cognome, String indirizzoSpedizionePrincipale, String immagineProfilo) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzoSpedizionePrincipale = indirizzoSpedizionePrincipale;
        this.immagineProfilo = immagineProfilo;
    }

    public void aggiorna(String nome, String cognome, String indirizzoSpedizionePrincipale,
                         String immagineProfilo) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzoSpedizionePrincipale = indirizzoSpedizionePrincipale;
        this.immagineProfilo = immagineProfilo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getIndirizzoSpedizionePrincipale() {
        return indirizzoSpedizionePrincipale;
    }

    public String getImmagineProfilo() {
        return immagineProfilo;
    }
}
