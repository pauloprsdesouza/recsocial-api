package br.com.api.models.useraccount;

import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

public class UserAccountJson {
    private String email;
    private boolean viewInstructions;
    private String token;

    public UserAccountJson(UserAccount user) {
        email = user.getEmail();
        viewInstructions = user.getViewInstructions();
        token = user.getToken();
    }

    public String getEmail() {
        return this.email;
    }

    public boolean getViewInstructions() {
        return this.viewInstructions;
    }

    public String getToken() {
        return this.token;
    }
}
