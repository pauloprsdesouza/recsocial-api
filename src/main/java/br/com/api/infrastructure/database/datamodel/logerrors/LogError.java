package br.com.api.infrastructure.database.datamodel.logerrors;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

@Entity
@Table(name = "log_error")
public class LogError implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column
    private String url;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Column(name = "registration_date")
    private Date registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_active_user")
    private UserAccount activeUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserAccount getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(UserAccount activeUser) {
        this.activeUser = activeUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
