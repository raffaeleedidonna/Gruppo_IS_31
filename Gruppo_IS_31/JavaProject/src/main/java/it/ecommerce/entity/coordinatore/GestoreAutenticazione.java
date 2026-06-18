package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.UtenteRepository;
import it.ecommerce.entity.servizi.MessaggioNotifica;
import it.ecommerce.entity.servizi.ServizioNotifiche;

public class GestoreAutenticazione {

    private final GestoreTransazioni transazioni;
    private final UtenteRepository utenti;
    private final ServizioNotifiche notifiche;

    public GestoreAutenticazione(GestoreTransazioni transazioni, UtenteRepository utenti,
                                 ServizioNotifiche notifiche) {
        this.transazioni = transazioni;
        this.utenti = utenti;
        this.notifiche = notifiche;
    }

    public Utente autentica(String email, String password) {
        return transazioni.inTransazione(() -> {
            Utente utente = utenti.perEmail(email)
                    .orElseThrow(() -> new EccezioneValidazione("Credenziali non valide."));
            if (!utente.passwordCorrisponde(password)) {
                throw new EccezioneValidazione("Credenziali non valide.");
            }
            return utente;
        });
    }

    public Utente registra(String nome, String cognome, String email, String password,
                           String indirizzoSpedizione, String immagineProfilo) {
        Utente registrato = transazioni.inTransazione(() -> {
            validaCampi(nome, cognome, email, password);
            if (utenti.esisteEmail(email)) {
                throw new EccezioneValidazione("Email già associata ad un utente.");
            }
            return utenti.salva(new Cliente(email, password,
                    new Profilo(nome, cognome, indirizzoSpedizione, immagineProfilo)));
        });
        notifiche.invia(new MessaggioNotifica(registrato.getEmail(),
                "Registrazione completata. Benvenuto su Ecommerce."));
        return registrato;
    }

    private void validaCampi(String nome, String cognome, String email, String password) {
        if (vuoto(nome) || vuoto(cognome) || vuoto(email) || vuoto(password)) {
            throw new EccezioneValidazione("Tutti i campi sono obbligatori.");
        }
        if (!email.contains("@")) {
            throw new EccezioneValidazione("Email non valida.");
        }
    }

    private boolean vuoto(String valore) {
        return valore == null || valore.isBlank();
    }
}
