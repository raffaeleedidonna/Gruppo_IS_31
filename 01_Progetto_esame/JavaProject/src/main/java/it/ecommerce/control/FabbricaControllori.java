package it.ecommerce.control;

import it.ecommerce.entity.coordinatore.GestoreAutenticazione;
import it.ecommerce.entity.coordinatore.GestoreCarrello;
import it.ecommerce.entity.coordinatore.GestoreCatalogo;
import it.ecommerce.entity.coordinatore.GestoreMonitoraggio;
import it.ecommerce.entity.coordinatore.GestoreOrdini;
import it.ecommerce.entity.coordinatore.GestoreOrdiniCliente;
import it.ecommerce.entity.coordinatore.GestoreProdotti;
import it.ecommerce.entity.coordinatore.GestoreProfilo;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public final class FabbricaControllori {

    private final FornitorePersistenza persistenza;
    private final GestoreTransazioni transazioni;

    public FabbricaControllori() {
        this(RegistroPersistenza.fornitore());
    }

    public FabbricaControllori(FornitorePersistenza persistenza) {
        this.persistenza = persistenza;
        this.transazioni = persistenza.gestoreTransazioni();
    }

    public AutenticazioneController autenticazione() {
        return new AutenticazioneControllerImpl(new GestoreAutenticazione(
                transazioni, persistenza.utenteRepository(), persistenza.servizioNotifiche()));
    }

    public GestioneProdottiController gestioneProdotti() {
        return new GestioneProdottiControllerImpl(new GestoreProdotti(
                transazioni, persistenza.catalogoRepository(), persistenza.categoriaRepository()));
    }

    public CatalogoController catalogo() {
        return new CatalogoControllerImpl(
                new GestoreCatalogo(transazioni, persistenza.catalogoRepository()));
    }

    public CarrelloController carrello() {
        return new CarrelloControllerImpl(new GestoreCarrello(
                transazioni, persistenza.utenteRepository(), persistenza.catalogoRepository()));
    }

    public OrdineClienteController ordineCliente() {
        return new OrdineClienteControllerImpl(new GestoreOrdiniCliente(
                transazioni, persistenza.utenteRepository(), persistenza.ordineRepository(),
                persistenza.servizioNotifiche(), persistenza.catalogoRepository()));
    }

    public GestioneOrdiniController gestioneOrdini() {
        return new GestioneOrdiniControllerImpl(new GestoreOrdini(
                transazioni, persistenza.ordineRepository(), persistenza.catalogoRepository(),
                persistenza.servizioNotifiche()));
    }

    public ProfiloController profilo() {
        return new ProfiloControllerImpl(
                new GestoreProfilo(transazioni, persistenza.utenteRepository()));
    }

    public MonitoraggioController monitoraggio() {
        return new MonitoraggioControllerImpl(new GestoreMonitoraggio(
                transazioni, persistenza.ordineRepository(), persistenza.catalogoRepository(),
                persistenza.utenteRepository()));
    }
}
