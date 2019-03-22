import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;

public class WaitForMySql extends DefaultTask {

  @TaskAction
  public void waitForMySql() {
    System.out.println("Waiting for MySql...");

    loadDriver();

    waitForConnection();
  }

  private String getenv(String name, String defaultValue) {
    return Optional.ofNullable(System.getenv(name)).orElse(defaultValue);
  }
  private String getenv(String name, Supplier<String> defaultValue) {
    return Optional.ofNullable(System.getenv(name)).orElseGet(defaultValue);
  }

  private void loadDriver() {
    try {

      String datasourceDriverClassName = getenv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "com.mysql.jdbc.Driver");
      System.out.println("Trying to initialize driver: " + datasourceDriverClassName);
      Class.forName(datasourceDriverClassName);

      System.out.println("Initialization succeed");
    } catch (ClassNotFoundException e) {
      System.out.println("Initialization failed");

      throw new RuntimeException(e);
    }
  }

  private void waitForConnection() {
    while (true) {
      Connection connection = null;
      try {

        String datasourceUrl = getenv("SPRING_DATASOURCE_URL", () -> String.format("jdbc:mysql://%s/eventuate?useSSL=true", getenv("DOCKER_HOST_IP", "localhost")));

        System.out.println("Trying to connect to " + datasourceUrl + " ...");

        String datasourceUsername = getenv("SPRING_DATASOURCE_USERNAME", "mysqluser");
        String datasourcePassword = getenv("SPRING_DATASOURCE_PASSWORD", "mysqlpw");

        connection = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);

        System.out.println("Connection succeed");

        break;
      } catch (SQLException e) {
        System.out.println("Connection failed");
        e.printStackTrace();

        sleep();
      } finally {
        if (connection != null) {
          closeConnection(connection);
        }
      }
    }
  }

  private void closeConnection(Connection connection) {
    try {
      System.out.println("Trying to close connection");

      connection.close();

      System.out.println("Connection closed");
    } catch (SQLException e) {
      System.out.println("Failed to close connection, see stack trace:");

      e.printStackTrace();
    }
  }

  private void sleep() {
    System.out.println("sleeping for 5 seconds...");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
