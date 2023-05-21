package kz.zsanzharrko.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag(value = "integrated")
class DatabaseConnectorTest {
  private static DataSourceConfiguration configuration;

  @BeforeAll
  public static void configDatasource() throws IOException {
    configuration = new DataSourceConfiguration();
  }

  @AfterEach
   void closeConnection() {
    if (DatabaseConnector.getService() != null) {
      DatabaseConnector.getService().closeConnection();
    }
  }

  @Test
  void getInstance() throws SQLException, ClassNotFoundException {
    DatabaseConnector service = DatabaseConnector.getInstance(configuration.getDatasourceConfig());
    assertNotNull(service);
  }

  @Test
  void getConnection() throws SQLException, ClassNotFoundException {
    DatabaseConnector service = DatabaseConnector.getInstance(configuration.getDatasourceConfig());
    assertNotNull(service.getConnection());
  }

  @Test
  void closeConnectionDatasource() throws SQLException, ClassNotFoundException {
    DatabaseConnector service = DatabaseConnector.getInstance(configuration.getDatasourceConfig());
    service.closeConnection();
    assertNull(DatabaseConnector.getService());
  }

  @Test
  void getService() throws SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(configuration.getDatasourceConfig());
    DatabaseConnector service = DatabaseConnector.getService();
    assertNotNull(service);
  }
}