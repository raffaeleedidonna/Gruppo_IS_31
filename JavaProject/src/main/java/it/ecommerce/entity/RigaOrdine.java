package it.ecommerce.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "riga_ordine")
public class RigaOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantitaAcquistata;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoDiAcquisto;

    protected RigaOrdine() {
    }

    public RigaOrdine(Ordine ordine, Prodotto prodotto, int quantitaAcquistata, BigDecimal prezzoDiAcquisto) {
        this.ordine = ordine;
        this.prodotto = prodotto;
        this.quantitaAcquistata = quantitaAcquistata;
        this.prezzoDiAcquisto = prezzoDiAcquisto;
    }

    public BigDecimal subtotale() {
        return prezzoDiAcquisto.multiply(BigDecimal.valueOf(quantitaAcquistata));
    }

    public Long getId() {
        return id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantitaAcquistata() {
        return quantitaAcquistata;
    }

    public BigDecimal getPrezzoDiAcquisto() {
        return prezzoDiAcquisto;
    }
}
