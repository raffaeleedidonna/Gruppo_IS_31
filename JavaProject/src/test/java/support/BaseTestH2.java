package support;

import database.JpaUtil;
import entity.Amministratore;
import entity.Categoria;
import entity.Cliente;
import entity.Prodotto;
import jakarta.persistence.EntityManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTestH2 {

	private static final String[] ENTITA_DA_RIPULIRE = {
		"RigaOrdine", "Ordine", "RigaCarrello", "Carrello", "Prodotto", "Categoria", "Utente"
	};

	@BeforeEach
	void ripulisciDatabase() {
		EntityManager em = JpaUtil.getInstance().getEntityManager();
		try {
			em.getTransaction().begin();
			for (String entita : ENTITA_DA_RIPULIRE) {
				em.createQuery("DELETE FROM " + entita).executeUpdate();
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	protected Categoria creaCategoria(String nome) {
		return salva(new Categoria(nome));
	}

	protected Prodotto creaProdotto(String nome, double prezzo, long quantitaMagazzino, boolean disponibile) {
		return creaProdotto(nome, prezzo, quantitaMagazzino, disponibile, creaCategoria("Categoria " + nome));
	}

	protected Prodotto creaProdotto(String nome, double prezzo, long quantitaMagazzino, boolean disponibile, Categoria categoria) {
		return salva(new Prodotto(nome, "Descrizione di " + nome, prezzo, quantitaMagazzino, disponibile, false, categoria));
	}

	protected Cliente creaCliente(String email, String password) {
		return salva(new Cliente(email, hash(password), "Mario", "Rossi", "Via Roma 1", null));
	}

	protected Amministratore creaAmministratore(String email, String password) {
		return salva(new Amministratore(email, hash(password), "Anna", "Bianchi", "Via Milano 2", null));
	}

	protected String hash(String password) {
		return DigestUtils.sha256Hex(password);
	}

	protected <T> T salva(T oggetto) {
		EntityManager em = JpaUtil.getInstance().getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(oggetto);
			em.getTransaction().commit();
			return oggetto;
		} finally {
			em.close();
		}
	}

	protected void rendiNonDisponibile(long idProdotto) {
		EntityManager em = JpaUtil.getInstance().getEntityManager();
		try {
			em.getTransaction().begin();
			em.createQuery("UPDATE Prodotto p SET p.disponibile = false WHERE p.id = :id")
				.setParameter("id", idProdotto)
				.executeUpdate();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	protected <T> T ricarica(Class<T> classe, long id) {
		EntityManager em = JpaUtil.getInstance().getEntityManager();
		try {
			return em.find(classe, id);
		} finally {
			em.close();
		}
	}
}
