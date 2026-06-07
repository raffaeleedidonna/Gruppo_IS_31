package it.ecommerce.entity.coordinatore;

import java.util.List;

import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreProdotti {

    private final FornitorePersistenza fornitore;

    public GestoreProdotti() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreProdotti(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public Prodotto aggiungi(Prodotto nuovo, Long categoriaId) {
        return fornitore.inTransazione(() -> {
            ProdottoDAO prodotti = fornitore.prodottoDAO();
            if (prodotti.esistePerNome(nuovo.getNome())) {
                throw new EccezioneValidazione("Esiste già un prodotto con questo nome.");
            }
            validaQuantita(nuovo.getQuantitaMagazzino());
            nuovo.setCategoria(categoriaValida(categoriaId));
            return prodotti.salva(nuovo);
        });
    }

    public Prodotto modifica(Long id, Prodotto modifiche, Long categoriaId) {
        return fornitore.inTransazione(() -> {
            ProdottoDAO prodotti = fornitore.prodottoDAO();
            Prodotto esistente = prodotti.perId(id)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            validaQuantita(modifiche.getQuantitaMagazzino());
            esistente.aggiorna(
                    modifiche.getNome(),
                    modifiche.getDescrizione(),
                    modifiche.getPrezzoAttuale(),
                    modifiche.getQuantitaMagazzino(),
                    modifiche.isDisponibile(),
                    modifiche.isInOfferta(),
                    categoriaValida(categoriaId));
            return prodotti.salva(esistente);
        });
    }

    public void rimuoviDalCatalogo(Long id) {
        fornitore.inTransazione(() -> {
            ProdottoDAO prodotti = fornitore.prodottoDAO();
            Prodotto esistente = prodotti.perId(id)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            esistente.rimuoviDalCatalogo();
            prodotti.salva(esistente);
            return null;
        });
    }

    public Prodotto dettaglio(Long id) {
        return fornitore.inTransazione(() -> fornitore.prodottoDAO().perId(id).orElse(null));
    }

    public List<Prodotto> tuttiNelCatalogo() {
        return fornitore.inTransazione(() -> fornitore.prodottoDAO().tuttiNelCatalogo());
    }

    public List<Categoria> categorie() {
        return fornitore.inTransazione(() -> fornitore.categoriaDAO().tutte());
    }

    private Categoria categoriaValida(Long categoriaId) {
        if (categoriaId == null) {
            throw new EccezioneValidazione("Categoria non valida.");
        }
        return fornitore.categoriaDAO().perId(categoriaId)
                .orElseThrow(() -> new EccezioneValidazione("Categoria non valida."));
    }

    private void validaQuantita(int quantita) {
        if (quantita < 0) {
            throw new EccezioneValidazione("La quantità non può essere negativa.");
        }
    }
}
