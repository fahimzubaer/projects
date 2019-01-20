/* Name: Fahim Zubaer
Course: CNT 4714 – Fall 2017 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: November 12, 2017
*/
/**
 *
 * @author fahim
 */


import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class database
{
  private Connection connection = null;
   
  public void connect(String driver, String url, String user, String password)
    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
          
  {
    Class.forName(driver).newInstance();
    this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "root", "root");
  }
  
  public boolean isConnected()
  {
    if (this.connection == null) {
      return false;
    }
    
    try
    {
      return !this.connection.isClosed();
    }
    catch (SQLException ex) {}
    return false;
  }
  
  public ResultSet Query(String cmd)
    throws SQLException
          
  {
    Statement statement = this.connection.createStatement(1004, 1007);
    return statement.executeQuery(cmd);
  }
  
  
  public void disconnect()
  {
    if (this.connection != null) {
      try
      {
        this.connection.close();
      }
      catch (SQLException ex)
      {
        Logger.getLogger(database.class.getName()).log(Level.SEVERE, "Failed to close connection", ex);
      }
    }
  }
  
  public int Update(String cmd)
    throws SQLException
          
  {
    Statement statement = this.connection.createStatement();
    int result = statement.executeUpdate(cmd);
    statement.close();
    
    return result;
  }
  
  
}
