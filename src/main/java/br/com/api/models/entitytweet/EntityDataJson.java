package br.com.api.models.entitytweet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.com.api.models.hashtag.HashtagDataJson;
import br.com.api.models.twitteruser.TwitterUserDataJson;
import br.com.api.models.url.UrlDataJson;

public class EntityDataJson implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    private List<UrlDataJson> urls;
    private List<HashtagDataJson> hashtags;
    private List<TwitterUserDataJson> mentions;

    public EntityDataJson() {
        this.urls = new ArrayList<>();
        this.hashtags = new ArrayList<>();
        this.mentions = new ArrayList<>();
    }

    public List<UrlDataJson> getUrls() {
        return this.urls;
    }

    public void setUrls(List<UrlDataJson> urls) {
        this.urls = urls;
    }

    public List<HashtagDataJson> getHashtags() {
        return this.hashtags;
    }

    public void setHashtags(List<HashtagDataJson> hashtags) {
        this.hashtags = hashtags;
    }

    public List<TwitterUserDataJson> getMentions() {
        return this.mentions;
    }

    public void setMentions(List<TwitterUserDataJson> mentions) {
        this.mentions = mentions;
    }

}
