package it.ecommerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
@Table(name = "utente")
public abstract class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profilo_id")
    private Profilo profilo;

    protected Utente() {
    }

    protected Utente(String email, String passwordInChiaro, String nome, String cognome, Profilo profilo) {
        this.email = email;
        this.passwordHash = Password.hash(passwordInChiaro);
        this.nome = nome;
        this.cognome = cognome;
        this.profilo = profilo;
    }

    public boolean passwordCorrisponde(String passwordInChiaro) {
        return passwordHash.equals(Password.hash(passwordInChiaro));
    }

    public abstract String ruolo();

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Profilo getProfilo() {
        return profilo;
    }
}
