package control;

public class GestoreNotifiche {

	// Questa variabile realizza il cuore del pattern Singleton
	private static GestoreNotifiche instance;

	private ServizioNotifiche servizio;

	private GestoreNotifiche() {}

	public static GestoreNotifiche getInstance() {
		if (instance == null) {
			instance = new GestoreNotifiche();
		}
		return instance;
	}

	public void setServizio(ServizioNotifiche servizio) {
		this.servizio = servizio;
	}

	public void invia(String destinatario, String oggetto, String testo) {
		// se il main non ha configurato un servizio, non si notifica
		if (servizio != null) {
			servizio.invia(destinatario, oggetto, testo);
		}
	}
}
