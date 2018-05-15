# passwordsafe-credentials-plugin

This repository contains a Jenkins plugin to expose Rackspace PasswordSafe credentials for use within Jenkins.

Basically, given a valid PasswordSafe user account, provide an integration function to expose PasswordSafe credentials as standard Jenkins credentials.

## Jenkins credentials

* Base credential support is from the [credential plugin](https://wiki.jenkins.io/display/JENKINS/Credentials+Plugin).  It provides a standard means of storing and accessing credentails with Jenkins.
* See the [SSH credentials plugin](https://wiki.jenkins.io/display/JENKINS/SSH+Credentials+Plugin) for an example of how to build custom credential support on top of the base credentials plugin.

## Configuration

1. Manage Jenkins => Global configuration
2. Select Jenkins credential to use to authenticate with PasswordSafe.
3. Select PasswordSafe project(s) to expose to Jenkins.

## Credential provider

Per the [implementation guide](https://github.com/jenkinsci/credentials-plugin/blob/master/docs/implementation.adoc), PasswordSafe credentials should be exposed as a new type of credential provider.

See notes on implementing a credential provider.

## Credential Store

Each enabled PasswordSafe project should be exposed to Jenkins as its own "credential store".  This will eliminate confusion over where credentials are from.

* See [com.cloudbees.plugins.credentials.CredentialsStore](https://github.com/jenkinsci/credentials-plugin/blob/master/src/main/java/com/cloudbees/plugins/credentials/CredentialsStore.java) for more information.
