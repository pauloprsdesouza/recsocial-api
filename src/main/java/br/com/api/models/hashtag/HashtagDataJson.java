package br.com.api.models.hashtag;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class HashtagDataJson implements Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @SerializedName(value = "name", alternate = "tag")
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
