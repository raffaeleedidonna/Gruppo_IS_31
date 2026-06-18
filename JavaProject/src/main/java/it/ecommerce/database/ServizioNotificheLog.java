package it.ecommerce.database;

import java.util.logging.Logger;

import it.ecommerce.entity.servizi.MessaggioNotifica;
import it.ecommerce.entity.servizi.ServizioNotifiche;

final class ServizioNotificheLog implements ServizioNotifiche {

    private static final Logger LOG = Logger.getLogger(ServizioNotificheLog.class.getName());

    @Override
    public void invia(MessaggioNotifica messaggio) {
        try {
            LOG.info(() -> "Notifica a " + messaggio.destinatario() + ": " + messaggio.testo());
        } catch (RuntimeException erroreInvioIgnorato) {
        }
    }
}
