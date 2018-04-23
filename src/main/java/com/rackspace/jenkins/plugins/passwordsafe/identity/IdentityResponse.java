package com.rackspace.jenkins.plugins.passwordsafe.identity;

import java.util.Map;

public class IdentityResponse {

    private static final Object USER_KEY = "user";
    private static final Object USER_ID_KEY = "id";
    private static final String TOKEN_KEY = "token";
    private static final String TOKEN_ID_KEY = "id";

    private Map<String, Map> access;

    public Map<String, Map> getAccess() {
        return access;
    }

    public void setAccess(Map<String, Map> access) {
        this.access = access;
    }

    public Map<String, String> getUser() {
        return (Map<String, String>)getAccess().get(USER_KEY);
    }

    public String getUserId() {
        return (String)getUser().get(USER_ID_KEY);
    }
    
    @Override
    public String toString() {
        return "[IdentityResponse: user=" + getUserId() + "]";
    }

    public Map<String, String> getToken() {
        return getAccess().get(TOKEN_KEY);
    }

    public String getTokenId() {
        return getToken().get(TOKEN_ID_KEY);
    }
}
