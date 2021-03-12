package br.com.api.infrastructure.database.datamodel.recommendations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

@Entity
@Table(name = "recommendation")
public class Recommendation implements Serializable {

    /**
      * 
      */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "recommendation_type")
    private String recommendationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_active_user")
    private UserAccount user;

    @Column(name = "finished_date")
    private Date finishedDate;

    @Column(name = "registration_date")
    private Date registrationDate;

    @OneToMany(mappedBy = "recommendation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RecommendationItem> items;

    public Recommendation() {
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecommendationTypeEnum getRecommendationType() {
        return RecommendationTypeEnum.getValue(this.recommendationType);
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public List<RecommendationItem> getItems() {
        return items;
    }

    public void setItems(List<RecommendationItem> items) {
        this.items = items;
    }

    public void addRecommendationItem(RecommendationItem item) {
        this.items.add(item);
        item.setRecommendation(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recommendation other = (Recommendation) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
