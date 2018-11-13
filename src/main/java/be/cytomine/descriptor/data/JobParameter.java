package be.cytomine.descriptor.data;

import be.cytomine.descriptor.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Romain on 12-11-18.
 * This is a class.
 */
public class JobParameter implements Mappable{
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("type", this.type);
        addFormattedDefaultValueByType(map);
        map.put("optional", this.optional);
        map.put("set-by-server", this.setByServer);
        return map;
    }

    private void addFormattedDefaultValueByType(Map<String, Object> m) {
        String key = "default-value";
        if (defaultValue == null) {
            m.put(key, null);
            return;
        }
        switch(type) {
            case "Number":
                try {
                    m.put(key, Integer.parseInt(defaultValue));
                } catch (NumberFormatException e) {
                    m.put(key, Double.parseDouble(defaultValue));
                }
                break;
            case "Domain":
                m.put(key, Integer.parseInt(defaultValue));
                break;
            case "Boolean":
            case "String":
            case "ListDomain":
            case "Date":
                m.put(key, defaultValue);
                break;
        }
    }

    public Map<String, Object> toFullMap() {
        HashMap<String, Object> map = new HashMap<>(toMap());
        map.put("value-key", "@ID");
        map.put("command-line-flag", "--@id");
        return map;
    }

    public static JobParameter fromMap(Map<String, Object> m) {
        Object v = m.getOrDefault("default-value", null);
        String defaultValue = v == null ? null : StringUtil.removeTrailingZeros(v.toString());
        return new JobParameter(
            (String)m.get("id"),
            (String)m.get("name"),
            (String)m.getOrDefault("description", ""),
            (String)m.get("type"),
            defaultValue,
            (Boolean)m.getOrDefault("optional", true),
            (Boolean)m.getOrDefault("set-by-server", false)
        );
    }
}


