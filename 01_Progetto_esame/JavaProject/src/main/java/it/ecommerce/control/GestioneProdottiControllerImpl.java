package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.CategoriaDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.coordinatore.GestoreProdotti;

public class GestioneProdottiControllerImpl implements GestioneProdottiController {

    private final GestoreProdotti gestore;

    public GestioneProdottiControllerImpl() {
        this(new GestoreProdotti());
    }

    public GestioneProdottiControllerImpl(GestoreProdotti gestore) {
        this.gestore = gestore;
    }

    @Override
    public List<ProdottoDTO> elencoProdotti() {
        return gestore.tuttiNelCatalogo().stream().map(MappaProdotti::aDTO).toList();
    }

    @Override
    public EsitoDTO<ProdottoDTO> aggiungiProdotto(ProdottoDTO nuovo) {
        try {
            Prodotto creato = gestore.aggiungi(daDTO(nuovo), nuovo.categoriaId());
            return EsitoDTO.successo("Prodotto creato.", MappaProdotti.aDTO(creato));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public ProdottoDTO dettaglioProdotto(Long prodottoId) {
        Prodotto prodotto = gestore.dettaglio(prodottoId);
        return prodotto == null ? null : MappaProdotti.aDTO(prodotto);
    }

    @Override
    public EsitoDTO<ProdottoDTO> modificaProdotto(ProdottoDTO modificato) {
        try {
            Prodotto aggiornato = gestore.modifica(modificato.id(), daDTO(modificato), modificato.categoriaId());
            return EsitoDTO.successo("Prodotto aggiornato.", MappaProdotti.aDTO(aggiornato));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public EsitoDTO<Void> rimuoviDalCatalogo(Long prodottoId) {
        try {
            gestore.rimuoviDalCatalogo(prodottoId);
            return EsitoDTO.successo("Prodotto rimosso dal catalogo.", null);
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public List<CategoriaDTO> categorie() {
        return gestore.categorie().stream()
                .map(categoria -> new CategoriaDTO(categoria.getId(), categoria.getNome()))
                .toList();
    }

    private Prodotto daDTO(ProdottoDTO dto) {
        return new Prodotto(dto.nome(), dto.descrizione(), dto.prezzoAttuale(),
                dto.quantitaMagazzino(), dto.disponibile(), dto.inOfferta());
    }
}
