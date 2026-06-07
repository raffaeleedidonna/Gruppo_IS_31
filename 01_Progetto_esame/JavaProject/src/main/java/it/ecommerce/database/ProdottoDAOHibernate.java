package it.ecommerce.database;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.persistenza.ProdottoDAO;

final class ProdottoDAOHibernate implements ProdottoDAO {

    private final SessionFactory sessionFactory;

    ProdottoDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Prodotto salva(Prodotto prodotto) {
        if (prodotto.getId() == null) {
            sessione().persist(prodotto);
            return prodotto;
        }
        return sessione().merge(prodotto);
    }

    @Override
    public Optional<Prodotto> perId(Long id) {
        return Optional.ofNullable(sessione().find(Prodotto.class, id));
    }

    @Override
    public List<Prodotto> tuttiNelCatalogo() {
        return sessione().createQuery(
                "from Prodotto p where p.presenteNelCatalogo = true order by p.nome",
                Prodotto.class).getResultList();
    }

    @Override
    public List<Prodotto> inOfferta() {
        return sessione().createQuery(
                "from Prodotto p where p.presenteNelCatalogo = true and p.inOfferta = true order by p.nome",
                Prodotto.class).getResultList();
    }

    @Override
    public List<Prodotto> cerca(String termine) {
        return sessione().createQuery(
                "from Prodotto p where p.presenteNelCatalogo = true "
                        + "and lower(p.nome) like :termine order by p.nome",
                Prodotto.class)
                .setParameter("termine", "%" + termine.toLowerCase() + "%")
                .getResultList();
    }

    @Override
    public boolean esistePerNome(String nome) {
        Long conteggio = sessione().createQuery(
                "select count(p) from Prodotto p where p.presenteNelCatalogo = true "
                        + "and lower(p.nome) = :nome",
                Long.class)
                .setParameter("nome", nome.toLowerCase())
                .getSingleResult();
        return conteggio > 0;
    }

    @Override
    public List<Prodotto> perCategoria(Long categoriaId) {
        return sessione().createQuery(
                "from Prodotto p where p.presenteNelCatalogo = true "
                        + "and p.categoria.id = :categoriaId order by p.nome",
                Prodotto.class)
                .setParameter("categoriaId", categoriaId)
                .getResultList();
    }
}
