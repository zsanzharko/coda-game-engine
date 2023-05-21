package kz.zsanzharrko.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseConnector {
  private static DatabaseConnector service;
  private Connection connection;

  private DatabaseConnector(Properties properties) throws SQLException, ClassNotFoundException {
    Class.forName(properties.getProperty("datasource.name"));
    String url = properties.getProperty("datasource.url");
    String username = properties.getProperty("datasource.username");
    String password = properties.getProperty("datasource.password");
    this.connection = DriverManager.getConnection(url, username, password);
  }

  public static DatabaseConnector getInstance(Properties properties) throws SQLException, ClassNotFoundException {
    if (service == null) {
      service = new DatabaseConnector(properties);
    }
    return service;
  }

  public Connection getConnection() {
    return connection;
  }

  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connection = null;
      service = null;
    }
  }

  public static DatabaseConnector getService() {
    return service;
  }
}
