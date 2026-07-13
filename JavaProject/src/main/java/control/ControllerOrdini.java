package control;

import java.util.List;

import entity.Carrello;
import entity.RegistroCarrello;
import entity.RegistroOrdini;

public class ControllerOrdini {

	public static boolean confermaOrdine(long idCliente) {
    RegistroCarrello reg_c = new RegistroCarrello();
    RegistroOrdini reg_o = new RegistroOrdini();

    Carrello carrello = reg_c.cercaCarrelloPerCliente(idCliente);

    if (carrello == null || carrello.isEmpty()) {return false;}
    
    if (!carrello.haScorteSufficienti()) {return false;}

    return reg_o.registraOrdineDa(carrello);
	}

	public static List<String[]> getOrdini() {
		throw new UnsupportedOperationException();
	}
}
