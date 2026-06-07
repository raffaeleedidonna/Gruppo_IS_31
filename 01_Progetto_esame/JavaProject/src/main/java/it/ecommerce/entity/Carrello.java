package it.ecommerce.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrello")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RigaCarrello> righe = new ArrayList<>();

    public Carrello() {
    }

    public void aggiungi(Prodotto prodotto, int quantita) {
        RigaCarrello esistente = rigaPerProdotto(prodotto);
        if (esistente != null) {
            esistente.aumenta(quantita);
        } else {
            righe.add(new RigaCarrello(this, prodotto, quantita));
        }
    }

    public int quantitaProdotto(Prodotto prodotto) {
        RigaCarrello riga = rigaPerProdotto(prodotto);
        return riga == null ? 0 : riga.getQuantita();
    }

    public RigaCarrello rigaPerId(Long rigaId) {
        return righe.stream()
                .filter(riga -> riga.getId() != null && riga.getId().equals(rigaId))
                .findFirst()
                .orElse(null);
    }

    public void rimuoviRiga(RigaCarrello riga) {
        righe.remove(riga);
    }

    public void svuota() {
        righe.clear();
    }

    public boolean isVuoto() {
        return righe.isEmpty();
    }

    public BigDecimal totale() {
        return righe.stream()
                .map(RigaCarrello::subtotale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public List<RigaCarrello> getRighe() {
        return righe;
    }

    private RigaCarrello rigaPerProdotto(Prodotto prodotto) {
        return righe.stream()
                .filter(riga -> riga.getProdotto().getId().equals(prodotto.getId()))
                .findFirst()
                .orElse(null);
    }
}
