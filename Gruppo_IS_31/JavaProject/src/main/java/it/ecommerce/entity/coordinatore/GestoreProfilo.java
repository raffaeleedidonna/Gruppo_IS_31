package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.UtenteRepository;

public class GestoreProfilo {

    private final GestoreTransazioni transazioni;
    private final UtenteRepository utenti;

    public GestoreProfilo(GestoreTransazioni transazioni, UtenteRepository utenti) {
        this.transazioni = transazioni;
        this.utenti = utenti;
    }

    public Utente mostra(Long utenteId) {
        return transazioni.inTransazione(() -> utenteValido(utenteId));
    }

    public Utente aggiorna(Long utenteId, String nome, String cognome, String indirizzoSpedizionePrincipale,
                           String immagineProfilo) {
        return transazioni.inTransazione(() -> {
            if (vuoto(nome) || vuoto(cognome)) {
                throw new EccezioneValidazione("Nome e cognome sono obbligatori.");
            }
            Utente utente = utenteValido(utenteId);
            Profilo profilo = utente.getProfilo();
            if (profilo == null) {
                throw new EccezioneValidazione("Profilo non disponibile.");
            }
            profilo.aggiorna(nome, cognome, indirizzoSpedizionePrincipale, immagineProfilo);
            utenti.salva(utente);
            return utente;
        });
    }

    private Utente utenteValido(Long utenteId) {
        return utenti.perId(utenteId)
                .orElseThrow(() -> new EccezioneValidazione("Utente non trovato."));
    }

    private boolean vuoto(String valore) {
        return valore == null || valore.isBlank();
    }
}
