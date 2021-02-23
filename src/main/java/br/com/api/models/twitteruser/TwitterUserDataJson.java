package br.com.api.models.twitteruser;

import java.io.Serializable;
import java.util.Date;
import com.google.gson.annotations.SerializedName;
import br.com.api.models.tweet.PublicMetricDataJson;

public class TwitterUserDataJson implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    private long id;

    @SerializedName(value = "profileImageURL", alternate = "profile_image_url")
    private String profileImageURL;

    private String name;

    private boolean verified;

    private String location;

    private String description;

    @SerializedName(value = "userName", alternate = "username")
    private String userName;

    @SerializedName(value = "createdAt", alternate = "created_at")
    private Date createdAt;

    @SerializedName(value = "publicMetrics", alternate = "public_metrics")
    private PublicMetricDataJson publicMetrics;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileImageURL() {
        return this.profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public boolean getVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public PublicMetricDataJson getPublicMetrics() {
        return this.publicMetrics;
    }

    public void setPublicMetrics(PublicMetricDataJson publicMetrics) {
        this.publicMetrics = publicMetrics;
    }
}
