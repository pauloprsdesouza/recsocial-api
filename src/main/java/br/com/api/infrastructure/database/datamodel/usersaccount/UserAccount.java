package br.com.api.infrastructure.database.datamodel.usersaccount;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.com.api.infrastructure.database.datamodel.logerrors.LogError;
import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;

@Entity
@Table(name = "user_account")
public class UserAccount implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "view_instructions")
    private boolean viewInstructions;

    @Column(name = "registration_date")
    private Date registrationDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recommendation> recommendations;

    @OneToMany(mappedBy = "activeUser")
    private Set<LogError> systemErrors;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getViewInstructions() {
        return this.viewInstructions;
    }

    public void setViewInstructions(boolean viewInstructions) {
        this.viewInstructions = viewInstructions;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserAccount email(String email) {
        setEmail(email);
        return this;
    }

    public UserAccount password(String password) {
        setPassword(password);
        return this;
    }

    public UserAccount registrationDate(Date registrationDate) {
        setRegistrationDate(registrationDate);
        return this;
    }

    public Set<LogError> getSystemErrors() {
        return this.systemErrors;
    }

    public void setSystemErrors(Set<LogError> systemErrors) {
        this.systemErrors = systemErrors;
    }
}
