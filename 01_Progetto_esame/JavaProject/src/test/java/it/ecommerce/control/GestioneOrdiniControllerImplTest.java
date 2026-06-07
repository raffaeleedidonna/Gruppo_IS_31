package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.coordinatore.GestoreOrdini;
import it.ecommerce.entity.persistenza.AzioneTransazionale;
import it.ecommerce.entity.persistenza.CategoriaDAO;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.NotificaDAO;
import it.ecommerce.entity.persistenza.OrdineDAO;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;
import it.ecommerce.entity.persistenza.UtenteDAO;

class GestioneOrdiniControllerImplTest {

    private GestioneOrdiniController controllerCon(Ordine ordine) {
        return new GestioneOrdiniControllerImpl(new GestoreOrdini(new FornitoreOrdiniFinto(ordine)));
    }

    private Ordine ordineConRiga(Prodotto prodotto, int quantita) {
        Ordine ordine = new Ordine(new Cliente("c@x.it", "pw", "M", "R", new Profilo()), "Via Test 1");
        ordine.aggiungiRiga(prodotto, quantita, prodotto.getPrezzoAttuale());
        return ordine;
    }

    @Test
    void annullamentoRipristinaLoStock() {
        Prodotto prodotto = new Prodotto("X", "", new BigDecimal("5.00"), 2, true, false);
        Ordine ordine = ordineConRiga(prodotto, 3);

        EsitoDTO<OrdineDTO> esito = controllerCon(ordine).aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);

        assertTrue(esito.successo());
        assertEquals("Ordine annullato con successo", esito.messaggio());
        assertEquals(5, prodotto.getQuantitaMagazzino());
        assertEquals(StatoOrdine.ANNULLATO, ordine.getStato());
    }

    @Test
    void aggiornamentoDiStatoNonAnnullatoNonToccaLoStock() {
        Prodotto prodotto = new Prodotto("X", "", new BigDecimal("5.00"), 2, true, false);
        Ordine ordine = ordineConRiga(prodotto, 3);

        EsitoDTO<OrdineDTO> esito = controllerCon(ordine).aggiornaStato(10L, StatoOrdineDTO.SPEDITO);

        assertTrue(esito.successo());
        assertEquals(2, prodotto.getQuantitaMagazzino());
        assertEquals(StatoOrdine.SPEDITO, ordine.getStato());
    }

    @Test
    void doppioAnnullamentoNonRaddoppiaLoStock() {
        Prodotto prodotto = new Prodotto("X", "", new BigDecimal("5.00"), 2, true, false);
        Ordine ordine = ordineConRiga(prodotto, 3);
        GestioneOrdiniController controller = controllerCon(ordine);

        controller.aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);
        controller.aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);

        assertEquals(5, prodotto.getQuantitaMagazzino());
        assertEquals(StatoOrdine.ANNULLATO, ordine.getStato());
    }

    private static final class FornitoreOrdiniFinto implements FornitorePersistenza {

        private final Ordine ordine;

        FornitoreOrdiniFinto(Ordine ordine) {
            this.ordine = ordine;
        }

        @Override
        public <R> R inTransazione(AzioneTransazionale<R> azione) {
            return azione.esegui();
        }

        @Override
        public OrdineDAO ordineDAO() {
            return new OrdineDAO() {
                @Override
                public Ordine salva(Ordine ordineDaSalvare) {
                    return ordineDaSalvare;
                }

                @Override
                public Optional<Ordine> perId(Long id) {
                    return Optional.of(ordine);
                }

                @Override
                public List<Ordine> tutti() {
                    return List.of(ordine);
                }

                @Override
                public List<Ordine> perCliente(Long clienteId) {
                    return List.of(ordine);
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
        public UtenteDAO utenteDAO() {
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
