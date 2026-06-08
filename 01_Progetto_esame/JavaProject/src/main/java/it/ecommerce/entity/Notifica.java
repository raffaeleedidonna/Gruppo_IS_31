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

    @Column(nullable = false)
    private boolean letta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ordine_id")
    private Ordine ordine;

    protected Notifica() {
    }

    public Notifica(Cliente cliente, String messaggio, Ordine ordine) {
        this.cliente = cliente;
        this.messaggio = messaggio;
        this.ordine = ordine;
        this.dataCreazione = LocalDateTime.now();
        this.letta = false;
    }

    public void segnaLetta() {
        this.letta = true;
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

    public boolean isLetta() {
        return letta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Ordine getOrdine() {
        return ordine;
    }
}
