package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.UtenteRepository;

public class GestoreCarrello {

    private final GestoreTransazioni transazioni;
    private final UtenteRepository utenti;
    private final CatalogoRepository catalogo;

    public GestoreCarrello(GestoreTransazioni transazioni, UtenteRepository utenti, CatalogoRepository catalogo) {
        this.transazioni = transazioni;
        this.utenti = utenti;
        this.catalogo = catalogo;
    }

    public VistaCarrello aggiungiAlCarrello(Long clienteId, Long prodottoId, int quantita) {
        return transazioni.inTransazione(() -> {
            if (quantita <= 0) {
                throw new EccezioneValidazione("La quantità deve essere positiva.");
            }
            Cliente cliente = clienteValido(clienteId);
            ProdottoCatalogo voce = catalogo.vocePerProdotto(prodottoId)
                    .orElseThrow(() -> new EccezioneValidazione("Prodotto non trovato."));
            Carrello carrello = cliente.carrelloCorrente();
            int quantitaRisultante = carrello.quantitaProdotto(voce.getProdotto()) + quantita;
            if (!voce.disponibilitaSufficiente(quantitaRisultante)) {
                throw new EccezioneValidazione("Prodotto non disponibile nella quantità richiesta.");
            }
            carrello.aggiungi(voce.getProdotto(), quantita);
            utenti.salva(cliente);
            return vista(carrello);
        });
    }

    public VistaCarrello modificaQuantita(Long clienteId, Long rigaCarrelloId, int quantitaDesiderata) {
        return transazioni.inTransazione(() -> {
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
                ProdottoCatalogo voce = voceDi(riga.getProdotto());
                if (voce == null || !voce.disponibilitaSufficiente(quantitaDesiderata)) {
                    throw new EccezioneValidazione("Quantità superiore alla disponibilità.");
                }
                riga.impostaQuantita(quantitaDesiderata);
            } else {
                carrello.rimuoviRiga(riga);
            }
            utenti.salva(cliente);
            return vista(carrello);
        });
    }

    public VistaCarrello carrello(Long clienteId) {
        return transazioni.inTransazione(() -> vista(clienteValido(clienteId).getCarrello()));
    }

    private Cliente clienteValido(Long clienteId) {
        Utente utente = utenti.perId(clienteId)
                .orElseThrow(() -> new EccezioneValidazione("Utente non trovato."));
        if (!(utente instanceof Cliente cliente)) {
            throw new EccezioneValidazione("Operazione consentita solo ai clienti.");
        }
        return cliente;
    }

    private ProdottoCatalogo voceDi(Prodotto prodotto) {
        return catalogo.vocePerProdotto(prodotto.getId()).orElse(null);
    }

    private VistaCarrello vista(Carrello carrello) {
        if (carrello == null) {
            return new VistaCarrello(null, List.of(), BigDecimal.ZERO);
        }
        List<VistaRigaCarrello> righe = new ArrayList<>();
        BigDecimal totale = BigDecimal.ZERO;
        for (RigaCarrello riga : carrello.getRighe()) {
            ProdottoCatalogo voce = voceDi(riga.getProdotto());
            BigDecimal prezzo = voce == null ? BigDecimal.ZERO : voce.getPrezzoAttuale();
            int disponibile = voce == null ? 0 : voce.getQuantitaMagazzino();
            righe.add(new VistaRigaCarrello(riga.getId(), riga.getProdotto().getId(),
                    riga.getProdotto().getNome(), prezzo, riga.getQuantita(), disponibile));
            totale = totale.add(prezzo.multiply(BigDecimal.valueOf(riga.getQuantita())));
        }
        return new VistaCarrello(carrello.getId(), righe, totale);
    }
}
