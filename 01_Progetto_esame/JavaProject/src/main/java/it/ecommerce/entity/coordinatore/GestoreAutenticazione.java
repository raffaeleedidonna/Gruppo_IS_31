package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.EccezioneValidazione;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.RegistroPersistenza;
import it.ecommerce.entity.persistenza.UtenteDAO;

public class GestoreAutenticazione {

    private final FornitorePersistenza fornitore;

    public GestoreAutenticazione() {
        this(RegistroPersistenza.fornitore());
    }

    public GestoreAutenticazione(FornitorePersistenza fornitore) {
        this.fornitore = fornitore;
    }

    public Utente autentica(String email, String password) {
        return fornitore.inTransazione(() -> {
            Utente utente = fornitore.utenteDAO().perEmail(email)
                    .orElseThrow(() -> new EccezioneValidazione("Credenziali non valide."));
            if (!utente.passwordCorrisponde(password)) {
                throw new EccezioneValidazione("Credenziali non valide.");
            }
            return utente;
        });
    }

    public Utente registra(String nome, String cognome, String email, String password) {
        return fornitore.inTransazione(() -> {
            validaCampi(nome, cognome, email, password);
            UtenteDAO utenti = fornitore.utenteDAO();
            if (utenti.esisteEmail(email)) {
                throw new EccezioneValidazione("Email già associata ad un utente.");
            }
            return utenti.salva(new Cliente(email, password, nome, cognome, new Profilo()));
        });
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
