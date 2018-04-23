package com.rackspace.jenkins.plugins.passwordsafe.identity;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentityClient {

    private final String username;
    private final String password;

    private static String IDENTITY_URL = "https://identity-internal.api.rackspacecloud.com";
    private static int ATTEMPTS = 3;
    private static final Logger LOG = Logger.getLogger(IdentityClient.class.getName());

    public IdentityClient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public IdentityResponse doRackspaceAuth() throws IOException {

        final String url = IDENTITY_URL + "/v2.0/tokens";

        final String requestBody = IdentityRequest.json(username, password);

        for (int i = 0; i < ATTEMPTS; i++ ) {
            try {
                final Response r = Request.Post(url)
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .bodyString(requestBody, ContentType.APPLICATION_JSON)
                        .execute();

                // this will vomit an IOException subclass on failure.  on success,
                // it will be the response body as a string.
                final String responseStr = r.returnContent().asString();

                final Gson gson = new Gson();
                return gson.fromJson(responseStr, IdentityResponse.class);

            } catch (IOException e) {
                LOG.log(Level.WARNING, "Error authenticating, attempt " + (i+1), e);
            }

            throw new IOException("Failed to authenticate with identity service.");
        }


        return null;
    }

}
