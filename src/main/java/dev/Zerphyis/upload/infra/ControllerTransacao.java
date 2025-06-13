package dev.Zerphyis.upload.infra;

import dev.Zerphyis.upload.aplication.records.DataImportacao;
import dev.Zerphyis.upload.aplication.records.DataTransacao;
import dev.Zerphyis.upload.aplication.services.TransacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Controller
@RequestMapping("/transacoes")
public class ControllerTransacao {

    private final TransacaoService transacaoService;

    public ControllerTransacao(final TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

        @GetMapping("/mostrar")
        public String mostrarPaginaImportacao(Model model) {
            List<DataImportacao> importacoes = transacaoService.listarTodasImportacoes();
            model.addAttribute("importacoes", importacoes);
            return "index";
        }

    @PostMapping("/importar")
    public String importarTransacoes(@RequestParam("file") MultipartFile file, Model model) {
                try {
                    transacaoService.processarEImportarTransacoes(file);
                    model.addAttribute("message", "Transações importadas com sucesso.");
                    List<DataImportacao> importacoes = transacaoService.listarTodasImportacoes();
                    model.addAttribute("importacoes", importacoes);
                    return "index";
                } catch (Exception e) {
                    model.addAttribute("message", "Erro ao importar transações: " + e.getMessage());
                    return "transacao/resposta";
                }
    }

        @GetMapping("/listar")
        public String listarTransacoes(Model model) {
            List<DataTransacao> transacoes = transacaoService.listarTodasTransacoes();
            model.addAttribute("transacoes", transacoes);
            return "transacao/listar";
        }
}

