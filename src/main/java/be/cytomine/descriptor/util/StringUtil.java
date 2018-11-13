package be.cytomine.descriptor.util;

/**
 * Created by Romain on 13-11-18.
 * This is a class.
 */
public class StringUtil {
    public static String trimornull(String s) {
        return s == null ? null : s.trim();
    }

    public static boolean nullorempty(String s) {
        return s == null || s.isEmpty();
    }

    public static String removeTrailingZeros(String s) {
        return s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static String toUpperCaseHuman(String s) {
        String[] splitted = s.split("[\\s_]+");
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String split: splitted) {
            if (i++ > 0) {
                builder.append(" ");
            }
            builder.append(capitalizeFirstLetter(split.toLowerCase()));
        }
        return builder.toString();
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
