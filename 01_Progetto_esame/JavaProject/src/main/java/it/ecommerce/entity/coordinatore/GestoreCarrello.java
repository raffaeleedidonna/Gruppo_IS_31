package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreCarrello {

    private final FornitorePersistenza fornitore;

    public GestoreCarrello() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreCarrello(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public Carrello aggiungiAlCarrello(Long clienteId, Long prodottoId, int quantita) {
        return fornitore.inTransazione(() -> {
            if (quantita <= 0) {
                throw new EccezioneValidazione("La quantità deve essere positiva.");
            }
            Cliente cliente = clienteValido(clienteId);
            Prodotto prodotto = fornitore.prodottoDAO().perId(prodottoId)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            Carrello carrello = cliente.carrelloCorrente();
            int quantitaRisultante = carrello.quantitaProdotto(prodotto) + quantita;
            if (!prodotto.disponibilitaSufficiente(quantitaRisultante)) {
                throw new EccezioneValidazione("Prodotto non disponibile nella quantità richiesta.");
            }
            carrello.aggiungi(prodotto, quantita);
            fornitore.utenteDAO().salva(cliente);
            return inizializza(carrello);
        });
    }

    public Carrello modificaQuantita(Long clienteId, Long rigaCarrelloId, int quantitaDesiderata) {
        return fornitore.inTransazione(() -> {
            Cliente cliente = clienteValido(clienteId);
            Carrello carrello = cliente.getCarrello();
            if (carrello == null) {
                throw new EccezioneValidazione("Il carrello è vuoto.");
            }
            RigaCarrello riga = carrello.rigaPerId(rigaCarrelloId);
            if (riga == null) {
                throw new EccezioneValidazione("Elemento non presente nel carrello.");
            }
            if (quantitaDesiderata > 0) {
                if (quantitaDesiderata > riga.getProdotto().getQuantitaMagazzino()) {
                    throw new EccezioneValidazione("Quantità superiore alla disponibilità.");
                }
                riga.impostaQuantita(quantitaDesiderata);
            } else {
                carrello.rimuoviRiga(riga);
            }
            fornitore.utenteDAO().salva(cliente);
            return inizializza(carrello);
        });
    }

    public Carrello carrello(Long clienteId) {
        return fornitore.inTransazione(() -> inizializza(clienteValido(clienteId).getCarrello()));
    }

    private Cliente clienteValido(Long clienteId) {
        Utente utente = fornitore.utenteDAO().perId(clienteId)
                .orElseThrow(() -> new EccezioneValidazione("Utente non trovato."));
        if (!(utente instanceof Cliente cliente)) {
            throw new EccezioneValidazione("Operazione consentita solo ai clienti.");
        }
        return cliente;
    }

    private Carrello inizializza(Carrello carrello) {
        if (carrello != null) {
            carrello.getRighe().size();
        }
        return carrello;
    }
}
