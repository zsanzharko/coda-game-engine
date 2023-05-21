package kz.zsanzharrko.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {

  public static Properties getPropertiesFromFile(String srcPath) throws IOException {
    InputStream inputStream =
            FileUtils.class.getClassLoader()
                    .getResourceAsStream(srcPath);
    Properties properties = new Properties();
    properties.load(inputStream);

    assert inputStream != null;
    inputStream.close();

    return properties;
  }
}
