package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SystemPropertyUtil {
    public final static String BASE_API_URL = "baseApiUrl";
    public final static String VERSION = "version";
    public final static String INCLUDE_GROUPS = "includeGroups";
    public final static String EXCLUDE_GROUPS = "excludeGroups";

    public static void loadAllPropsFromFiles() {
        CustomFileReader customFileReader = new CustomFileReader();
        Properties systemProps = customFileReader.readPropFile("system.properties");
        if (!systemProps.isEmpty()) {
            setDefaultValueForSystemProperty(BASE_API_URL, systemProps);
            setDefaultValueForSystemProperty(VERSION, systemProps);
        }
    }

    /**
     * @param propertyName Name of the system parameter
     * @param value        the default value that should be set for that property if it's not already set
     */
    private static void setDefaultValueForSystemProperty(String propertyName, String value) {
        if (System.getProperty(propertyName) == null) {
            System.setProperty(propertyName, value);
        }
    }

    private static void setDefaultValueForSystemProperty(String propName, Properties propertyFile) {
        setDefaultValueForSystemProperty(propName, propertyFile.getProperty(propName));
    }

    private static String getPropertyValue(String propertyName) {
        return System.getProperty(propertyName);
    }

    public static String getBaseApiUrl() {
        return getPropertyValue(BASE_API_URL);
    }

    public static String getVersion() {
        return getPropertyValue(VERSION);
    }


    public static List<String> getIncludeGroups() {
        if (System.getProperties().containsKey(INCLUDE_GROUPS)) {
            return Arrays.asList(getPropertyValue(INCLUDE_GROUPS).split("/"));
        }
        return new ArrayList<>();
    }

    public static List<String> getExcludeGroups() {
        if (System.getProperties().containsKey(EXCLUDE_GROUPS)) {
            return Arrays.asList(getPropertyValue(EXCLUDE_GROUPS).split("/"));
        }
        return new ArrayList<>();
    }

}
