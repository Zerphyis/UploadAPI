package dev.Zerphyis.upload.infra.contorllers;

import dev.Zerphyis.upload.aplication.records.DataAlteracaoSenha;
import dev.Zerphyis.upload.aplication.services.UsuariosService;
import dev.Zerphyis.upload.domain.usuarios.Usuarios;
import dev.Zerphyis.upload.infra.exceptions.RegraNegocio;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControllerLogin {
    private final UsuariosService service;

    public static final String FORMULARIO_ALTERACAO_SENHA = "auth/alteracao-senha";

    public ControllerLogin(UsuariosService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String carregaPaginaLogin() {
        return "auth/login";
    }

    @GetMapping("/alterar-senha")
    public String carregaPaginaAlteracao() {
        return FORMULARIO_ALTERACAO_SENHA;
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute("dados") DataAlteracaoSenha dados, BindingResult result, Model model, @AuthenticationPrincipal Usuarios logado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERACAO_SENHA;
        }

        try {
            service.alterarSenha(dados, logado);
            return "redirect:home";
        } catch (RegraNegocio e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERACAO_SENHA;
        }
    }
}


