package br.com.api.models.url;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class UrlDataJson implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @SerializedName(value = "name", alternate = "url")
    private String name;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
