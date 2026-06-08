package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.RegistrazioneDTO;
import it.ecommerce.control.dto.UtenteDTO;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreAutenticazione;
import it.ecommerce.entity.persistenza.UtenteRepository;

class AutenticazioneControllerImplTest {

    private AutenticazioneController controllerCon(UtenteFinto utenti) {
        return new AutenticazioneControllerImpl(new GestoreAutenticazione(new TransazioniDirette(), utenti));
    }

    @Test
    void registrazioneCreaClienteConProfiloPopolato() {
        UtenteFinto utenti = new UtenteFinto(false);
        EsitoDTO<UtenteDTO> esito = controllerCon(utenti).registra(new RegistrazioneDTO(
                "Mario", "Rossi", "mario@x.it", "pw", "Via Roma 1", "Zm90bw=="));

        assertTrue(esito.successo());
        Cliente cliente = assertInstanceOf(Cliente.class, utenti.salvato());
        assertEquals("mario@x.it", cliente.getEmail());
        assertEquals("Mario", cliente.getProfilo().getNome());
        assertEquals("Rossi", cliente.getProfilo().getCognome());
        assertEquals("Via Roma 1", cliente.getProfilo().getIndirizzoSpedizionePrincipale());
        assertEquals("Zm90bw==", cliente.getProfilo().getImmagineProfilo());
        assertEquals("Mario", esito.dato().nome());
    }

    @Test
    void registrazioneConEmailDuplicataFallisce() {
        UtenteFinto utenti = new UtenteFinto(true);
        EsitoDTO<UtenteDTO> esito = controllerCon(utenti).registra(new RegistrazioneDTO(
                "Mario", "Rossi", "mario@x.it", "pw", null, null));

        assertFalse(esito.successo());
        assertTrue(esito.messaggio().toLowerCase().contains("email"));
    }

    private static final class UtenteFinto implements UtenteRepository {

        private final boolean emailEsistente;
        private Utente salvato;

        UtenteFinto(boolean emailEsistente) {
            this.emailEsistente = emailEsistente;
        }

        Utente salvato() {
            return salvato;
        }

        @Override
        public Utente salva(Utente utente) {
            salvato = utente;
            return utente;
        }

        @Override
        public Optional<Utente> perEmail(String email) {
            return Optional.empty();
        }

        @Override
        public Optional<Utente> perId(Long id) {
            return Optional.empty();
        }

        @Override
        public boolean esisteEmail(String email) {
            return emailEsistente;
        }

        @Override
        public long contaClienti() {
            return 0;
        }
    }
}
