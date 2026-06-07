package it.ecommerce.control;

public final class FabbricaControllori {

    public AutenticazioneController autenticazione() {
        return new AutenticazioneControllerImpl();
    }

    public GestioneProdottiController gestioneProdotti() {
        return new GestioneProdottiControllerImpl();
    }

    public CatalogoController catalogo() {
        return new CatalogoControllerImpl();
    }

    public CarrelloController carrello() {
        return new CarrelloControllerImpl();
    }

    public OrdineClienteController ordineCliente() {
        return new OrdineClienteControllerImpl();
    }

    public GestioneOrdiniController gestioneOrdini() {
        return new GestioneOrdiniControllerImpl();
    }

    public ProfiloController profilo() {
        return new ProfiloControllerImpl();
    }

    public MonitoraggioController monitoraggio() {
        return new MonitoraggioControllerImpl();
    }
}
