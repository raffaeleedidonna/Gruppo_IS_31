package it.ecommerce.control;

import java.util.List;

import it.ecommerce.control.dto.CategoriaDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.coordinatore.GestoreProdotti;

public class GestioneProdottiControllerImpl implements GestioneProdottiController {

    private final GestoreProdotti gestore;

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
            ProdottoCatalogo creato = gestore.aggiungi(nuovo.nome(), nuovo.descrizione(),
                    nuovo.prezzoAttuale(), nuovo.quantitaMagazzino(), nuovo.disponibile(),
                    nuovo.inOfferta(), nuovo.categoriaId());
            return EsitoDTO.successo("Prodotto creato.", MappaProdotti.aDTO(creato));
        } catch (EccezioneValidazione errore) {
            return EsitoDTO.errore(errore.getMessage());
        }
    }

    @Override
    public ProdottoDTO dettaglioProdotto(Long prodottoId) {
        ProdottoCatalogo voce = gestore.dettaglio(prodottoId);
        return voce == null ? null : MappaProdotti.aDTO(voce);
    }

    @Override
    public EsitoDTO<ProdottoDTO> modificaProdotto(ProdottoDTO modificato) {
        try {
            ProdottoCatalogo aggiornato = gestore.modifica(modificato.id(), modificato.nome(),
                    modificato.descrizione(), modificato.prezzoAttuale(), modificato.quantitaMagazzino(),
                    modificato.disponibile(), modificato.inOfferta(), modificato.categoriaId());
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
}
