package it.ecommerce.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.Utente;
import it.ecommerce.entity.coordinatore.GestoreProfilo;
import it.ecommerce.entity.persistenza.UtenteRepository;

class ProfiloControllerImplTest {

    private Cliente cliente() {
        return new Cliente("mario@x.it", "pw", new Profilo("Mario", "Rossi", "Via Roma 1", null));
    }

    private ProfiloController controllerCon(Cliente cliente) {
        return new ProfiloControllerImpl(new GestoreProfilo(new TransazioniDirette(), new UtenteFinto(cliente)));
    }

    @Test
    void mostraProfiloRestituisceIdentitaEContatti() {
        ProfiloDTO profilo = controllerCon(cliente()).mostraProfilo(1L);

        assertEquals("Mario", profilo.nome());
        assertEquals("Rossi", profilo.cognome());
        assertEquals("mario@x.it", profilo.email());
        assertEquals("Via Roma 1", profilo.indirizzoSpedizionePrincipale());
    }

    @Test
    void aggiornaProfiloModificaIdentitaIndirizzoEFoto() {
        Cliente cliente = cliente();
        EsitoDTO<ProfiloDTO> esito = controllerCon(cliente).aggiornaProfilo(1L,
                new ProfiloDTO("Luigi", "Verdi", "mario@x.it", "Via Milano 2", "aW1n"));

        assertTrue(esito.successo());
        assertEquals("Luigi", cliente.getProfilo().getNome());
        assertEquals("Verdi", cliente.getProfilo().getCognome());
        assertEquals("Via Milano 2", cliente.getProfilo().getIndirizzoSpedizionePrincipale());
        assertEquals("aW1n", cliente.getProfilo().getImmagineProfilo());
        assertEquals("Luigi", esito.dato().nome());
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
}
