package dev.Zerphyis.upload.aplication.records;

import jakarta.validation.constraints.NotBlank;

public record DataAlteracaoSenha(@NotBlank String senhaAtual,
                                 @NotBlank String novaSenha,
                                 @NotBlank String novaSenhaConfirmacao) {
}
