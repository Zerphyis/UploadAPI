package dev.Zerphyis.upload.domain.importacao;

import dev.Zerphyis.upload.aplication.records.DataImportacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "tb_importacao")
public class Importacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime dataHoraImportacao;
    @NotNull
    private LocalDate dataTransacao;

    public Importacao() {
    }
    public Importacao(DataImportacao data){
        this.dataHoraImportacao=data.dataHoraImportacao();
        this.dataTransacao=data.dataTransacao();
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public LocalDateTime getDataHoraImportacao() {
        return dataHoraImportacao;
    }

    public void setDataHoraImportacao(LocalDateTime dataHoraImportacao) {
        this.dataHoraImportacao = dataHoraImportacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


