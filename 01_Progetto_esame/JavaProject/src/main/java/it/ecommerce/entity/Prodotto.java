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
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 2000)
    private String descrizione;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoAttuale;

    @Column(nullable = false)
    private int quantitaMagazzino;

    @Column(nullable = false)
    private boolean disponibile;

    @Column(nullable = false)
    private boolean inOfferta;

    @Column(nullable = false)
    private boolean presenteNelCatalogo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    protected Prodotto() {
    }

    public Prodotto(String nome, String descrizione, BigDecimal prezzoAttuale,
                    int quantitaMagazzino, boolean disponibile, boolean inOfferta) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzoAttuale = prezzoAttuale;
        this.quantitaMagazzino = quantitaMagazzino;
        this.disponibile = disponibile;
        this.inOfferta = inOfferta;
        this.presenteNelCatalogo = true;
    }

    public void aggiorna(String nome, String descrizione, BigDecimal prezzoAttuale,
                         int quantitaMagazzino, boolean disponibile, boolean inOfferta,
                         Categoria categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzoAttuale = prezzoAttuale;
        this.quantitaMagazzino = quantitaMagazzino;
        this.disponibile = disponibile;
        this.inOfferta = inOfferta;
        this.categoria = categoria;
    }

    public void rimuoviDalCatalogo() {
        this.presenteNelCatalogo = false;
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

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
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

    public boolean isPresenteNelCatalogo() {
        return presenteNelCatalogo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
