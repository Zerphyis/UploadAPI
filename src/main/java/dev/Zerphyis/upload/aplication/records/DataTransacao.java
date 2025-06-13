package dev.Zerphyis.upload.aplication.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataTransacao(

                            String bancoOrigem,
                            String agenciaOrigem,
                            String contaOrigem,
                            String bancoDestino,
                            String contaDestino,
                            String agenciaDestino,
                            BigDecimal valor,
                            LocalDateTime datatTransacao
) {
}
