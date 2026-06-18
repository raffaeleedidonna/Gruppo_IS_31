package it.ecommerce.entity.coordinatore;

import java.math.BigDecimal;
import java.util.List;

public record VistaCarrello(Long carrelloId, List<VistaRigaCarrello> righe, BigDecimal totale) {
}
