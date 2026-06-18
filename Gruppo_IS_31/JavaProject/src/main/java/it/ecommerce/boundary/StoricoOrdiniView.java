package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import it.ecommerce.control.OrdineClienteController;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;

public class StoricoOrdiniView extends JPanel {

    private final OrdineClienteController controller;
    private final Long clienteId;
    private final ModelloTabellaOrdini modello = new ModelloTabellaOrdini();
    private final JTable tabella = new JTable(modello);
    private final JLabel etichettaStato = new JLabel(" ");

    public StoricoOrdiniView(OrdineClienteController controller, Long clienteId) {
        super(new BorderLayout());
        this.controller = controller;
        this.clienteId = clienteId;
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(creaBarraStrumenti(), BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        etichettaStato.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(etichettaStato, BorderLayout.SOUTH);
        ricarica();
    }

    public void ricarica() {
        EsitoDTO<List<OrdineDTO>> esito = controller.storicoOrdini(clienteId);
        if (esito.successo()) {
            modello.imposta(esito.dato());
            etichettaStato.setText(" ");
        } else {
            modello.imposta(List.of());
            etichettaStato.setText(esito.messaggio());
        }
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        JButton dettaglio = new JButton("Dettaglio");
        JButton aggiorna = new JButton("Aggiorna");
        dettaglio.addActionListener(evento -> mostraDettaglio());
        aggiorna.addActionListener(evento -> ricarica());
        barra.add(dettaglio);
        barra.add(aggiorna);
        return barra;
    }

    private void mostraDettaglio() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un ordine.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OrdineDTO selezionato = modello.ordineA(tabella.convertRowIndexToModel(riga));
        OrdineDTO dettaglio = controller.dettaglioOrdine(clienteId, selezionato.id());
        if (dettaglio == null) {
            JOptionPane.showMessageDialog(this, "Ordine non disponibile.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new DettaglioOrdineDialog(SwingUtilities.getWindowAncestor(this), dettaglio).setVisible(true);
    }
}
