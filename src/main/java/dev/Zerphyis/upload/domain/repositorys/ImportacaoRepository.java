package dev.Zerphyis.upload.domain.repositorys;

import dev.Zerphyis.upload.domain.importacao.Importacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ImportacaoRepository extends JpaRepository<Importacao,Long> {
    boolean existsByDataTransacao(LocalDate dataTransacao);

    Optional<Importacao> findByDataTransacao(LocalDate dataTransacao);
}
