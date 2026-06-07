package it.ecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifica")
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String messaggio;

    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Notifica() {
    }

    public Notifica(Cliente cliente, String messaggio) {
        this.cliente = cliente;
        this.messaggio = messaggio;
        this.dataCreazione = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
}
