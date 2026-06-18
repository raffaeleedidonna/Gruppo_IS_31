package it.ecommerce.entity;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prodotto_catalogo")
public class ProdottoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "prodotto_id", nullable = false, unique = true)
    private Prodotto prodotto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoAttuale;

    @Column(nullable = false)
    private int quantitaMagazzino;

    @Column(nullable = false)
    private boolean disponibile;

    @Column(nullable = false)
    private boolean inOfferta;

    protected ProdottoCatalogo() {
    }

    public ProdottoCatalogo(Prodotto prodotto, BigDecimal prezzoAttuale, int quantitaMagazzino,
                            boolean disponibile, boolean inOfferta) {
        this.prodotto = prodotto;
        this.prezzoAttuale = prezzoAttuale;
        this.quantitaMagazzino = quantitaMagazzino;
        this.disponibile = disponibile;
        this.inOfferta = inOfferta;
    }

    public void aggiorna(BigDecimal prezzoAttuale, int quantitaMagazzino, boolean disponibile, boolean inOfferta) {
        this.prezzoAttuale = prezzoAttuale;
        this.quantitaMagazzino = quantitaMagazzino;
        this.disponibile = disponibile;
        this.inOfferta = inOfferta;
    }

    public boolean disponibilitaSufficiente(int quantitaRichiesta) {
        return disponibile && quantitaMagazzino >= quantitaRichiesta;
    }

    public void decrementaMagazzino(int quantita) {
        this.quantitaMagazzino -= quantita;
    }

    public void incrementaMagazzino(int quantita) {
        this.quantitaMagazzino += quantita;
    }

    public Long getId() {
        return id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public BigDecimal getPrezzoAttuale() {
        return prezzoAttuale;
    }

    public int getQuantitaMagazzino() {
        return quantitaMagazzino;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public boolean isInOfferta() {
        return inOfferta;
    }
}
