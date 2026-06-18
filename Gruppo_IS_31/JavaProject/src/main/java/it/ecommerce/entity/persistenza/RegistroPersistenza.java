package it.ecommerce.entity.persistenza;

import java.util.ServiceLoader;

public final class RegistroPersistenza {

    private static FornitorePersistenza fornitore;

    private RegistroPersistenza() {
    }

    public static synchronized FornitorePersistenza fornitore() {
        if (fornitore == null) {
            fornitore = ServiceLoader.load(FornitorePersistenza.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Nessun FornitorePersistenza registrato"));
        }
        return fornitore;
    }
}
