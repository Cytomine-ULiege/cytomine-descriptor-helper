package be.cytomine.descriptor.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;
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
        this.parameters = checkDefaultCytomineParameters(parameters);
    }

    private List<JobParameter> checkDefaultCytomineParameters(List<JobParameter> parameters) {
        List<JobParameter> defaultParameters = JobParameter.getDefaultCytomineParameters();

        Set<String> defaultIds = new HashSet<>();
        for (JobParameter defaultParam: defaultParameters) {
            defaultIds.add(defaultParam.getName());
        }

        // filter default parametrs
        defaultParameters.addAll(parameters.stream()
                .filter(param -> !defaultIds.contains(param.getName()))
                .collect(Collectors.toList()));
        return defaultParameters;
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
            builder.append(param.getId().toUpperCase());
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

    public static Software fromMap(Map<String, Object> map) {
        // parse
        String image = (String)((LinkedTreeMap<String, Object>)map.get("container-image")).get("image");
        String[] splittedImage = image.split("/");
        String dockerHub = splittedImage[0];
        String name = (String)map.get("name");
        String cli = (String)map.get("command-line");
        String[] splittedCli = cli.split(" +");
        // dirty : try to get prefix by deducing lowercased name from image name. If ill defined take two first characters of image name as prefix
        String[] prefixOrFull = splittedImage[1].split(name.toLowerCase());
        String prefix = prefixOrFull.length != 1 || prefixOrFull[0].length() == splittedImage[1].length() ? splittedImage[1].substring(0, 2) : prefixOrFull[0];

        List<Object> mapParams = (List<Object>)map.get("inputs");
        List<JobParameter> parameters = mapParams.stream()
                .map(p -> JobParameter.fromMap((LinkedTreeMap<String, Object>)p))
                .collect(Collectors.toList());
        return new Software(
            (String)map.get("name"),
            dockerHub,
            prefix,
            (String)map.get("schema-version"),
            (String)map.getOrDefault("description", ""),
            splittedCli[0],
            splittedCli[1],
            parameters
        );
    }

    public List<JobParameter> getParameters() {
        return parameters;
    }
}
