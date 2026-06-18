package it.ecommerce.boundary;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

final class SupportoImmagine {

    private static final long DIMENSIONE_MASSIMA = 2L * 1024 * 1024;

    private SupportoImmagine() {
    }

    static String selezionaBase64(Component genitore) {
        JFileChooser selettore = new JFileChooser();
        selettore.setDialogTitle("Scegli un'immagine");
        selettore.setFileFilter(new FileNameExtensionFilter("Immagini", "png", "jpg", "jpeg", "gif"));
        if (selettore.showOpenDialog(genitore) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = selettore.getSelectedFile();
        if (file.length() > DIMENSIONE_MASSIMA) {
            JOptionPane.showMessageDialog(genitore, "Immagine troppo grande (massimo 2 MB).",
                    "Immagine non valida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (IOException eccezione) {
            JOptionPane.showMessageDialog(genitore, "Impossibile leggere l'immagine.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    static ImageIcon anteprima(String base64, int lato) {
        if (base64 == null || base64.isBlank()) {
            return null;
        }
        try {
            ImageIcon originale = new ImageIcon(Base64.getDecoder().decode(base64));
            if (originale.getIconWidth() <= 0) {
                return null;
            }
            Image scalata = originale.getImage().getScaledInstance(lato, lato, Image.SCALE_SMOOTH);
            return new ImageIcon(scalata);
        } catch (IllegalArgumentException eccezione) {
            return null;
        }
    }
}
