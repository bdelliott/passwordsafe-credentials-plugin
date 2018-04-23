package com.rackspace.jenkins.plugins.passwordsafe.identity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class IdentityRequest {

    private Map auth;

    public IdentityRequest(String username, String password) {

        auth = new HashMap();

        Map domainMap = new HashMap();
        domainMap.put("name", "rackspace");

        auth.put("RAX-AUTH:domain", domainMap);

        Map credsMap = new HashMap();
        credsMap.put("username", username);
        credsMap.put("password", password);

        auth.put("passwordCredentials", credsMap);

    }

    public Map getAuth() {
        return auth;
    }

    public void setAuth(Map auth) {
        this.auth = auth;
    }

    public static String json(String username, String password) {
        final IdentityRequest requestBody = new IdentityRequest(username, password);
        Gson gson = new Gson();
        return gson.toJson(requestBody);
    }

}
