package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.ecommerce.control.AutenticazioneController;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.RegistrazioneDTO;
import it.ecommerce.control.dto.UtenteDTO;

public class RegistrazioneView extends JDialog {

    private final AutenticazioneController autenticazione;
    private final JTextField campoNome = new JTextField(18);
    private final JTextField campoCognome = new JTextField(18);
    private final JTextField campoEmail = new JTextField(18);
    private final JPasswordField campoPassword = new JPasswordField(18);

    public RegistrazioneView(Window proprietario, AutenticazioneController autenticazione) {
        super(proprietario, "Registrazione", ModalityType.APPLICATION_MODAL);
        this.autenticazione = autenticazione;
        setContentPane(creaContenuto());
        pack();
        setLocationRelativeTo(proprietario);
    }

    private JPanel creaContenuto() {
        JPanel campi = new JPanel(new GridLayout(0, 2, 8, 8));
        campi.add(new JLabel("Nome"));
        campi.add(campoNome);
        campi.add(new JLabel("Cognome"));
        campi.add(campoCognome);
        campi.add(new JLabel("Email"));
        campi.add(campoEmail);
        campi.add(new JLabel("Password"));
        campi.add(campoPassword);

        JButton registrati = new JButton("Registrati");
        JButton annulla = new JButton("Annulla");
        registrati.addActionListener(evento -> registra());
        annulla.addActionListener(evento -> dispose());
        JPanel pulsanti = new JPanel();
        pulsanti.add(registrati);
        pulsanti.add(annulla);

        JPanel contenuto = new JPanel(new BorderLayout(10, 10));
        contenuto.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contenuto.add(campi, BorderLayout.CENTER);
        contenuto.add(pulsanti, BorderLayout.SOUTH);
        return contenuto;
    }

    private void registra() {
        RegistrazioneDTO dati = new RegistrazioneDTO(
                campoNome.getText().trim(),
                campoCognome.getText().trim(),
                campoEmail.getText().trim(),
                new String(campoPassword.getPassword()));
        EsitoDTO<UtenteDTO> esito = autenticazione.registra(dati);
        if (esito.successo()) {
            JOptionPane.showMessageDialog(this, esito.messaggio(), "Registrazione",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, esito.messaggio(), "Registrazione non riuscita",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
