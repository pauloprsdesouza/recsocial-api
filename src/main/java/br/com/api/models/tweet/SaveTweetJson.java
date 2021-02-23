package br.com.api.models.tweet;

import java.util.List;

public class SaveTweetJson {

    private List<String> screenNames;
    private long idEntity;

    public List<String> getScreenNames() {
        return screenNames;
    }

    public void setScreenNames(List<String> screenNames) {
        this.screenNames = screenNames;
    }

    public long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(long idEntity) {
        this.idEntity = idEntity;
    }
}
