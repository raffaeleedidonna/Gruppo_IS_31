public class Carrello {

	/**
	 * 
	 * @param prodotto
	 * @param quantita
	 */

	List<RigaCarrello> righe;

	public void aggiungi(int prodotto, int quantita) {
		// TODO - implement Carrello.aggiungi
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param prodotto
	 */
	public int quantitaProdotto(int prodotto) {
		// TODO - implement Carrello.quantitaProdotto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param rigaId
	 */
	public RigaCarrello rigaPerId(int rigaId) {
		// TODO - implement Carrello.rigaPerId
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param riga
	 */
	public void rimuoviRiga(int riga) {
		// TODO - implement Carrello.rimuoviRiga
		throw new UnsupportedOperationException();
	}

	public void svuota() {
		// TODO - implement Carrello.svuota
		throw new UnsupportedOperationException();
	}

	public boolean isVuoto() {
		// TODO - implement Carrello.isVuoto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param prodotto
	 */
	public void aggiungiProdotto(int prodotto) {
		// TODO - implement Carrello.aggiungiProdotto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param prodotto
	 */
	public void rimuoviProdotto(int prodotto) {
		// TODO - implement Carrello.rimuoviProdotto
		throw new UnsupportedOperationException();
	}

	public void calcolaTotale() {
		// TODO - implement Carrello.calcolaTotale
		throw new UnsupportedOperationException();
	}

	public void svuotaCarrello() {
		// TODO - implement Carrello.svuotaCarrello
		throw new UnsupportedOperationException();
	}


	public boolean riversaIn(Ordine ordine){

		for (RigaCarrello riga : righe){

			Prodotto prodotto,  = riga.getProdotto();

			ordine.aggiungiRiga(prodotto);

			prodotto.scarica(quantita);


		}
	}

}