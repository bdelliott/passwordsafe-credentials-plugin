package com.rackspace.jenkins.plugins.passwordsafe.configuration;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.rackspace.jenkins.plugins.passwordsafe.PasswordSafeClient;
import com.rackspace.jenkins.plugins.passwordsafe.PasswordSafeProject;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.acegisecurity.Authentication;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import sun.security.util.Password;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holder class for a credential used with Password Safe itself and any associated configuration metadata.
 */
public class CredentialConfiguration implements Describable<CredentialConfiguration> {

    private String credentialsId;

    private static final Logger LOG = Logger.getLogger(CredentialConfiguration.class.getName());

    @DataBoundConstructor
    public CredentialConfiguration(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    @Override
    public Descriptor<CredentialConfiguration> getDescriptor() {
        return new DescriptorImpl();
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<CredentialConfiguration> {

        public ListBoxModel doFillCredentialsIdItems(
            @AncestorInPath Item item,
            @QueryParameter String credentialsId) {

            StandardListBoxModel standardListBoxModel = new StandardListBoxModel();

            if (item == null) {
                if (!Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER)) {
                    return standardListBoxModel.includeCurrentValue(credentialsId);
                }
            } else {
                if (!item.hasPermission(Item.EXTENDED_READ)
                        && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
                    return standardListBoxModel.includeCurrentValue(credentialsId);
                }
            }

            standardListBoxModel = (StandardListBoxModel) standardListBoxModel.includeMatchingAs(
                    ACL.SYSTEM,
                    item,
                    StandardUsernamePasswordCredentials.class,
                    Collections.<DomainRequirement>emptyList(),
                    CredentialsMatchers.always()
            ).includeCurrentValue(credentialsId);

            return standardListBoxModel;

        }

        public static class ProjectListResponse {
            public boolean success;
            public String error;
            public List<PasswordSafeProject> projects;
        }

        /**
         * Method to dynamically populate the list of projects accessible to a particular PasswordSafe user.
         *
         * @param credentialId  credential id of password safe user
         * @return list of projects
         */
        @JavaScriptMethod
        public ProjectListResponse getProjects(String credentialId) {

            final ProjectListResponse response = new ProjectListResponse();

            // use credentials API to access the given credential id
            List<StandardUsernamePasswordCredentials> credentials = CredentialsProvider.lookupCredentials(
                    StandardUsernamePasswordCredentials.class,
                    (Item) null,
                    ACL.SYSTEM,
                    Collections.emptyList()
            );
            credentials = CredentialsMatchers.filter(credentials, CredentialsMatchers.withId(credentialId));

            if (credentials.size() == 0) {
                response.success = false;
                response.error = "No credentials found matching id: " + credentialId;
                return response;
            }

            if (credentials.size() > 1) {
                response.success = false;
                response.error = "Too many (" + credentials.size() + ") credentials found matching id: " + credentialId;
                return response;
            }

            final StandardUsernamePasswordCredentials credential = credentials.get(0);

            try {
                final PasswordSafeClient client = new PasswordSafeClient(
                        credential.getUsername(),
                        credential.getPassword().getPlainText()
                );
                final List<PasswordSafeProject> projects = client.projectsList();

                response.success = true;
                response.projects = projects;

            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to retrieve password safe project list", e);
                response.success = false;
                response.error = "Failed to retrieve password safe project list: " + e.getMessage();
            }

            return response;
        }
    }
}