package dev.Zerphyis.upload.aplication.services.email;

import dev.Zerphyis.upload.domain.usuarios.Usuarios;
import dev.Zerphyis.upload.infra.exceptions.RegraNegocio;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    private final JavaMailSender enviadorEmail;
    private static final String EMAIL_ORIGEM = "Teste01@email.com";
    private static final String NOME_ENVIADOR = "Challenger Back-End #3";

    public static final String URL_SITE = "http://localhost:8080";

    public EmailService(JavaMailSender enviadorEmail) {
        this.enviadorEmail = enviadorEmail;
    }

    @Async
    private void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = enviadorEmail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);
        } catch(MessagingException | UnsupportedEncodingException e){
            throw new RegraNegocio("Erro ao enviar email");
        }

        enviadorEmail.send(message);
    }

    public void enviarEmailSenha(Usuarios usuario) {
        String assunto = "Challenger Back-End #3 - Solicitação de alteração de senha";

        String template = "<p>Olá <strong>[[name]]</strong>,</p>"
                + "<p>Recebemos uma solicitação para redefinir a sua senha. "
                + "Para continuar, clique no link abaixo:</p>"
                + "<p><a href=\"[[URL]]\" target=\"_blank\" style=\"color: #2b6cb0; font-weight: bold; text-decoration: none;\">"
                + "Alterar minha senha</a></p>"
                + "<p>Se você não solicitou essa alteração, ignore este e-mail.</p>"
                + "<br>"
                + "<p>Obrigado,<br>Equipe Challenger Back-End #3</p>";

        String conteudo = gerarConteudoEmail(template, usuario.getNome(), URL_SITE + "/recuperar-conta?codigo=" + usuario.getToken());

        enviarEmail(usuario.getUsername(), assunto, conteudo);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }
}
