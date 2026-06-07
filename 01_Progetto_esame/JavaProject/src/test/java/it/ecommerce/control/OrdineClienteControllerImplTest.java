package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Notifica;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreOrdiniCliente;
import it.ecommerce.entity.persistenza.AzioneTransazionale;
import it.ecommerce.entity.persistenza.CategoriaDAO;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.NotificaDAO;
import it.ecommerce.entity.persistenza.OrdineDAO;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;
import it.ecommerce.entity.persistenza.UtenteDAO;

class OrdineClienteControllerImplTest {

    private OrdineClienteController controllerCon(FornitoreOrdiniFinto fornitore) {
        return new OrdineClienteControllerImpl(new GestoreOrdiniCliente(fornitore));
    }

    @Test
    void confermaOrdineDecrementaStockSvuotaCarrelloEFissaPrezzo() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("2.50"), 10, true, false);
        Cliente cliente = new Cliente("c@x.it", "pw", "Mario", "Rossi", new Profilo());
        cliente.carrelloCorrente().aggiungi(prodotto, 4);
        FornitoreOrdiniFinto fornitore = new FornitoreOrdiniFinto(cliente);

        EsitoDTO<OrdineDTO> esito = controllerCon(fornitore).confermaOrdine(1L, "Via Test 1");

        assertTrue(esito.successo());
        assertEquals(6, prodotto.getQuantitaMagazzino());
        assertTrue(cliente.getCarrello().isVuoto());

        Ordine salvato = fornitore.ordineSalvato();
        assertEquals(1, salvato.getRighe().size());
        assertEquals(4, salvato.getRighe().get(0).getQuantitaAcquistata());
        assertEquals(0, new BigDecimal("2.50").compareTo(salvato.getRighe().get(0).getPrezzoDiAcquisto()));
        assertEquals(StatoOrdine.INSERITO, salvato.getStato());
        assertEquals(0, new BigDecimal("10.00").compareTo(salvato.getTotaleComplessivo()));
    }

    @Test
    void confermaOrdineConDisponibilitaInsufficienteAdeguaIlCarrello() {
        Prodotto prodotto = new Prodotto("Penna", "", new BigDecimal("2.50"), 2, true, false);
        Cliente cliente = new Cliente("c@x.it", "pw", "Mario", "Rossi", new Profilo());
        cliente.carrelloCorrente().aggiungi(prodotto, 5);
        FornitoreOrdiniFinto fornitore = new FornitoreOrdiniFinto(cliente);

        EsitoDTO<OrdineDTO> esito = controllerCon(fornitore).confermaOrdine(1L, null);

        assertFalse(esito.successo());
        assertNull(fornitore.ordineSalvato());
        assertEquals(2, cliente.getCarrello().getRighe().get(0).getQuantita());
        assertEquals(2, prodotto.getQuantitaMagazzino());
    }

    @Test
    void confermaOrdineConCarrelloVuotoFallisce() {
        Cliente cliente = new Cliente("c@x.it", "pw", "Mario", "Rossi", new Profilo());
        FornitoreOrdiniFinto fornitore = new FornitoreOrdiniFinto(cliente);

        EsitoDTO<OrdineDTO> esito = controllerCon(fornitore).confermaOrdine(1L, null);

        assertFalse(esito.successo());
        assertNull(fornitore.ordineSalvato());
    }

    private static final class FornitoreOrdiniFinto implements FornitorePersistenza {

        private final Cliente cliente;
        private Ordine ordineSalvato;

        FornitoreOrdiniFinto(Cliente cliente) {
            this.cliente = cliente;
        }

        Ordine ordineSalvato() {
            return ordineSalvato;
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
        public OrdineDAO ordineDAO() {
            return new OrdineDAO() {
                @Override
                public Ordine salva(Ordine ordine) {
                    ordineSalvato = ordine;
                    return ordine;
                }

                @Override
                public Optional<Ordine> perId(Long id) {
                    return Optional.empty();
                }

                @Override
                public List<Ordine> tutti() {
                    return List.of();
                }

                @Override
                public List<Ordine> perCliente(Long clienteId) {
                    return List.of();
                }
            };
        }

        @Override
        public NotificaDAO notificaDAO() {
            return new NotificaDAO() {
                @Override
                public Notifica salva(Notifica notifica) {
                    return notifica;
                }

                @Override
                public List<Notifica> perCliente(Long clienteId) {
                    return List.of();
                }
            };
        }

        @Override
        public ProdottoDAO prodottoDAO() {
            return null;
        }

        @Override
        public CategoriaDAO categoriaDAO() {
            return null;
        }

        @Override
        public UnitaDiLavoro apreUnitaDiLavoro() {
            return null;
        }
    }
}
