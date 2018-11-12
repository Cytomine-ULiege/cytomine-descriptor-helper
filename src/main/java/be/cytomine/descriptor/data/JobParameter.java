package be.cytomine.descriptor.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Romain on 12-11-18.
 * This is a class.
 */
public class JobParameter {
    private String id;
    private String name;
    private String description;
    private String type;
    private String defaultValue;
    private boolean optional;
    private boolean setByServer;

    public JobParameter(String id, String name, String description, String type, String defaultValue, boolean optional, boolean setByServer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
        this.optional = optional;
        this.setByServer = setByServer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean getOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean getSetByServer() {
        return setByServer;
    }

    public void setSetByServer(boolean setByServer) {
        this.setByServer = setByServer;
    }

    public static List<JobParameter> getDefaultCytomineParameters() {
        ArrayList<JobParameter> jobParameters = new ArrayList<>();
        jobParameters.add(new JobParameter("cytomine_host", "Cytomine host", "Cytomine server hostname", "String", null, false, true));
        jobParameters.add(new JobParameter("cytomine_public_key", "Cytomine public key", "Cytomine public key", "String", null, false, true));
        jobParameters.add(new JobParameter("cytomine_private_key", "Cytomine private key", "Cytomine private key", "String", null, false, true));
        jobParameters.add(new JobParameter("cytomine_id_project", "Cytomine project id", "Cytomine project id", "Number", null, false, true));
        jobParameters.add(new JobParameter("cytomine_id_software", "Cytomine software id", "Cytomine software id", "Number", null, false, true));
        return jobParameters;
    }
}


