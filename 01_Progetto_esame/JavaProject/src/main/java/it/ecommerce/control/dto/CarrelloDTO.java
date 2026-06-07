package it.ecommerce.control.dto;

import java.math.BigDecimal;
import java.util.List;

public record CarrelloDTO(Long id, List<RigaCarrelloDTO> righe, BigDecimal totale) {
}
