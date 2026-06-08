package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.ecommerce.control.ProfiloController;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;

public class ProfiloView extends JPanel {

    private static final int LATO_ANTEPRIMA = 120;

    private final ProfiloController controller;
    private final Long utenteId;
    private final JTextField campoNome = new JTextField(24);
    private final JTextField campoCognome = new JTextField(24);
    private final JTextField campoEmail = new JTextField(24);
    private final JTextField campoIndirizzo = new JTextField(24);
    private final JLabel anteprimaFoto = new JLabel();
    private String immagineBase64;

    public ProfiloView(ProfiloController controller, Long utenteId) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.utenteId = utenteId;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        campoEmail.setEditable(false);
        add(creaModulo(), BorderLayout.NORTH);
        add(creaFoto(), BorderLayout.CENTER);
        add(creaPulsanti(), BorderLayout.SOUTH);
        carica();
    }

    private JPanel creaModulo() {
        JPanel campi = new JPanel(new GridLayout(0, 2, 8, 8));
        campi.add(new JLabel("Nome"));
        campi.add(campoNome);
        campi.add(new JLabel("Cognome"));
        campi.add(campoCognome);
        campi.add(new JLabel("Email (non modificabile)"));
        campi.add(campoEmail);
        campi.add(new JLabel("Indirizzo di spedizione principale"));
        campi.add(campoIndirizzo);
        return campi;
    }

    private JPanel creaFoto() {
        JPanel pannello = new JPanel(new BorderLayout(8, 8));
        pannello.add(new JLabel("Foto profilo"), BorderLayout.NORTH);
        pannello.add(anteprimaFoto, BorderLayout.CENTER);
        JButton cambia = new JButton("Cambia foto…");
        cambia.addActionListener(evento -> cambiaFoto());
        JPanel azione = new JPanel();
        azione.add(cambia);
        pannello.add(azione, BorderLayout.SOUTH);
        return pannello;
    }

    private JPanel creaPulsanti() {
        JButton salva = new JButton("Salva");
        salva.addActionListener(evento -> salva());
        JPanel pannello = new JPanel();
        pannello.add(salva);
        return pannello;
    }

    private void carica() {
        ProfiloDTO profilo = controller.mostraProfilo(utenteId);
        campoNome.setText(valore(profilo.nome()));
        campoCognome.setText(valore(profilo.cognome()));
        campoEmail.setText(valore(profilo.email()));
        campoIndirizzo.setText(valore(profilo.indirizzoSpedizionePrincipale()));
        immagineBase64 = profilo.immagineProfilo();
        mostraAnteprima();
    }

    private void cambiaFoto() {
        String base64 = SupportoImmagine.selezionaBase64(this);
        if (base64 != null) {
            immagineBase64 = base64;
            mostraAnteprima();
        }
    }

    private void mostraAnteprima() {
        ImageIcon icona = SupportoImmagine.anteprima(immagineBase64, LATO_ANTEPRIMA);
        if (icona == null) {
            anteprimaFoto.setIcon(null);
            anteprimaFoto.setText("Nessuna foto");
        } else {
            anteprimaFoto.setText(null);
            anteprimaFoto.setIcon(icona);
        }
    }

    private void salva() {
        ProfiloDTO modifiche = new ProfiloDTO(testo(campoNome), testo(campoCognome),
                campoEmail.getText().trim(), testo(campoIndirizzo), immagineBase64);
        EsitoDTO<ProfiloDTO> esito = controller.aggiornaProfilo(utenteId, modifiche);
        JOptionPane.showMessageDialog(this, esito.messaggio(),
                esito.successo() ? "Profilo" : "Errore",
                esito.successo() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        if (esito.successo()) {
            carica();
        }
    }

    private String valore(String testo) {
        return testo == null ? "" : testo;
    }

    private String testo(JTextField campo) {
        String testo = campo.getText().trim();
        return testo.isEmpty() ? null : testo;
    }
}
