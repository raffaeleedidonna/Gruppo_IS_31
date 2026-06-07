package it.ecommerce.boundary;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import it.ecommerce.control.FabbricaControllori;
import it.ecommerce.control.dto.UtenteDTO;

public class HomeAmministratoreView extends JFrame {

    private final FabbricaControllori fabbrica;

    public HomeAmministratoreView(FabbricaControllori fabbrica, UtenteDTO utente) {
        super("Amministrazione - E-commerce");
        this.fabbrica = fabbrica;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 580);
        setLocationRelativeTo(null);
        setJMenuBar(creaMenuSessione());
        JTabbedPane schede = new JTabbedPane();
        schede.addTab("Prodotti", new GestioneProdottiView(fabbrica.gestioneProdotti()));
        schede.addTab("Ordini", new ElencoOrdiniView(fabbrica.gestioneOrdini()));
        schede.addTab("Monitoraggio", new MonitoraggioView(fabbrica.monitoraggio()));
        schede.addTab("Profilo", new ProfiloView(fabbrica.profilo(), utente.id()));
        add(schede);
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
