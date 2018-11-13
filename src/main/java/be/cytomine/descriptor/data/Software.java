package be.cytomine.descriptor.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Romain on 13-11-18.
 * This is a class.
 */
public class Software implements Mappable {
    private String name;
    private String dockerHub;
    private String prefix;
    private String schemaVersion;
    private String description;
    private String executable;
    private String script;
    private List<JobParameter> parameters;

    public Software(String name, String dockerHub, String prefix, String schemaVersion, String description, String executable, String script, List<JobParameter> parameters) {
        this.name = name ;
        this.dockerHub = dockerHub ;
        this.prefix = prefix ;
        this.schemaVersion = schemaVersion ;
        this.description = description ;
        this.executable = executable ;
        this.script = script ;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDockerHub() {
        return dockerHub;
    }

    public void setDockerHub(String dockerHub) {
        this.dockerHub = dockerHub;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getCommandLine() {
        StringBuilder builder = new StringBuilder();
        builder.append(executable); builder.append(" ");
        builder.append(script); builder.append(" ");
        for (JobParameter param : parameters) {
            builder.append(param.getName().toUpperCase());
            builder.append(" ");
        }
        return builder.toString();
    }

    public Map<String, Object> toFullMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        HashMap<String, Object> containerImage = new HashMap<>();
        containerImage.put("image", dockerHub + "/" + (prefix + name).toLowerCase());
        containerImage.put("type", "singularity");
        map.put("container-image", containerImage);
        map.put("schema-version", schemaVersion);
        map.put("command-line", getCommandLine());
        map.put("description", description);
        map.put("inputs", parameters.stream().map(JobParameter::toFullMap).collect(Collectors.toList()));
        return map;
    }

}
