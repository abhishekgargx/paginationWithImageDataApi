package com.abhishek.paginationWithImageApi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("owner")
    @Expose
    private String owner = "";
    @SerializedName("secret")
    @Expose
    private String secret = "";
    @SerializedName("server")
    @Expose
    private String server = "";
    @SerializedName("farm")
    @Expose
    private Long farm = 0l;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("ispublic")
    @Expose
    private Long ispublic;
    @SerializedName("isfriend")
    @Expose
    private Long isfriend;
    @SerializedName("isfamily")
    @Expose
    private Long isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Long getFarm() {
        return farm;
    }

    public void setFarm(Long farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIspublic() {
        return ispublic;
    }

    public void setIspublic(Long ispublic) {
        this.ispublic = ispublic;
    }

    public Long getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Long isfriend) {
        this.isfriend = isfriend;
    }

    public Long getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Long isfamily) {
        this.isfamily = isfamily;
    }

}