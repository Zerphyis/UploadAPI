package dev.Zerphyis.upload.domain.repositorys;

import dev.Zerphyis.upload.domain.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Long> {
    Optional<Usuarios> findByEmailIgnoreCase(String emal);
}
