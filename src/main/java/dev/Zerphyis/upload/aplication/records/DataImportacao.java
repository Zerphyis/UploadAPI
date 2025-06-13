package dev.Zerphyis.upload.aplication.records;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DataImportacao(
        LocalDateTime dataHoraImportacao,
        LocalDate dataTransacao
) {
}
