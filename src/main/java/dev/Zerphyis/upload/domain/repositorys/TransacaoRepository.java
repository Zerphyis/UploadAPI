package dev.Zerphyis.upload.domain.repositorys;

import dev.Zerphyis.upload.domain.transacoes.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao,Long> {
}
