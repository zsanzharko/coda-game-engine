package kz.zsanzharrko.utils;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceConfiguration {
  @Getter
  private final Properties datasourceConfig = new Properties();
  public DataSourceConfiguration() throws IOException {
    InputStream inputStream = DataSourceConfiguration.class.getClassLoader()
            .getResourceAsStream("application.properties");
    datasourceConfig.load(inputStream);
  }
}
