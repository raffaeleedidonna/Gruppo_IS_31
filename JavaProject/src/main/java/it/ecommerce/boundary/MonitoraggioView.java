package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.ecommerce.control.MonitoraggioController;
import it.ecommerce.control.dto.StatistichePiattaformaDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;

public class MonitoraggioView extends JPanel {

    private final MonitoraggioController controller;
    private final JLabel valoreOrdini = new JLabel();
    private final JLabel valoreProdotti = new JLabel();
    private final JLabel valoreClienti = new JLabel();
    private final JLabel valoreFatturato = new JLabel();
    private final DefaultTableModel modelloStati = new DefaultTableModel(
            new Object[] {"Stato", "Numero ordini"}, 0) {
        @Override
        public boolean isCellEditable(int riga, int colonna) {
            return false;
        }
    };
    private final ModelloTabellaProdotti modelloPiuVenduti = new ModelloTabellaProdotti();

    public MonitoraggioView(MonitoraggioController controller) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(creaRiepilogo(), BorderLayout.NORTH);
        add(creaTabelle(), BorderLayout.CENTER);
        add(creaPulsanti(), BorderLayout.SOUTH);
        ricarica();
    }

    private JComponent creaRiepilogo() {
        JPanel pannello = new JPanel(new GridLayout(0, 2, 8, 8));
        pannello.add(new JLabel("Numero ordini:"));
        pannello.add(valoreOrdini);
        pannello.add(new JLabel("Prodotti a catalogo:"));
        pannello.add(valoreProdotti);
        pannello.add(new JLabel("Clienti registrati:"));
        pannello.add(valoreClienti);
        pannello.add(new JLabel("Fatturato totale:"));
        pannello.add(valoreFatturato);
        return pannello;
    }

    private JComponent creaTabelle() {
        JPanel pannello = new JPanel(new GridLayout(1, 2, 10, 10));
        pannello.add(sezione("Ordini per stato", new JTable(modelloStati)));
        pannello.add(sezione("Prodotti più venduti", new JTable(modelloPiuVenduti)));
        return pannello;
    }

    private JComponent sezione(String titolo, JTable tabella) {
        JPanel pannello = new JPanel(new BorderLayout(4, 4));
        pannello.add(new JLabel(titolo), BorderLayout.NORTH);
        pannello.add(new JScrollPane(tabella), BorderLayout.CENTER);
        return pannello;
    }

    private JComponent creaPulsanti() {
        JButton aggiorna = new JButton("Aggiorna");
        aggiorna.addActionListener(evento -> ricarica());
        JPanel pannello = new JPanel();
        pannello.add(aggiorna);
        return pannello;
    }

    private void ricarica() {
        StatistichePiattaformaDTO statistiche = controller.statistiche();
        valoreOrdini.setText(String.valueOf(statistiche.numeroOrdini()));
        valoreProdotti.setText(String.valueOf(statistiche.numeroProdotti()));
        valoreClienti.setText(String.valueOf(statistiche.numeroClienti()));
        valoreFatturato.setText("€ " + statistiche.fatturatoTotale());
        modelloStati.setRowCount(0);
        for (Map.Entry<StatoOrdineDTO, Long> voce : statistiche.ordiniPerStato().entrySet()) {
            modelloStati.addRow(new Object[] {voce.getKey(), voce.getValue()});
        }
        modelloPiuVenduti.impostaProdotti(statistiche.prodottiPiuVenduti());
    }
}
