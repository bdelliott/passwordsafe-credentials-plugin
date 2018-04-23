package com.rackspace.jenkins.plugins.passwordsafe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rackspace.jenkins.plugins.passwordsafe.identity.IdentityClient;
import com.rackspace.jenkins.plugins.passwordsafe.identity.IdentityResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * PasswordSafe client implementation
 */
public class PasswordSafeClient {

    private final IdentityResponse identityResponse;

    private static final String PASSWORDSAFE_URL = "https://passwordsafe.corp.rackspace.com";

    public PasswordSafeClient(String username, String password) throws IOException {

        final IdentityClient identityClient = new IdentityClient(username, password);
        identityResponse = identityClient.doRackspaceAuth();
    }

    public List<PasswordSafeProject> projectsList() throws IOException {
        final String url = PASSWORDSAFE_URL + "/projects";

        final Response r = Request.Get(url)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("x-auth-token", identityResponse.getTokenId())
                .execute();

        // this will vomit an IOException subclass on failure.  on success,
        // it will be the response body as a string.
        final String responseStr = r.returnContent().asString();
        final Gson gson = new Gson();
        System.out.println(responseStr);

        // declare that we want to decode a list of type Project from json:
        final Type listType = new TypeToken<List<PasswordSafeProject>>(){}.getType();
        final List<PasswordSafeProject> projects = gson.fromJson(responseStr, listType);
        return projects;
    }

    /**
     * Standalone entry point for testing password safe client operations.
     *
     * @param args  CLI args
     * @throws IOException on client error
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: PasswordSafeClient username password");
            System.exit(1);
        }

        final String username = args[0];
        final String password = args[1];

        final PasswordSafeClient client = new PasswordSafeClient(username, password);

        // list projects
        for (PasswordSafeProject project : client.projectsList()) {
            System.out.println(project);
        }
    }
}
