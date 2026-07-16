package boundary;

import javax.swing.*;

import boundary.notifiche.NotificatoreLog;
import control.GestoreNotifiche;

public class Main {
	public static void main(String[] args) {
		GestoreNotifiche.getInstance().setServizio(new NotificatoreLog());

		SwingUtilities.invokeLater(() -> new FormAccesso().apriFormAccesso());
	}
}
