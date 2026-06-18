package it.ecommerce.boundary;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import it.ecommerce.control.FabbricaControllori;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::avvia);
    }

    private static void avvia() {
        applicaLookAndFeelDiSistema();
        new LoginView(new FabbricaControllori()).setVisible(true);
    }

    private static void applicaLookAndFeelDiSistema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedOperationException
                | javax.swing.UnsupportedLookAndFeelException eccezione) {
            UIManager.getCrossPlatformLookAndFeelClassName();
        }
    }
}
