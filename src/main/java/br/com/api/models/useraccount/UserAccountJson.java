package br.com.api.models.useraccount;

import java.util.Date;

import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

public class UserAccountJson {
    private String email;
    private boolean viewInstructions;
    private Date registrationDate;

    public UserAccountJson(UserAccount user) {
        email = user.getEmail();
        viewInstructions = user.getViewInstructions();
        registrationDate = user.getRegistrationDate();
    }

    public String getEmail() {
        return this.email;
    }

    public boolean getViewInstructions() {
        return this.viewInstructions;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }
}
