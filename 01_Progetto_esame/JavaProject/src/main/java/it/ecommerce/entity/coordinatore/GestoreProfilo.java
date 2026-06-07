package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;

public class GestoreProfilo {

    private final FornitorePersistenza fornitore;

    public GestoreProfilo() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreProfilo(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public Profilo mostra(Long utenteId) {
        return fornitore.inTransazione(() -> utenteValido(utenteId).getProfilo());
    }

    public Profilo aggiorna(Long utenteId, String datiAnagrafici, String indirizzoSpedizionePrincipale,
                            String immagineProfilo) {
        return fornitore.inTransazione(() -> {
            Utente utente = utenteValido(utenteId);
            Profilo profilo = utente.getProfilo();
            if (profilo == null) {
                throw new EccezioneValidazione("Profilo non disponibile.");
            }
            profilo.aggiorna(datiAnagrafici, indirizzoSpedizionePrincipale, immagineProfilo);
            fornitore.utenteDAO().salva(utente);
            return profilo;
        });
    }

    private Utente utenteValido(Long utenteId) {
        return fornitore.utenteDAO().perId(utenteId)
                .orElseThrow(() -> new EccezioneValidazione("Utente non trovato."));
    }
}
