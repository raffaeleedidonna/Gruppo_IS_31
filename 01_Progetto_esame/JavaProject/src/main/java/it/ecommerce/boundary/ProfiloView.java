package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.ecommerce.control.ProfiloController;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProfiloDTO;

public class ProfiloView extends JPanel {

    private final ProfiloController controller;
    private final Long utenteId;
    private final JTextField campoDati = new JTextField(24);
    private final JTextField campoIndirizzo = new JTextField(24);
    private final JTextField campoImmagine = new JTextField(24);

    public ProfiloView(ProfiloController controller, Long utenteId) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.utenteId = utenteId;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(creaModulo(), BorderLayout.NORTH);
        add(creaPulsanti(), BorderLayout.SOUTH);
        carica();
    }

    private JPanel creaModulo() {
        JPanel campi = new JPanel(new GridLayout(0, 2, 8, 8));
        campi.add(new JLabel("Dati anagrafici"));
        campi.add(campoDati);
        campi.add(new JLabel("Indirizzo di spedizione principale"));
        campi.add(campoIndirizzo);
        campi.add(new JLabel("Immagine profilo (percorso)"));
        campi.add(campoImmagine);
        return campi;
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
        campoDati.setText(valore(profilo.datiAnagrafici()));
        campoIndirizzo.setText(valore(profilo.indirizzoSpedizionePrincipale()));
        campoImmagine.setText(valore(profilo.immagineProfilo()));
    }

    private void salva() {
        ProfiloDTO modifiche = new ProfiloDTO(testo(campoDati), testo(campoIndirizzo), testo(campoImmagine));
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
