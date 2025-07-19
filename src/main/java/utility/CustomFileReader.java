package utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CustomFileReader {
    /**
     * Used to read .properties file
     *
     * @param path the path to the file
     * @return the file in a Properties class as to access the different properties' values
     * <p>
     * Throws runtime exception as to stop the execution of the test
     */
    public Properties readPropFile(String path) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(path));
        } catch (IOException e) {
            System.out.printf("Error while opening file: %s, exception %s%n", path, e);
        }
        return prop;
    }

    public File readFile(String path) {
        return new File(path);
    }

}
