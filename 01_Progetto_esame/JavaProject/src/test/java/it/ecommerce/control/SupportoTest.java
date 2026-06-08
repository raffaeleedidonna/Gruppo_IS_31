package it.ecommerce.control;

import java.lang.reflect.Field;

final class SupportoTest {

    private SupportoTest() {
    }

    static void assegnaId(Object entita, long id) {
        try {
            Field campo = campoId(entita.getClass());
            campo.setAccessible(true);
            campo.set(entita, id);
        } catch (ReflectiveOperationException eccezione) {
            throw new IllegalStateException(eccezione);
        }
    }

    private static Field campoId(Class<?> tipo) throws NoSuchFieldException {
        Class<?> corrente = tipo;
        while (corrente != null) {
            try {
                return corrente.getDeclaredField("id");
            } catch (NoSuchFieldException assente) {
                corrente = corrente.getSuperclass();
            }
        }
        throw new NoSuchFieldException("id");
    }
}
