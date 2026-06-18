package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.List;

import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.CategoriaRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;

public class GestoreProdotti {

    private final GestoreTransazioni transazioni;
    private final CatalogoRepository catalogo;
    private final CategoriaRepository categorie;

    public GestoreProdotti(GestoreTransazioni transazioni, CatalogoRepository catalogo,
                           CategoriaRepository categorie) {
        this.transazioni = transazioni;
        this.catalogo = catalogo;
        this.categorie = categorie;
    }

    public ProdottoCatalogo aggiungi(String nome, String descrizione, BigDecimal prezzoAttuale,
                                     int quantitaMagazzino, boolean disponibile, boolean inOfferta,
                                     Long categoriaId) {
        return transazioni.inTransazione(() -> {
            if (catalogo.esisteProdottoPerNome(nome)) {
                throw new EccezioneValidazione("Esiste già un prodotto con questo nome.");
            }
            validaQuantita(quantitaMagazzino);
            Prodotto prodotto = new Prodotto(nome, descrizione, categoriaValida(categoriaId));
            ProdottoCatalogo voce = new ProdottoCatalogo(prodotto, prezzoAttuale,
                    quantitaMagazzino, disponibile, inOfferta);
            return catalogo.salva(voce);
        });
    }

    public ProdottoCatalogo modifica(Long prodottoId, String nome, String descrizione, BigDecimal prezzoAttuale,
                                     int quantitaMagazzino, boolean disponibile, boolean inOfferta,
                                     Long categoriaId) {
        return transazioni.inTransazione(() -> {
            ProdottoCatalogo voce = catalogo.vocePerProdotto(prodottoId)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            validaQuantita(quantitaMagazzino);
            voce.getProdotto().aggiorna(nome, descrizione, categoriaValida(categoriaId));
            voce.aggiorna(prezzoAttuale, quantitaMagazzino, disponibile, inOfferta);
            return catalogo.salva(voce);
        });
    }

    public void rimuoviDalCatalogo(Long prodottoId) {
        transazioni.inTransazione(() -> {
            ProdottoCatalogo voce = catalogo.vocePerProdotto(prodottoId)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            catalogo.rimuovi(voce);
            return null;
        });
    }

    public ProdottoCatalogo dettaglio(Long prodottoId) {
        return transazioni.inTransazione(() -> catalogo.vocePerProdotto(prodottoId).orElse(null));
    }

    public List<ProdottoCatalogo> tuttiNelCatalogo() {
        return transazioni.inTransazione(catalogo::tutte);
    }

    public List<Categoria> categorie() {
        return transazioni.inTransazione(categorie::tutte);
    }

    private Categoria categoriaValida(Long categoriaId) {
        if (categoriaId == null) {
            throw new EccezioneValidazione("Categoria non valida.");
        }
        return categorie.perId(categoriaId)
                .orElseThrow(() -> new EccezioneValidazione("Categoria non valida."));
    }

    private void validaQuantita(int quantita) {
        if (quantita < 0) {
            throw new EccezioneValidazione("La quantità non può essere negativa.");
        }
    }
}
