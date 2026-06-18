package it.ecommerce.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordine")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totaleComplessivo;

    @Column
    private String indirizzoSpedizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RigaOrdine> righe = new ArrayList<>();

    protected Ordine() {
    }

    public Ordine(Cliente cliente, String indirizzoSpedizione) {
        this.cliente = cliente;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.stato = StatoOrdine.INSERITO;
        this.dataCreazione = LocalDateTime.now();
        this.totaleComplessivo = BigDecimal.ZERO;
    }

    public void aggiungiRiga(Prodotto prodotto, int quantita, BigDecimal prezzoDiAcquisto) {
        righe.add(new RigaOrdine(this, prodotto, quantita, prezzoDiAcquisto));
        this.totaleComplessivo = righe.stream()
                .map(RigaOrdine::subtotale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cambiaStato(StatoOrdine nuovoStato) {
        this.stato = nuovoStato;
    }

    @PrePersist
    @PreUpdate
    public void assicuraNonVuoto() {
        if (righe.isEmpty()) {
            throw new EccezioneValidazione("Un ordine deve contenere almeno una riga.");
        }
    }

    public boolean appartieneA(Long clienteId) {
        return cliente.getId() != null && cliente.getId().equals(clienteId);
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public BigDecimal getTotaleComplessivo() {
        return totaleComplessivo;
    }

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public List<RigaOrdine> getRighe() {
        return righe;
    }
}
