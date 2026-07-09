package database;

public class JpaUtil {

	/**
	 * EntityManagerFactory condivisa.
	 * 
	 * La factory viene creata una sola volta, perché è un oggetto
	 * costoso da inizializzare: legge la persistence unit dal file
	 * persistence.xml e prepara Hibernate per comunicare con il database.
	 */
	private jakarta.persistence.EntityManagerFactory emf;
	/**
	 * Istanza unica di JpaUtil.
	 * 
	 * Questa variabile realizza il cuore del pattern Singleton:
	 * la classe mantiene internamente l'unica istanza disponibile.
	 */
	private static JpaUtil instance;

	public JpaUtil getInstance() {
		return this.instance;
	}

	/**
	 * Costruttore privato.
	 * 
	 * Questo impedisce al resto dell'applicazione di creare oggetti
	 * JpaUtil usando new JpaUtil().
	 * 
	 * L'unico modo per ottenere JpaUtil sarà passare dal metodo
	 * statico getInstance().
	 */
	private JpaUtil() {
		// TODO - implement JpaUtil.JpaUtil
		throw new UnsupportedOperationException();
	}

	/**
	 * Crea un nuovo EntityManager.
	 * 
	 * Attenzione: l'EntityManager non è Singleton.
	 * Ogni operazione di persistenza deve usare un proprio EntityManager,
	 * perché l'EntityManager mantiene lo stato della singola sessione
	 * di lavoro con il database.
	 */
	public jakarta.persistence.EntityManager getEntityManager() {
		// TODO - implement JpaUtil.getEntityManager
		throw new UnsupportedOperationException();
	}

	/**
	 * Chiude la EntityManagerFactory.
	 * 
	 * Questo metodo va chiamato alla fine dell'applicazione,
	 * quando non sono più necessarie operazioni di persistenza.
	 */
	public void chiudi() {
		// TODO - implement JpaUtil.chiudi
		throw new UnsupportedOperationException();
	}

}