package boundary.notifiche;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import control.ServizioNotifiche;

public class NotificatoreLog implements ServizioNotifiche {

	public NotificatoreLog() {}

	@Override
	public void invia(String destinatario, String oggetto, String testo) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    System.out.println(String.format(
        "[NOTIFICA %s]%n  A: %s%n  Oggetto: %s%n  Messaggio: %s%n",
        timestamp, destinatario, oggetto, testo
    ));
	}
}
