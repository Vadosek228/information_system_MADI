package ru.vladislav_akulinin.mychat_version_2.notifications;

public class Token {
    private String token;

    public Token(String token){
        this.token = token;
    }

    public Token(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
