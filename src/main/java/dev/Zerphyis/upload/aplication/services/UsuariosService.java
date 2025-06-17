package dev.Zerphyis.upload.aplication.services;

import dev.Zerphyis.upload.aplication.records.DataAlteracaoSenha;
import dev.Zerphyis.upload.aplication.records.DataUsuarios;
import dev.Zerphyis.upload.aplication.services.email.EmailService;
import dev.Zerphyis.upload.domain.repositorys.UsuariosRepository;
import dev.Zerphyis.upload.domain.usuarios.Usuarios;
import dev.Zerphyis.upload.infra.exceptions.RegraNegocio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuariosService implements UserDetailsService {
    private final UsuariosRepository usuarioRepository;
    private final PasswordEncoder encriptador;
    private final EmailService emailService;

    public UsuariosService(UsuariosRepository usuarioRepository, PasswordEncoder encriptador, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.encriptador = encriptador;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public Long salvarUsuarios(DataUsuarios data) {
        var newUsuarios = new Usuarios(data);
        String senhaCriptografada = encriptador.encode(newUsuarios.getPassword());
        var salvarUsuarios = usuarioRepository.save(newUsuarios);
        return salvarUsuarios.getId();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void alterarSenha(DataAlteracaoSenha dados, Usuarios usuario) {
        if (!encriptador.matches(dados.senhaAtual(), usuario.getPassword())) {
            throw new RegraNegocio("Senhas dígitadas não batem");

        }
        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RegraNegocio("Senha e confirmação não conferem!");
        }
        String senhaCriptografada = encriptador.encode(dados.novaSenha());
        usuario.alterarSenha(senhaCriptografada);

        usuarioRepository.save(usuario);
    }

    public void enviarToken(String email){
        Usuarios usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new RegraNegocio("Usuário não encontrado!")
        );
        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        emailService.enviarEmailSenha(usuario);
    }


}
