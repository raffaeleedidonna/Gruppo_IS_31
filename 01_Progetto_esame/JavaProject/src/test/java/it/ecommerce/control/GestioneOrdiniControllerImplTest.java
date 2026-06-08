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
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.coordinatore.GestoreOrdini;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.OrdineRepository;

class GestioneOrdiniControllerImplTest {

    private ProdottoCatalogo voce(int magazzino) {
        Prodotto prodotto = new Prodotto("X", "", new Categoria("Cat"));
        SupportoTest.assegnaId(prodotto, 1L);
        return new ProdottoCatalogo(prodotto, new BigDecimal("5.00"), magazzino, true, false);
    }

    private Ordine ordineConRiga(ProdottoCatalogo voce, int quantita) {
        Ordine ordine = new Ordine(new Cliente("c@x.it", "pw", new Profilo("M", "R", null, null)), "Via Test 1");
        ordine.aggiungiRiga(voce.getProdotto(), quantita, voce.getPrezzoAttuale());
        return ordine;
    }

    private GestioneOrdiniController controllerCon(Ordine ordine, ProdottoCatalogo voce) {
        return new GestioneOrdiniControllerImpl(new GestoreOrdini(
                new TransazioniDirette(), new OrdineFinto(ordine), new CatalogoFinto(voce)));
    }

    @Test
    void annullamentoRipristinaLoStock() {
        ProdottoCatalogo voce = voce(2);
        Ordine ordine = ordineConRiga(voce, 3);

        EsitoDTO<OrdineDTO> esito = controllerCon(ordine, voce).aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);

        assertTrue(esito.successo());
        assertEquals("Ordine annullato con successo", esito.messaggio());
        assertEquals(5, voce.getQuantitaMagazzino());
        assertEquals(StatoOrdine.ANNULLATO, ordine.getStato());
    }

    @Test
    void aggiornamentoDiStatoNonAnnullatoNonToccaLoStock() {
        ProdottoCatalogo voce = voce(2);
        Ordine ordine = ordineConRiga(voce, 3);

        EsitoDTO<OrdineDTO> esito = controllerCon(ordine, voce).aggiornaStato(10L, StatoOrdineDTO.SPEDITO);

        assertTrue(esito.successo());
        assertEquals(2, voce.getQuantitaMagazzino());
        assertEquals(StatoOrdine.SPEDITO, ordine.getStato());
    }

    @Test
    void doppioAnnullamentoNonRaddoppiaLoStock() {
        ProdottoCatalogo voce = voce(2);
        Ordine ordine = ordineConRiga(voce, 3);
        GestioneOrdiniController controller = controllerCon(ordine, voce);

        controller.aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);
        controller.aggiornaStato(10L, StatoOrdineDTO.ANNULLATO);

        assertEquals(5, voce.getQuantitaMagazzino());
        assertEquals(StatoOrdine.ANNULLATO, ordine.getStato());
    }

    private static final class OrdineFinto implements OrdineRepository {

        private final Ordine ordine;

        OrdineFinto(Ordine ordine) {
            this.ordine = ordine;
        }

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
