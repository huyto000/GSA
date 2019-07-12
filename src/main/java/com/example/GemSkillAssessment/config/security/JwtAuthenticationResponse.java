package com.example.GemSkillAssessment.config.security;

import com.example.GemSkillAssessment.enumerted.EJobTitle;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "GSA";
    private EJobTitle eJobTitle;
    private Boolean fill;
    private Boolean edit;
    private Integer idUser;
    private Integer idPeriod;
    private String userName;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int geteJobTitle() {
        return eJobTitle.getLabel();
    }

    public void seteJobTitle(EJobTitle eJobTitle) {
        this.eJobTitle = eJobTitle;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdPeriod() {
        return idPeriod;
    }

    public void setIdPeriod(Integer idPeriod) {
        this.idPeriod = idPeriod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
