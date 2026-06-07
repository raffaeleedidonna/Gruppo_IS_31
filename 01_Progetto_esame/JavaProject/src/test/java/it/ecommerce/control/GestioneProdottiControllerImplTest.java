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
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.coordinatore.GestoreProdotti;
import it.ecommerce.entity.persistenza.AzioneTransazionale;
import it.ecommerce.entity.persistenza.CategoriaDAO;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.NotificaDAO;
import it.ecommerce.entity.persistenza.OrdineDAO;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;
import it.ecommerce.entity.persistenza.UtenteDAO;

class GestioneProdottiControllerImplTest {

    private GestioneProdottiController controllerCon(FornitorePersistenzaFinto fornitore) {
        return new GestioneProdottiControllerImpl(new GestoreProdotti(fornitore));
    }

    @Test
    void aggiungeUnProdottoValido() {
        FornitorePersistenzaFinto fornitore = new FornitorePersistenzaFinto();
        fornitore.aggiungiCategoria(1L, "Elettronica");
        GestioneProdottiController controller = controllerCon(fornitore);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Tastiera", "meccanica", new BigDecimal("49.90"), 10, true, false, 1L, null));

        assertTrue(esito.successo());
        assertEquals(1, controller.elencoProdotti().size());
    }

    @Test
    void rifiutaQuantitaNegativa() {
        FornitorePersistenzaFinto fornitore = new FornitorePersistenzaFinto();
        fornitore.aggiungiCategoria(1L, "Elettronica");
        GestioneProdottiController controller = controllerCon(fornitore);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Monitor", "27 pollici", new BigDecimal("199.00"), -1, true, false, 1L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().toLowerCase().contains("negativa"));
        assertTrue(controller.elencoProdotti().isEmpty());
    }

    @Test
    void rifiutaNomeDuplicato() {
        FornitorePersistenzaFinto fornitore = new FornitorePersistenzaFinto();
        fornitore.aggiungiCategoria(1L, "Elettronica");
        fornitore.aggiungiProdottoEsistente(new Prodotto("Mouse", "wireless",
                new BigDecimal("19.90"), 5, true, false));
        GestioneProdottiController controller = controllerCon(fornitore);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Mouse", "altro", new BigDecimal("21.00"), 3, true, false, 1L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().contains("Esiste già"));
    }

    @Test
    void rifiutaCategoriaNonValida() {
        FornitorePersistenzaFinto fornitore = new FornitorePersistenzaFinto();
        GestioneProdottiController controller = controllerCon(fornitore);

        EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(new ProdottoDTO(
                null, "Webcam", "full hd", new BigDecimal("39.90"), 8, true, false, 99L, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().contains("Categoria"));
    }

    private static final class FornitorePersistenzaFinto implements FornitorePersistenza {

        private final ProdottoDAOFinto prodotti = new ProdottoDAOFinto();
        private final CategoriaDAOFinto categorie = new CategoriaDAOFinto();

        void aggiungiCategoria(Long id, String nome) {
            categorie.aggiungi(id, nome);
        }

        void aggiungiProdottoEsistente(Prodotto prodotto) {
            prodotti.salva(prodotto);
        }

        @Override
        public ProdottoDAO prodottoDAO() {
            return prodotti;
        }

        @Override
        public CategoriaDAO categoriaDAO() {
            return categorie;
        }

        @Override
        public UtenteDAO utenteDAO() {
            return null;
        }

        @Override
        public OrdineDAO ordineDAO() {
            return null;
        }

        @Override
        public NotificaDAO notificaDAO() {
            return null;
        }

        @Override
        public UnitaDiLavoro apreUnitaDiLavoro() {
            return new UnitaDiLavoroFinta();
        }

        @Override
        public <R> R inTransazione(AzioneTransazionale<R> azione) {
            return azione.esegui();
        }
    }

    private static final class ProdottoDAOFinto implements ProdottoDAO {

        private final List<Prodotto> dati = new ArrayList<>();

        @Override
        public Prodotto salva(Prodotto prodotto) {
            if (!dati.contains(prodotto)) {
                dati.add(prodotto);
            }
            return prodotto;
        }

        @Override
        public Optional<Prodotto> perId(Long id) {
            return Optional.empty();
        }

        @Override
        public List<Prodotto> tuttiNelCatalogo() {
            return dati.stream().filter(Prodotto::isPresenteNelCatalogo).toList();
        }

        @Override
        public List<Prodotto> inOfferta() {
            return List.of();
        }

        @Override
        public List<Prodotto> cerca(String termine) {
            return List.of();
        }

        @Override
        public boolean esistePerNome(String nome) {
            return dati.stream().anyMatch(prodotto -> prodotto.getNome().equalsIgnoreCase(nome));
        }

        @Override
        public List<Prodotto> perCategoria(Long categoriaId) {
            return List.of();
        }
    }

    private static final class CategoriaDAOFinto implements CategoriaDAO {

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
            return new ArrayList<>(dati.values());
        }
    }

    private static final class UnitaDiLavoroFinta implements UnitaDiLavoro {

        @Override
        public void inizia() {
        }

        @Override
        public void conferma() {
        }

        @Override
        public void annulla() {
        }

        @Override
        public void chiudi() {
        }
    }
}
