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
@Table(name = "riga_carrello")
public class RigaCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carrello_id", nullable = false)
    private Carrello carrello;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    protected RigaCarrello() {
    }

    public RigaCarrello(Carrello carrello, Prodotto prodotto, int quantita) {
        this.carrello = carrello;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public void aumenta(int quantita) {
        this.quantita += quantita;
    }

    public void impostaQuantita(int quantita) {
        this.quantita = quantita;
    }

    public BigDecimal subtotale() {
        return prodotto.getPrezzoAttuale().multiply(BigDecimal.valueOf(quantita));
    }

    public Long getId() {
        return id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantita() {
        return quantita;
    }
}
