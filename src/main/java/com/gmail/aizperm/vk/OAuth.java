package com.gmail.aizperm.vk;

import java.net.URLEncoder;

public class OAuth {
    private Integer client_id;
    private String display = "page";
    private String redirect_uri = "https://oauth.vk.com/blank.html";
    private String scope = "offline,messages,photos";
    private String response_type = "code";
    private String v = "5.71";

    public OAuth(Integer client_id) {
        this.client_id = client_id;
    }

    public Integer getClient_id() {
        return client_id;
    }

    public OAuth setClient_id(Integer client_id) {
        this.client_id = client_id;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public OAuth setDisplay(String display) {
        this.display = display;
        return this;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public OAuth setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public OAuth setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getResponse_type() {
        return response_type;
    }

    public OAuth setResponse_type(String response_type) {
        this.response_type = response_type;
        return this;
    }

    public String getV() {
        return v;
    }

    public OAuth setV(String v) {
        this.v = v;
        return this;
    }

    public String build() {
        StringBuilder b = new StringBuilder();
        b.append("https://oauth.vk.com/authorize?");
        b.append("client_id=");
        b.append(client_id);
        b.append("&display=");
        b.append(display);
        b.append("&redirect_uri=");
        b.append(URLEncoder.encode(redirect_uri));
        b.append("&scope=");
        b.append(scope);
        b.append("&response_type=");
        b.append(response_type);
        b.append("&v=");
        b.append(v);

        return b.toString();
    }
}
