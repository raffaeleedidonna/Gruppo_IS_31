package it.ecommerce.boundary;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import it.ecommerce.control.CarrelloController;
import it.ecommerce.control.FabbricaControllori;
import it.ecommerce.control.OrdineClienteController;
import it.ecommerce.control.dto.UtenteDTO;

public class HomeClienteView extends JFrame {

    private final FabbricaControllori fabbrica;

    public HomeClienteView(FabbricaControllori fabbrica, UtenteDTO utente) {
        super("Area Cliente - E-commerce");
        this.fabbrica = fabbrica;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 580);
        setLocationRelativeTo(null);
        setJMenuBar(creaMenuSessione());
        Long clienteId = utente.id();
        CarrelloController controllerCarrello = fabbrica.carrello();
        OrdineClienteController controllerOrdini = fabbrica.ordineCliente();
        StoricoOrdiniView vistaOrdini = new StoricoOrdiniView(controllerOrdini, clienteId);
        CarrelloView vistaCarrello = new CarrelloView(controllerCarrello, controllerOrdini,
                clienteId, vistaOrdini::ricarica);
        CatalogoView vistaCatalogo = new CatalogoView(fabbrica.catalogo(), controllerCarrello,
                clienteId, vistaCarrello::ricarica);

        JTabbedPane schede = new JTabbedPane();
        schede.addTab("Home", creaBenvenuto(utente));
        schede.addTab("Catalogo", vistaCatalogo);
        schede.addTab("Carrello", vistaCarrello);
        schede.addTab("Ordini", vistaOrdini);
        schede.addTab("Profilo", new ProfiloView(fabbrica.profilo(), clienteId));
        add(schede);
    }

    private JPanel creaBenvenuto(UtenteDTO utente) {
        JPanel pannello = new JPanel(new BorderLayout());
        pannello.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        pannello.add(new JLabel("Benvenuto, " + utente.nome() + " " + utente.cognome() + "."),
                BorderLayout.NORTH);
        return pannello;
    }

    private JMenuBar creaMenuSessione() {
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Sessione");
        JMenuItem esci = new JMenuItem("Esci");
        esci.addActionListener(evento -> {
            dispose();
            new LoginView(fabbrica).setVisible(true);
        });
        menu.add(esci);
        barra.add(menu);
        return barra;
    }
}
