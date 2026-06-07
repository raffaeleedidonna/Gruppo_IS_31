package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreCarrello;
import it.ecommerce.entity.persistenza.AzioneTransazionale;
import it.ecommerce.entity.persistenza.CategoriaDAO;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.NotificaDAO;
import it.ecommerce.entity.persistenza.OrdineDAO;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;
import it.ecommerce.entity.persistenza.UtenteDAO;

class CarrelloControllerImplTest {

    private CarrelloController controllerCon(Cliente cliente, Prodotto prodotto) {
        return new CarrelloControllerImpl(new GestoreCarrello(new FornitoreCarrelloFinto(cliente, prodotto)));
    }

    private Cliente nuovoCliente() {
        return new Cliente("c@x.it", "pw", "Mario", "Rossi", new Profilo());
    }

    @Test
    void aggiungeAlCarrelloQuandoDisponibile() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("1.50"), 5, true, false);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, prodotto).aggiungiAlCarrello(1L, 1L, 3);

        assertTrue(esito.successo());
        assertEquals(1, cliente.getCarrello().getRighe().size());
        assertEquals(3, cliente.getCarrello().getRighe().get(0).getQuantita());
    }

    @Test
    void rifiutaAggiuntaOltreDisponibilita() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("1.50"), 2, true, false);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, prodotto).aggiungiAlCarrello(1L, 1L, 5);

        assertFalse(esito.successo());
        assertTrue(cliente.getCarrello() == null || cliente.getCarrello().isVuoto());
    }

    @Test
    void rifiutaQuantitaNonPositiva() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("1.50"), 5, true, false);
        Cliente cliente = nuovoCliente();

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, prodotto).aggiungiAlCarrello(1L, 1L, 0);

        assertFalse(esito.successo());
    }

    @Test
    void modificaOltreDisponibilitaVieneRifiutata() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("1.50"), 3, true, false);
        Cliente cliente = nuovoCliente();
        cliente.carrelloCorrente().aggiungi(prodotto, 2);
        impostaId(cliente.getCarrello().getRighe().get(0), 1L);

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, prodotto).modificaQuantita(1L, 1L, 10);

        assertFalse(esito.successo());
        assertEquals(2, cliente.getCarrello().getRighe().get(0).getQuantita());
    }

    @Test
    void modificaAZeroRimuoveLElemento() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("1.50"), 3, true, false);
        Cliente cliente = nuovoCliente();
        cliente.carrelloCorrente().aggiungi(prodotto, 2);
        impostaId(cliente.getCarrello().getRighe().get(0), 1L);

        EsitoDTO<CarrelloDTO> esito = controllerCon(cliente, prodotto).modificaQuantita(1L, 1L, 0);

        assertTrue(esito.successo());
        assertTrue(cliente.getCarrello().isVuoto());
    }

    private static void impostaId(RigaCarrello riga, long id) {
        try {
            Field campo = RigaCarrello.class.getDeclaredField("id");
            campo.setAccessible(true);
            campo.set(riga, id);
        } catch (ReflectiveOperationException eccezione) {
            throw new IllegalStateException(eccezione);
        }
    }

    private static final class FornitoreCarrelloFinto implements FornitorePersistenza {

        private final Cliente cliente;
        private final Prodotto prodotto;

        FornitoreCarrelloFinto(Cliente cliente, Prodotto prodotto) {
            this.cliente = cliente;
            this.prodotto = prodotto;
        }

        @Override
        public <R> R inTransazione(AzioneTransazionale<R> azione) {
            return azione.esegui();
        }

        @Override
        public UtenteDAO utenteDAO() {
            return new UtenteDAO() {
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
            };
        }

        @Override
        public ProdottoDAO prodottoDAO() {
            return new ProdottoDAO() {
                @Override
                public Prodotto salva(Prodotto prodottoDaSalvare) {
                    return prodottoDaSalvare;
                }

                @Override
                public Optional<Prodotto> perId(Long id) {
                    return Optional.of(prodotto);
                }

                @Override
                public List<Prodotto> tuttiNelCatalogo() {
                    return List.of(prodotto);
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
                    return false;
                }

                @Override
                public List<Prodotto> perCategoria(Long categoriaId) {
                    return List.of();
                }
            };
        }

        @Override
        public CategoriaDAO categoriaDAO() {
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
            return null;
        }
    }
}
