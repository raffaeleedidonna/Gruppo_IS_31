package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreCarrello;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.UtenteRepository;

class CarrelloControllerImplTest {

    private ProdottoCatalogo voce(int magazzino) {
        Prodotto prodotto = new Prodotto("Penna", "", new Categoria("Cancelleria"));
        SupportoTest.assegnaId(prodotto, 1L);
        return new ProdottoCatalogo(prodotto, new BigDecimal("1.50"), magazzino, true, false);
    }

    private CarrelloController controllerCon(Cliente cliente, ProdottoCatalogo voce) {
        return new CarrelloControllerImpl(new GestoreCarrello(
                new TransazioniDirette(), new UtenteFinto(cliente), new CatalogoFinto(voce)));
    }

    private Cliente nuovoCliente() {
        return new Cliente("c@x.it", "pw", new Profilo("Mario", "Rossi", null, null));
    }

    @Test
    void aggiungeAlCarrelloQuandoDisponibile() {
        ProdottoCatalogo voce = voce(5);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, voce).aggiungiAlCarrello(1L, 1L, 3);

        assertTrue(esito.successo());
        assertEquals(1, cliente.getCarrello().getRighe().size());
        assertEquals(3, cliente.getCarrello().getRighe().get(0).getQuantita());
    }

    @Test
    void rifiutaAggiuntaOltreDisponibilita() {
        ProdottoCatalogo voce = voce(2);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, voce).aggiungiAlCarrello(1L, 1L, 5);

        assertFalse(esito.successo());
        assertTrue(cliente.getCarrello() == null || cliente.getCarrello().isVuoto());
    }

    @Test
    void rifiutaQuantitaNonPositiva() {
        ProdottoCatalogo voce = voce(5);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, voce).aggiungiAlCarrello(1L, 1L, 0);

        assertFalse(esito.successo());
    }

    @Test
    void modificaOltreDisponibilitaVieneRifiutata() {
        ProdottoCatalogo voce = voce(3);
        Cliente cliente = nuovoCliente();
        cliente.carrelloCorrente().aggiungi(voce.getProdotto(), 2);
        SupportoTest.assegnaId(cliente.getCarrello().getRighe().get(0), 1L);

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, voce).modificaQuantita(1L, 1L, 10);

        assertFalse(esito.successo());
        assertEquals(2, cliente.getCarrello().getRighe().get(0).getQuantita());
    }

    @Test
    void modificaAZeroRimuoveLElemento() {
        ProdottoCatalogo voce = voce(3);
        Cliente cliente = nuovoCliente();
        cliente.carrelloCorrente().aggiungi(voce.getProdotto(), 2);
        SupportoTest.assegnaId(cliente.getCarrello().getRighe().get(0), 1L);

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, voce).modificaQuantita(1L, 1L, 0);

        assertTrue(esito.successo());
        assertTrue(cliente.getCarrello().isVuoto());
    }

    private static final class UtenteFinto implements UtenteRepository {

        private final Cliente cliente;

        UtenteFinto(Cliente cliente) {
            this.cliente = cliente;
        }

        @Override
        public Utente salva(Utente utente) {
            return utente;
        }

        @Override
        public Optional<Utente> perEmail(String email) {
            return Optional.empty();
        }

        @Override
        public Optional<Utente> perId(Long id) {
            return Optional.of(cliente);
        }

        @Override
        public boolean esisteEmail(String email) {
            return false;
        }

        @Override
        public long contaClienti() {
            return 1;
        }
    }

    private static final class CatalogoFinto implements CatalogoRepository {

        private final ProdottoCatalogo voce;

        CatalogoFinto(ProdottoCatalogo voce) {
            this.voce = voce;
        }

        @Override
        public ProdottoCatalogo salva(ProdottoCatalogo voceDaSalvare) {
            return voceDaSalvare;
        }

        @Override
        public void rimuovi(ProdottoCatalogo voceDaRimuovere) {
        }

        @Override
        public Optional<ProdottoCatalogo> vocePerProdotto(Long prodottoId) {
            return voce.getProdotto().getId().equals(prodottoId) ? Optional.of(voce) : Optional.empty();
        }

        @Override
        public List<ProdottoCatalogo> tutte() {
            return List.of(voce);
        }

        @Override
        public List<ProdottoCatalogo> inOfferta() {
            return List.of();
        }

        @Override
        public List<ProdottoCatalogo> cerca(String termine) {
            return List.of();
        }

        @Override
        public boolean esisteProdottoPerNome(String nome) {
            return false;
        }

        @Override
        public long conta() {
            return 1;
        }
    }
}
