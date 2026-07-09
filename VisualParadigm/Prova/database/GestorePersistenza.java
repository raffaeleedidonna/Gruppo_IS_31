package database;

public class GestorePersistenza {

	/**
	 * Salva nel database un oggetto persistente.
	 * 
	 * Il parametro è di tipo Object perché il gestore della persistenza
	 * deve rimanere generico: non deve conoscere direttamente le classi
	 * specifiche del dominio, come Proprietario o Imbarcazione.
	 * 
	 * L'oggetto passato deve però essere una Entity, cioè una classe
	 * annotata con @Entity.
	 * @param oggetto
	 */
	public boolean salva(Object oggetto) {
		// TODO - implement GestorePersistenza.salva
		throw new UnsupportedOperationException();
	}

	/**
	 * Salva più oggetti nella stessa transazione.
	 * 
	 * Questo metodo è utile quando vogliamo rendere persistenti oggetti
	 * collegati tra loro, ad esempio un Proprietario e una o più Imbarcazione.
	 * 
	 * Usare una sola transazione è importante: o vengono salvati tutti
	 * gli oggetti, oppure, in caso di errore, non viene salvato nessuno.
	 * @param oggetti
	 */
	public boolean salvaTutti(Object... oggetti) {
		// TODO - implement GestorePersistenza.salvaTutti
		throw new UnsupportedOperationException();
	}

	/**
	 * Cerca un oggetto persistente a partire dalla sua classe e dal suo id.
	 * 
	 * Il metodo è generico: può essere usato con qualunque Entity.
	 * 
	 * Esempio:
	 * Proprietario p = trovaPerId(Proprietario.class, 1L);
	 * @param classe
	 * @param id
	 */
	public <T>T trovaPerId(Class<T> classe, Long id) {
		// TODO - implement GestorePersistenza.trovaPerId
		throw new UnsupportedOperationException();
	}

	/**
	 * Cerca tutti gli oggetti persistenti di una certa classe
	 * per cui un campo ha un determinato valore.
	 * @param classe
	 * @param nomeCampo
	 * @param valore
	 */
	public <T>java.util.List<T> cercaPerCampo(Class<T> classe, String nomeCampo, Object valore) {
		// TODO - implement GestorePersistenza.cercaPerCampo
		throw new UnsupportedOperationException();
	}

	/**
	 * Cerca tutti gli oggetti persistenti che soddisfano un insieme di condizioni.
	 * 
	 * La query JPQL viene costruita nel livello database.
	 * @param classe
	 * @param campi
	 */
	public <T>java.util.List<T> cercaPerCampi(Class<T> classe, java.util.Map<String, Object> campi) {
		// TODO - implement GestorePersistenza.cercaPerCampi
		throw new UnsupportedOperationException();
	}

	/**
	 * Cerca il primo oggetto persistente che soddisfa un insieme di condizioni.
	 * 
	 * Se non trova nessun risultato, restituisce null.
	 * @param classe
	 * @param campi
	 */
	public <T>T cercaPrimoPerCampi(Class<T> classe, java.util.Map<String, Object> campi) {
		// TODO - implement GestorePersistenza.cercaPrimoPerCampi
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param oggetto
	 */
	public <T>T aggiorna(T oggetto) {
		// TODO - implement GestorePersistenza.aggiorna
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param classe
	 * @param id
	 */
	public <T>boolean elimina(Class<T> classe, Long id) {
		// TODO - implement GestorePersistenza.elimina
		throw new UnsupportedOperationException();
	}

}