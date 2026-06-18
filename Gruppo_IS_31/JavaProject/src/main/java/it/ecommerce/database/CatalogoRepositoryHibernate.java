package it.ecommerce.database;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.ProdottoCatalogo;
import it.ecommerce.entity.persistenza.CatalogoRepository;

final class CatalogoRepositoryHibernate implements CatalogoRepository {

    private final SessionFactory sessionFactory;

    CatalogoRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public ProdottoCatalogo salva(ProdottoCatalogo voce) {
        if (voce.getId() == null) {
            sessione().persist(voce);
            return voce;
        }
        return sessione().merge(voce);
    }

    @Override
    public void rimuovi(ProdottoCatalogo voce) {
        sessione().remove(voce);
    }

    @Override
    public Optional<ProdottoCatalogo> vocePerProdotto(Long prodottoId) {
        return sessione().createQuery(
                "from ProdottoCatalogo pc where pc.prodotto.id = :prodottoId", ProdottoCatalogo.class)
                .setParameter("prodottoId", prodottoId)
                .uniqueResultOptional();
    }

    @Override
    public List<ProdottoCatalogo> tutte() {
        return sessione().createQuery(
                "from ProdottoCatalogo pc order by pc.prodotto.nome", ProdottoCatalogo.class)
                .getResultList();
    }

    @Override
    public List<ProdottoCatalogo> inOfferta() {
        return sessione().createQuery(
                "from ProdottoCatalogo pc where pc.inOfferta = true order by pc.prodotto.nome",
                ProdottoCatalogo.class)
                .getResultList();
    }

    @Override
    public List<ProdottoCatalogo> cerca(String termine) {
        return sessione().createQuery(
                "from ProdottoCatalogo pc where lower(pc.prodotto.nome) like :termine "
                        + "order by pc.prodotto.nome",
                ProdottoCatalogo.class)
                .setParameter("termine", "%" + termine.toLowerCase() + "%")
                .getResultList();
    }

    @Override
    public boolean esisteProdottoPerNome(String nome) {
        Long conteggio = sessione().createQuery(
                "select count(pc) from ProdottoCatalogo pc where lower(pc.prodotto.nome) = :nome",
                Long.class)
                .setParameter("nome", nome.toLowerCase())
                .getSingleResult();
        return conteggio > 0;
    }

    @Override
    public long conta() {
        return sessione().createQuery("select count(pc) from ProdottoCatalogo pc", Long.class)
                .getSingleResult();
    }
}
