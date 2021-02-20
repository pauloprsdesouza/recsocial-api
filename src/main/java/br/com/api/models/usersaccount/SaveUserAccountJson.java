package br.com.api.models.usersaccount;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SaveUserAccountJson {

    @NotBlank(message = "Campo Email é obrigatório")
    @Pattern(regexp = ".+@.+\\..+", message = "Por favor forneça um email válido")
    @Size(min = 10, max = 100, message = "Campo deve estar entre 10 e 100")
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
