package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.ecommerce.control.FabbricaControllori;
import it.ecommerce.control.dto.CredenzialiDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.UtenteDTO;

public class LoginView extends JFrame {

    private final FabbricaControllori fabbrica;
    private final JTextField campoEmail = new JTextField(18);
    private final JPasswordField campoPassword = new JPasswordField(18);

    public LoginView(FabbricaControllori fabbrica) {
        super("Accesso - E-commerce");
        this.fabbrica = fabbrica;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(creaContenuto());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel creaContenuto() {
        JPanel campi = new JPanel(new GridLayout(0, 2, 8, 8));
        campi.add(new JLabel("Email"));
        campi.add(campoEmail);
        campi.add(new JLabel("Password"));
        campi.add(campoPassword);

        JButton accedi = new JButton("Accedi");
        JButton registrati = new JButton("Registrati");
        accedi.addActionListener(evento -> accedi());
        registrati.addActionListener(evento -> apriRegistrazione());
        getRootPane().setDefaultButton(accedi);
        JPanel pulsanti = new JPanel();
        pulsanti.add(accedi);
        pulsanti.add(registrati);

        JPanel contenuto = new JPanel(new BorderLayout(10, 10));
        contenuto.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contenuto.add(new JLabel("Benvenuto nella piattaforma E-commerce"), BorderLayout.NORTH);
        contenuto.add(campi, BorderLayout.CENTER);
        contenuto.add(pulsanti, BorderLayout.SOUTH);
        return contenuto;
    }

    private void accedi() {
        CredenzialiDTO credenziali = new CredenzialiDTO(
                campoEmail.getText().trim(), new String(campoPassword.getPassword()));
        EsitoDTO<UtenteDTO> esito = fabbrica.autenticazione().autentica(credenziali);
        if (esito.successo()) {
            apriHomePer(esito.dato());
        } else {
            JOptionPane.showMessageDialog(this, esito.messaggio(), "Accesso negato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apriRegistrazione() {
        new RegistrazioneView(this, fabbrica.autenticazione()).setVisible(true);
    }

    private void apriHomePer(UtenteDTO utente) {
        JFrame home = "AMMINISTRATORE".equals(utente.ruolo())
                ? new HomeAmministratoreView(fabbrica, utente)
                : new HomeClienteView(fabbrica, utente);
        home.setVisible(true);
        dispose();
    }
}
