package it.ecommerce.database;

import org.hibernate.SessionFactory;

import it.ecommerce.entity.persistenza.CategoriaDAO;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.NotificaDAO;
import it.ecommerce.entity.persistenza.OrdineDAO;
import it.ecommerce.entity.persistenza.ProdottoDAO;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;
import it.ecommerce.entity.persistenza.UtenteDAO;

public final class FornitorePersistenzaHibernate implements FornitorePersistenza {

    private final SessionFactory sessionFactory;
    private final ProdottoDAO prodottoDAO;
    private final CategoriaDAO categoriaDAO;
    private final UtenteDAO utenteDAO;
    private final OrdineDAO ordineDAO;
    private final NotificaDAO notificaDAO;

    public FornitorePersistenzaHibernate() {
        this.sessionFactory = SessionFactoryProvider.sessionFactory();
        this.prodottoDAO = new ProdottoDAOHibernate(sessionFactory);
        this.categoriaDAO = new CategoriaDAOHibernate(sessionFactory);
        this.utenteDAO = new UtenteDAOHibernate(sessionFactory);
        this.ordineDAO = new OrdineDAOHibernate(sessionFactory);
        this.notificaDAO = new NotificaDAOHibernate(sessionFactory);
        SeedSviluppo.popolaSeNecessario(this);
    }

    @Override
    public ProdottoDAO prodottoDAO() {
        return prodottoDAO;
    }

    @Override
    public CategoriaDAO categoriaDAO() {
        return categoriaDAO;
    }

    @Override
    public UtenteDAO utenteDAO() {
        return utenteDAO;
    }

    @Override
    public OrdineDAO ordineDAO() {
        return ordineDAO;
    }

    @Override
    public NotificaDAO notificaDAO() {
        return notificaDAO;
    }

    @Override
    public UnitaDiLavoro apreUnitaDiLavoro() {
        return new UnitaDiLavoroHibernate(sessionFactory);
    }
}
