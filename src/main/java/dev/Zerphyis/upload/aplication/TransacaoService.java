package dev.Zerphyis.upload.aplication;


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

@Service
public class TransacaoService {

        @Autowired
        private TransacaoRepository transacaoRepository;

        @Autowired
       private ImportacaoRepository importacaoRepository;

        public void processarEImportarTransacoes(MultipartFile file) throws Exception {
            List<Transacao> transacoes = lerTransacoesDoArquivo(file);

            if (transacoes.isEmpty()) {
                throw new Exception("O arquivo CSV está vazio!");
            }

            LocalDate dataTransacao = transacoes.get(0).getDatatTransacao().toLocalDate();

            if (importacaoRepository.existsByDataTransacao(dataTransacao)) {
                throw new Exception(
                        "Transações para a data " + dataTransacao + " já foram importadas.");
            }

            for (Transacao transacao : transacoes) {
                transacaoRepository.save(transacao);
            }

            Importacao importacao = new Importacao();
            importacao.setDataHoraImportacao(LocalDateTime.now());
            importacao.setDataTransacao(dataTransacao);
            importacaoRepository.save(importacao);
        }

        private static final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        private List<Transacao> lerTransacoesDoArquivo(MultipartFile file) throws Exception {
            List<Transacao> transacoes = new ArrayList<>();

            try (Reader reader = new InputStreamReader(file.getInputStream())) {
                CSVParser parser = CSVFormat.DEFAULT.parse(reader);

                for (CSVRecord record : parser) {
                    Transacao transacao = new Transacao();
                    transacao.setBancoOrigem(record.get(0).trim());
                    transacao.setAgenciaOrigem(record.get(1).trim());
                    transacao.setContaOrigem(record.get(2).trim());
                    transacao.setBancoDestino(record.get(3).trim());
                    transacao.setAgenciaDestino(record.get(4).trim());
                    transacao.setContaDestino(record.get(5).trim());

                    try {
                        transacao.setValor(new BigDecimal(record.get(6).replaceAll(",", ".").trim()));
                        transacao.setDatatTransacao(
                                LocalDateTime.parse(record.get(7).replace("T", " "), formatter));
                    } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                        throw new Exception(
                                "Erro ao converter valor ou data na linha: " + record.getRecordNumber(),
                                e);
                    }

                    transacoes.add(transacao);
                }
            } catch (Exception e) {
                throw new Exception("Erro ao ler o arquivo CSV", e);
            }

            return transacoes;
        }

        public List<Transacao> listarTodasTransacoes() {
            return transacaoRepository.findAll();
        }

        public List<Importacao> listarTodasImportacoes() {
            return importacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataTransacao"));
        }
    }


