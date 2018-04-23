package com.rackspace.jenkins.plugins.passwordsafe;

import java.util.Map;

public class PasswordSafeProject {

    private static final String DESCRIPTION_KEY = "description";
    private static final String ID_KEY= "id";
    private static final String NAME_KEY = "name";

    private Map<String, Object> project;

    public Map<String, Object> getProject() {
        return project;
    }

    public void setProject(Map<String, Object> project) {
        this.project = project;
    }

    public String getDescription() {
        return (String)project.get(DESCRIPTION_KEY);
    }

    public Double getId() {
        return (Double)project.get(ID_KEY);
    }

    public String getName() {
        return (String)project.get(NAME_KEY);
    }

    @Override
    public String toString() {
        return "[PasswordSafeProject: id=" + getId() +", name=" + getName() + ", description="+ getDescription() + "]";
    }
}
