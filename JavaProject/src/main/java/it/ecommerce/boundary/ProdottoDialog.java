package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.ecommerce.control.dto.CategoriaDTO;
import it.ecommerce.control.dto.ProdottoDTO;

public class ProdottoDialog extends JDialog {

    private final JTextField campoNome = new JTextField();
    private final JTextArea campoDescrizione = new JTextArea(4, 20);
    private final JTextField campoPrezzo = new JTextField();
    private final JTextField campoQuantita = new JTextField();
    private final JComboBox<CategoriaDTO> campoCategoria;
    private final JCheckBox campoDisponibile = new JCheckBox("Disponibile");
    private final JCheckBox campoInOfferta = new JCheckBox("In offerta");

    private final Long idEsistente;
    private ProdottoDTO risultato;

    public ProdottoDialog(Window proprietario, List<CategoriaDTO> categorie, ProdottoDTO iniziale) {
        super(proprietario, iniziale == null ? "Nuovo prodotto" : "Modifica prodotto",
                ModalityType.APPLICATION_MODAL);
        this.campoCategoria = new JComboBox<>(categorie.toArray(new CategoriaDTO[0]));
        this.idEsistente = iniziale == null ? null : iniziale.id();
        configuraRenderizzatoreCategoria();
        if (iniziale != null) {
            precompila(iniziale);
        }
        setContentPane(creaContenuto());
        pack();
        setLocationRelativeTo(proprietario);
    }

    public Optional<ProdottoDTO> mostra() {
        setVisible(true);
        return Optional.ofNullable(risultato);
    }

    private void configuraRenderizzatoreCategoria() {
        campoCategoria.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> lista, Object valore,
                    int indice, boolean selezionato, boolean focus) {
                super.getListCellRendererComponent(lista, valore, indice, selezionato, focus);
                if (valore instanceof CategoriaDTO categoria) {
                    setText(categoria.nome());
                }
                return this;
            }
        });
    }

    private void precompila(ProdottoDTO iniziale) {
        campoNome.setText(iniziale.nome());
        campoDescrizione.setText(iniziale.descrizione());
        campoPrezzo.setText(iniziale.prezzoAttuale() == null ? "" : iniziale.prezzoAttuale().toPlainString());
        campoQuantita.setText(Integer.toString(iniziale.quantitaMagazzino()));
        campoDisponibile.setSelected(iniziale.disponibile());
        campoInOfferta.setSelected(iniziale.inOfferta());
        selezionaCategoria(iniziale.categoriaId());
    }

    private void selezionaCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return;
        }
        for (int indice = 0; indice < campoCategoria.getItemCount(); indice++) {
            if (categoriaId.equals(campoCategoria.getItemAt(indice).id())) {
                campoCategoria.setSelectedIndex(indice);
                return;
            }
        }
    }

    private JPanel creaContenuto() {
        JPanel campi = new JPanel(new GridLayout(0, 2, 8, 8));
        campi.add(new JLabel("Nome"));
        campi.add(campoNome);
        campi.add(new JLabel("Descrizione"));
        campi.add(new JScrollPane(campoDescrizione));
        campi.add(new JLabel("Prezzo"));
        campi.add(campoPrezzo);
        campi.add(new JLabel("Quantità in magazzino"));
        campi.add(campoQuantita);
        campi.add(new JLabel("Categoria"));
        campi.add(campoCategoria);
        campi.add(campoDisponibile);
        campi.add(campoInOfferta);

        JButton salva = new JButton("Salva");
        JButton annulla = new JButton("Annulla");
        salva.addActionListener(evento -> conferma());
        annulla.addActionListener(evento -> dispose());
        JPanel pulsanti = new JPanel();
        pulsanti.add(salva);
        pulsanti.add(annulla);

        JPanel contenuto = new JPanel(new BorderLayout(10, 10));
        contenuto.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenuto.add(campi, BorderLayout.CENTER);
        contenuto.add(pulsanti, BorderLayout.SOUTH);
        return contenuto;
    }

    private void conferma() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            erroreCampo("Il nome è obbligatorio.");
            return;
        }
        BigDecimal prezzo = leggiPrezzo();
        if (prezzo == null) {
            return;
        }
        Integer quantita = leggiQuantita();
        if (quantita == null) {
            return;
        }
        CategoriaDTO categoria = (CategoriaDTO) campoCategoria.getSelectedItem();
        if (categoria == null) {
            erroreCampo("Seleziona una categoria.");
            return;
        }
        risultato = new ProdottoDTO(idEsistente, nome, campoDescrizione.getText().trim(), prezzo,
                quantita, campoDisponibile.isSelected(), campoInOfferta.isSelected(),
                categoria.id(), categoria.nome());
        dispose();
    }

    private BigDecimal leggiPrezzo() {
        try {
            BigDecimal prezzo = new BigDecimal(campoPrezzo.getText().trim().replace(',', '.'));
            if (prezzo.signum() < 0) {
                erroreCampo("Il prezzo non può essere negativo.");
                return null;
            }
            return prezzo;
        } catch (NumberFormatException eccezione) {
            erroreCampo("Prezzo non valido.");
            return null;
        }
    }

    private Integer leggiQuantita() {
        try {
            return Integer.valueOf(campoQuantita.getText().trim());
        } catch (NumberFormatException eccezione) {
            erroreCampo("Quantità non valida.");
            return null;
        }
    }

    private void erroreCampo(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Dati non validi", JOptionPane.ERROR_MESSAGE);
    }
}
