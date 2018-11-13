package be.cytomine.descriptor.data;

import java.util.Map;

/**
 * Created by Romain on 13-11-18.
 * This is a class.
 */
public interface Mappable {
    Map<String, Object> toFullMap();
}
