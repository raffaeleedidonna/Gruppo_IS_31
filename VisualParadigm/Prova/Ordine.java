public class Ordine {

	private int dataCreazione;
	private int totaleComplessivo;
	private int indirizzoSpedizione;
	private int stato;
	private StatoOrdine state;

	/**
	 * 
	 * @param prodotto
	 * @param quantita
	 * @param prezzoDiAcquisto
	 */
	public void aggiungiRiga(int prodotto, int quantita, int prezzoDiAcquisto) {
		// TODO - implement Ordine.aggiungiRiga
		throw new UnsupportedOperationException();
	}

	public void assicuraNonVuoto() {
		// TODO - implement Ordine.assicuraNonVuoto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nuovoStato
	 */
	public void cambiaStato(int nuovoStato) {
		// TODO - implement Ordine.cambiaStato
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param clienteId
	 */
	public boolean appartieneA(int clienteId) {
		// TODO - implement Ordine.appartieneA
		throw new UnsupportedOperationException();
	}

	public void calcolaTotale() {
		// TODO - implement Ordine.calcolaTotale
		throw new UnsupportedOperationException();
	}

	public void modificaStato() {
		// TODO - implement Ordine.modificaStato
		throw new UnsupportedOperationException();
	}

}