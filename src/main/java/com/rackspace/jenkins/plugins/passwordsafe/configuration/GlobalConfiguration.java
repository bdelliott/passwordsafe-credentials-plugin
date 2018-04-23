package com.rackspace.jenkins.plugins.passwordsafe.configuration;

import hudson.Extension;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Global configuration encompassing one or more credentials for authenticating with Rackspace
 * Password Safe.
 */
@Extension
public class GlobalConfiguration extends jenkins.model.GlobalConfiguration {

    private List<CredentialConfiguration> credentials = new ArrayList<>();

    public List<CredentialConfiguration> getCredentials() {
        return credentials;
    }

    @DataBoundSetter
    public void setCredentials(List<CredentialConfiguration> credentials) {
        this.credentials = credentials;
    }

    public GlobalConfiguration() {
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        super.configure(req, json);
        save();
        return true;
    }
}
