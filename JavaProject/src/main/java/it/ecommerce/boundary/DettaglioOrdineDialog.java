package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.Window;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.RigaOrdineDTO;

public class DettaglioOrdineDialog extends JDialog {

    public DettaglioOrdineDialog(Window proprietario, OrdineDTO ordine) {
        super(proprietario, "Dettaglio ordine", ModalityType.APPLICATION_MODAL);
        setContentPane(creaContenuto(ordine));
        pack();
        setLocationRelativeTo(proprietario);
    }

    private JPanel creaContenuto(OrdineDTO ordine) {
        JPanel intestazione = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        intestazione.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
        intestazione.add(new JLabel("Ordine numero: " + ordine.id()));
        intestazione.add(new JLabel("Data: " + formatta(ordine)));
        intestazione.add(new JLabel("Stato: " + ordine.stato()));
        intestazione.add(new JLabel("Indirizzo di spedizione: "
                + (ordine.indirizzoSpedizione() == null ? "non specificato" : ordine.indirizzoSpedizione())));
        intestazione.add(new JLabel("Totale: € " + ordine.totaleComplessivo()));

        JTable tabella = new JTable(new ModelloRigheOrdine(ordine.righe()));

        JButton chiudi = new JButton("Chiudi");
        chiudi.addActionListener(evento -> dispose());
        JPanel pulsanti = new JPanel();
        pulsanti.add(chiudi);

        JPanel contenuto = new JPanel(new BorderLayout(8, 8));
        contenuto.add(intestazione, BorderLayout.NORTH);
        contenuto.add(new JScrollPane(tabella), BorderLayout.CENTER);
        contenuto.add(pulsanti, BorderLayout.SOUTH);
        return contenuto;
    }

    private String formatta(OrdineDTO ordine) {
        if (ordine.dataCreazione() == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(ordine.dataCreazione());
    }

    private static final class ModelloRigheOrdine extends AbstractTableModel {

        private final String[] colonne = {"Prodotto", "Quantità", "Prezzo unitario", "Subtotale"};
        private final List<RigaOrdineDTO> righe;

        ModelloRigheOrdine(List<RigaOrdineDTO> righe) {
            this.righe = righe;
        }

        @Override
        public int getRowCount() {
            return righe.size();
        }

        @Override
        public int getColumnCount() {
            return colonne.length;
        }

        @Override
        public String getColumnName(int colonna) {
            return colonne[colonna];
        }

        @Override
        public Object getValueAt(int riga, int colonna) {
            RigaOrdineDTO elemento = righe.get(riga);
            return switch (colonna) {
                case 0 -> elemento.prodottoNome();
                case 1 -> elemento.quantitaAcquistata();
                case 2 -> elemento.prezzoDiAcquisto();
                case 3 -> elemento.prezzoDiAcquisto()
                        .multiply(java.math.BigDecimal.valueOf(elemento.quantitaAcquistata()));
                default -> "";
            };
        }
    }
}
