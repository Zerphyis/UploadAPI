package dev.Zerphyis.upload.aplication.services;


import dev.Zerphyis.upload.aplication.records.DataImportacao;
import dev.Zerphyis.upload.aplication.records.DataTransacao;
import dev.Zerphyis.upload.domain.importacao.Importacao;
import dev.Zerphyis.upload.domain.repositorys.ImportacaoRepository;
import dev.Zerphyis.upload.domain.repositorys.TransacaoRepository;
import dev.Zerphyis.upload.domain.transacoes.Transacao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ImportacaoRepository importacaoRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void processarEImportarTransacoes(MultipartFile file) throws Exception {
        List<DataTransacao> transacoes = lerTransacoesDoArquivo(file);

        if (transacoes.isEmpty()) {
            throw new Exception("O arquivo CSV está vazio!");
        }

        LocalDate dataTransacao = transacoes.get(0).datatTransacao().toLocalDate();

        if (importacaoRepository.existsByDataTransacao(dataTransacao)) {
            throw new Exception(
                    "Transações para a data " + dataTransacao + " já foram importadas."
            );
        }

        for (DataTransacao dto : transacoes) {
            Transacao entity = toEntity(dto);
            transacaoRepository.save(entity);
        }

        Importacao importacao = new Importacao();
        importacao.setDataHoraImportacao(LocalDateTime.now());
        importacao.setDataTransacao(dataTransacao);
        importacaoRepository.save(importacao);
    }

    private List<DataTransacao> lerTransacoesDoArquivo(MultipartFile file) throws Exception {
        List<DataTransacao> transacoes = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser parser = CSVFormat.DEFAULT.parse(reader);

            for (CSVRecord record : parser) {
                if (record.size() < 8) continue;
                if (record.get(6).isBlank() || record.get(7).isBlank()) continue;

                try {
                    DataTransacao dto = new DataTransacao(
                            record.get(0).trim(),
                            record.get(1).trim(),
                            record.get(2).trim(),
                            record.get(3).trim(),
                            record.get(4).trim(),
                            record.get(5).trim(),
                            new BigDecimal(record.get(6).replace(",", ".").trim()),
                            LocalDateTime.parse(record.get(7).replace("T", " "), FORMATTER)
                    );
                    transacoes.add(dto);
                } catch (Exception e) {
                    System.err.println("Linha ignorada (erro de parse): " + record.getRecordNumber());
                }
            }

            if (transacoes.isEmpty()) {
                throw new Exception("Nenhuma transação válida foi encontrada no arquivo CSV.");
            }

            return transacoes;

        } catch (Exception e) {
            throw new Exception("Erro ao ler o arquivo CSV", e);
        }
    }

    public List<DataTransacao> listarTodasTransacoes() {
        return transacaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DataImportacao> listarTodasImportacoes() {
        return importacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataTransacao"))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DataTransacao toDTO(Transacao t) {
        return new DataTransacao(
                t.getBancoOrigem(),
                t.getAgenciaOrigem(),
                t.getContaOrigem(),
                t.getBancoDestino(),
                t.getAgenciaDestino(),
                t.getContaDestino(),
                t.getValor(),
                t.getDatatTransacao()
        );
    }

    private DataImportacao toDTO(Importacao i) {
        return new DataImportacao(
                i.getDataHoraImportacao(),
                i.getDataTransacao()
        );
    }

    private Transacao toEntity(DataTransacao dto) {
        Transacao t = new Transacao();
        t.setBancoOrigem(dto.bancoOrigem());
        t.setAgenciaOrigem(dto.agenciaOrigem());
        t.setContaOrigem(dto.contaOrigem());
        t.setBancoDestino(dto.bancoDestino());
        t.setAgenciaDestino(dto.agenciaDestino());
        t.setContaDestino(dto.contaDestino());
        t.setValor(dto.valor());
        t.setDatatTransacao(dto.datatTransacao());
        return t;
    }
}


