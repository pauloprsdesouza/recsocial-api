package br.com.api.models.tweet;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class PublicMetricDataJson implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @SerializedName(value = "retweetCount", alternate = "retweet_count")
    private int retweetCount;

    @SerializedName(value = "likeCount", alternate = "like_count")
    private int likeCount;

    @SerializedName(value = "replyCount", alternate = "reply_count")
    private int replyCount;

    @SerializedName(value = "quoteCount", alternate = "quote_count")
    private int quoteCount;

    public int getRetweetCount() {
        return this.retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return this.replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getQuoteCount() {
        return this.quoteCount;
    }

    public void setQuoteCount(int quoteCount) {
        this.quoteCount = quoteCount;
    }

}
