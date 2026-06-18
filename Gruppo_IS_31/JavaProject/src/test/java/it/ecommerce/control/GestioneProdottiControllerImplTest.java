package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.coordinatore.GestoreProdotti;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.CategoriaRepository;

class GestioneProdottiControllerImplTest {

    private GestioneProdottiController controllerCon(CatalogoFinto catalogo, CategorieFinto categorie) {
        return new GestioneProdottiControllerImpl(
                new GestoreProdotti(new TransazioniDirette(), catalogo, categorie));
    }

    @Test
    void aggiungeUnProdottoValido() {
        CatalogoFinto catalogo = new CatalogoFinto();
        CategorieFinto categorie = new CategorieFinto();
        categorie.aggiungi(1L, "Elettronica");
        GestioneProdottiController controller = controllerCon(catalogo, categorie);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Tastiera", "meccanica", new BigDecimal("49.90"), 10, true, false, 1L, null));

        assertTrue(esito.successo());
        assertEquals(1, controller.elencoProdotti().size());
    }

    @Test
    void rifiutaQuantitaNegativa() {
        CatalogoFinto catalogo = new CatalogoFinto();
        CategorieFinto categorie = new CategorieFinto();
        categorie.aggiungi(1L, "Elettronica");
        GestioneProdottiController controller = controllerCon(catalogo, categorie);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Monitor", "27 pollici", new BigDecimal("199.00"), -1, true, false, 1L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().toLowerCase().contains("negativa"));
        assertTrue(controller.elencoProdotti().isEmpty());
    }

    @Test
    void rifiutaNomeDuplicato() {
        CatalogoFinto catalogo = new CatalogoFinto();
        CategorieFinto categorie = new CategorieFinto();
        categorie.aggiungi(1L, "Elettronica");
        catalogo.aggiungiVoceEsistente("Mouse", new Categoria("Elettronica"));
        GestioneProdottiController controller = controllerCon(catalogo, categorie);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Mouse", "altro", new BigDecimal("21.00"), 3, true, false, 1L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().contains("Esiste già"));
    }

    @Test
    void rifiutaCategoriaNonValida() {
        CatalogoFinto catalogo = new CatalogoFinto();
        CategorieFinto categorie = new CategorieFinto();
        GestioneProdottiController controller = controllerCon(catalogo, categorie);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Webcam", "full hd", new BigDecimal("39.90"), 8, true, false, 99L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().contains("Categoria"));
    }

    @Test
    void rimozioneDalCatalogoMantieneProdottoPerStoricoOrdine() {
        CatalogoFinto catalogo = new CatalogoFinto();
        CategorieFinto categorie = new CategorieFinto();
        categorie.aggiungi(1L, "Elettronica");
        GestoreProdotti gestore = new GestoreProdotti(new TransazioniDirette(), catalogo, categorie);
        ProdottoCatalogo voce = gestore.aggiungi("Mouse", "wireless",
                new BigDecimal("19.90"), 5, true, false, 1L);
        SupportoTest.assegnaId(voce.getProdotto(), 7L);

        Ordine ordine = new Ordine(new Cliente("c@x.it", "pw", new Profilo("M", "R", null, null)), "Via Test 1");
        ordine.aggiungiRiga(voce.getProdotto(), 2, voce.getPrezzoAttuale());

        gestore.rimuoviDalCatalogo(7L);

        assertTrue(catalogo.tutte().isEmpty());
        assertEquals("Mouse", ordine.getRighe().get(0).getProdotto().getNome());
        assertEquals(0, new BigDecimal("19.90").compareTo(ordine.getRighe().get(0).getPrezzoDiAcquisto()));
    }

    private static final class CatalogoFinto implements CatalogoRepository {

        private final List<ProdottoCatalogo> voci = new ArrayList<>();

        void aggiungiVoceEsistente(String nome, Categoria categoria) {
            Prodotto prodotto = new Prodotto(nome, "", categoria);
            voci.add(new ProdottoCatalogo(prodotto, new BigDecimal("1.00"), 1, true, false));
        }

        @Override
        public ProdottoCatalogo salva(ProdottoCatalogo voce) {
            if (!voci.contains(voce)) {
                voci.add(voce);
            }
            return voce;
        }

        @Override
        public void rimuovi(ProdottoCatalogo voce) {
            voci.remove(voce);
        }

        @Override
        public Optional<ProdottoCatalogo> vocePerProdotto(Long prodottoId) {
            return voci.stream()
                    .filter(voce -> voce.getProdotto().getId() != null
                            && voce.getProdotto().getId().equals(prodottoId))
                    .findFirst();
        }

        @Override
        public List<ProdottoCatalogo> tutte() {
            return voci;
        }

        @Override
        public List<ProdottoCatalogo> inOfferta() {
            return voci.stream().filter(ProdottoCatalogo::isInOfferta).toList();
        }

        @Override
        public List<ProdottoCatalogo> cerca(String termine) {
            return List.of();
        }

        @Override
        public boolean esisteProdottoPerNome(String nome) {
            return voci.stream()
                    .anyMatch(voce -> voce.getProdotto().getNome().equalsIgnoreCase(nome));
        }

        @Override
        public long conta() {
            return voci.size();
        }
    }

    private static final class CategorieFinto implements CategoriaRepository {

        private final Map<Long, Categoria> dati = new LinkedHashMap<>();

        void aggiungi(Long id, String nome) {
            dati.put(id, new Categoria(nome));
        }

        @Override
        public Categoria salva(Categoria categoria) {
            return categoria;
        }

        @Override
        public Optional<Categoria> perId(Long id) {
            return Optional.ofNullable(dati.get(id));
        }

        @Override
        public Optional<Categoria> perNome(String nome) {
            return dati.values().stream()
                    .filter(categoria -> categoria.getNome().equalsIgnoreCase(nome))
                    .findFirst();
        }

        @Override
        public List<Categoria> tutte() {
            return new java.util.ArrayList<>(dati.values());
        }
    }
}
