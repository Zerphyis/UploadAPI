package dev.Zerphyis.upload.infra.contorllers;

import dev.Zerphyis.upload.aplication.services.UsuariosService;
import dev.Zerphyis.upload.infra.exceptions.RegraNegocio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class ControllerEsqueciMinhaSenha {
    public static final String FORMULARIO_RECUPERACAO_SENHA = "auth/recuperacao-senha";
    private final UsuariosService service;

    public ControllerEsqueciMinhaSenha(UsuariosService service) {
        this.service = service;
    }


    @GetMapping("esqueci-minha-senha")
    public String carregarPaginaEsqueciMinhaSenha() {
        return FORMULARIO_RECUPERACAO_SENHA;
    }

    @PostMapping("esqueci-minha-senha")
    public String enviarTokenEmail(@ModelAttribute("email") String email, Model model) {
        try {
            service.enviarToken(email);
            return "redirect:esqueci-minha-senha?verificar";
        } catch (RegraNegocio e) {
            model.addAttribute("erro", e.getMessage());
            return FORMULARIO_RECUPERACAO_SENHA;
        }
    }
}
