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
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.StatoOrdine;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreOrdiniCliente;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.OrdineRepository;
import it.ecommerce.entity.persistenza.UtenteRepository;
import it.ecommerce.entity.servizi.MessaggioNotifica;

class OrdineClienteControllerImplTest {

    private ProdottoCatalogo voce(Long prodottoId, BigDecimal prezzo, int magazzino, boolean disponibile) {
        Prodotto prodotto = new Prodotto("Penna", "", new Categoria("Cancelleria"));
        SupportoTest.assegnaId(prodotto, prodottoId);
        return new ProdottoCatalogo(prodotto, prezzo, magazzino, disponibile, false);
    }

    private OrdineClienteController controllerCon(UtenteFinto utenti, OrdineFinto ordini,
                                                 ServizioNotificheFinto notifiche, CatalogoFinto catalogoRepo) {
        return new OrdineClienteControllerImpl(new GestoreOrdiniCliente(
                new TransazioniDirette(), utenti, ordini, notifiche, catalogoRepo));
    }

    @Test
    void confermaOrdineDecrementaStockSvuotaCarrelloEFissaPrezzo() {
        ProdottoCatalogo voce = voce(1L, new BigDecimal("2.50"), 10, true);
        Cliente cliente = new Cliente("c@x.it", "pw", new Profilo("Mario", "Rossi", null, null));
        cliente.carrelloCorrente().aggiungi(voce.getProdotto(), 4);
        OrdineFinto ordini = new OrdineFinto();

        EsitoDTO<OrdineDTO> esito = controllerCon(new UtenteFinto(cliente), ordini,
                new ServizioNotificheFinto(), new CatalogoFinto(voce)).confermaOrdine(1L, "Via Test 1");

        assertTrue(esito.successo());
        assertEquals(6, voce.getQuantitaMagazzino());
        assertTrue(cliente.getCarrello().isVuoto());

        Ordine salvato = ordini.ordineSalvato();
        assertEquals(1, salvato.getRighe().size());
        assertEquals(4, salvato.getRighe().get(0).getQuantitaAcquistata());
        assertEquals(0, new BigDecimal("2.50").compareTo(salvato.getRighe().get(0).getPrezzoDiAcquisto()));
        assertEquals(StatoOrdine.INSERITO, salvato.getStato());
        assertEquals(0, new BigDecimal("10.00").compareTo(salvato.getTotaleComplessivo()));
    }

    @Test
    void confermaOrdineInviaNotificaAlCliente() {
        ProdottoCatalogo voce = voce(1L, new BigDecimal("2.50"), 10, true);
        Cliente cliente = new Cliente("c@x.it", "pw", new Profilo("Mario", "Rossi", null, null));
        cliente.carrelloCorrente().aggiungi(voce.getProdotto(), 2);
        OrdineFinto ordini = new OrdineFinto();
        ServizioNotificheFinto notifiche = new ServizioNotificheFinto();

        controllerCon(new UtenteFinto(cliente), ordini, notifiche, new CatalogoFinto(voce))
                .confermaOrdine(1L, "Via Test 1");

        MessaggioNotifica notifica = notifiche.ultimo();
        assertEquals("c@x.it", notifica.destinatario());
        assertTrue(notifica.testo().contains("confermato"));
    }

    @Test
    void confermaOrdineConDisponibilitaInsufficienteAdeguaIlCarrello() {
        ProdottoCatalogo voce = voce(1L, new BigDecimal("2.50"), 2, true);
        Cliente cliente = new Cliente("c@x.it", "pw", new Profilo("Mario", "Rossi", null, null));
        cliente.carrelloCorrente().aggiungi(voce.getProdotto(), 5);
        OrdineFinto ordini = new OrdineFinto();

        EsitoDTO<OrdineDTO> esito = controllerCon(new UtenteFinto(cliente), ordini,
                new ServizioNotificheFinto(), new CatalogoFinto(voce)).confermaOrdine(1L, null);

        assertFalse(esito.successo());
        assertNull(ordini.ordineSalvato());
        assertEquals(2, cliente.getCarrello().getRighe().get(0).getQuantita());
        assertEquals(2, voce.getQuantitaMagazzino());
    }

    @Test
    void confermaOrdineConCarrelloVuotoFallisce() {
        ProdottoCatalogo voce = voce(1L, new BigDecimal("2.50"), 10, true);
        Cliente cliente = new Cliente("c@x.it", "pw", new Profilo("Mario", "Rossi", null, null));
        OrdineFinto ordini = new OrdineFinto();

        EsitoDTO<OrdineDTO> esito = controllerCon(new UtenteFinto(cliente), ordini,
                new ServizioNotificheFinto(), new CatalogoFinto(voce)).confermaOrdine(1L, null);

        assertFalse(esito.successo());
        assertNull(ordini.ordineSalvato());
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

    private static final class OrdineFinto implements OrdineRepository {

        private Ordine ordineSalvato;

        Ordine ordineSalvato() {
            return ordineSalvato;
        }

        @Override
        public Ordine salva(Ordine ordine) {
            ordineSalvato = ordine;
            return ordine;
        }

        @Override
        public Optional<Ordine> perId(Long id) {
            return Optional.ofNullable(ordineSalvato);
        }

        @Override
        public List<Ordine> tutti() {
            return List.of();
        }

        @Override
        public List<Ordine> perCliente(Long clienteId) {
            return List.of();
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
