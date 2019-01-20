/* Name: Fahim Zubaer
Course: CNT 4714 – Fall 2017 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: November 12, 2017
*/

/**
 *
 * @author fahim
 */


import java.sql.ResultSet;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;

public class Logic extends HttpServlet
{
  int x = 0;
  
  protected void Logic(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
          
  {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    
    String requestString = request.getParameter("userInput");
    requestString = requestString == null ? "" : requestString;
    
    try
    {
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
      out.println("   \"http://www.w3.org/TR/html4/loose.dtd\">");
      out.println("");
      out.println("<html>");
      out.println("    <head>");
      out.println("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
      out.println("           <title>Remote Database Management System</title>");
      out.println("<style type=\"text/css\">");
      out.println("body {                        ");
      out.println("   text-align: center;        ");
      out.println("   color: white;              ");
      out.println("}                             ");
      out.println("textarea {                    ");
      out.println("   background-color: orange;  ");
      out.println("}                             ");
      out.println("                              ");
      out.println("table.error{                  ");
      out.println("   margin: auto;              ");
      out.println("   border-width: 2px;         ");
      out.println("}                             ");
      out.println("td.error{                     ");
      out.println("   background-color: red;     ");
      out.println("   border-width: 1px;         ");
      out.println("   font-size: larger;         ");
      out.println("   color: white;              ");
      out.println("}                             ");
      out.println("                              ");
      out.println("table.update{                 ");
      out.println("   margin: auto;              ");
      out.println("   border-width: 2px;         ");
      out.println("}                             ");
      out.println("td.update{                    ");
      out.println("   background-color: lime;    ");
      out.println("   border-width: 5px;         ");
      out.println("   font-size: larger;         ");
      out.println("   color: black;              ");
      out.println("   font-weight:bold           ");
      out.println("}                             ");
      out.println("td.update2{                   ");
      out.println("   background-color: lime;    ");
      out.println("   border-width: 1px;         ");
      out.println("   font-size: larger;         ");
      out.println("   font-weight:bold           ");
      out.println("   color: white;              ");
      out.println("}                             ");
      out.println("                              ");
      out.println("table.result{                 ");
      out.println("   margin: auto;              ");
      out.println("   border-width: 2px;         ");
      out.println("}                             ");
      out.println("td.resultA{                   ");
      out.println("   background-color: silver;  ");
      out.println("   border-width: 1px;         ");
      out.println("   color: black;              ");
      out.println("}                             ");
      out.println("td.resultB{                   ");
      out.println("   background-color: white;   ");
      out.println("   border-width: 1px;         ");
      out.println("   color: black;              ");
      out.println("}                             ");
      out.println("th.result{                    ");
      out.println("   background-color: red;     ");
      out.println("   border-width: 1px;         ");
      out.println("   font-weight: bold;         ");
      out.println("   color: black;              ");
      out.println("}                             ");
      out.println("</style>");
      out.println("    </head>");
      out.println("    <body bgcolor=\"Blue \">");
      out.println("           <h1>Welcome to the Project 4 Remote Database Management System</h1>");
      out.println("           <hr>");
      out.println("           <p>You are connected to the Project 4 database.<br>");
      out.println("           Please enter any valid SQL query or update statement.<br>");
      out.println("           If no query/update command is given the Execute button will display all suppliers information in the database.<br>");
      out.println("           All execution results will appear below.</p>");
      out.println("           <form method=\"POST\" action=\"logic\" name=\"logic\">");
      out.println("                   <textarea id=\"textarea\" name=\"userInput\" cols=\"80\" rows=\"10\">" + requestString + "</textarea>");
      out.println("                   <br/>");
      out.println("                   <input type=\"submit\" value=\"Execute Command\" name=\"execute\"></button>");
      out.println("                   <input type=\"reset\" value=\"Clear Form\" name=\"clear\"></button>");
      out.println("           </form>");
      out.println("           <hr>");
      out.println("           <h3>Database Results:</h3>");
      out.println(ResultsTable(requestString));
      
      this.x = 1;
      
      out.println("    </body>");
      out.println("</html>");
      
    }
    finally
    {
      out.close();
    }
  }
  
  private String ResultsTable(String requestString)
    throws SQLException
          
  {
    if (this.x == 0) {
      return "";
    }
    if ((requestString == null) || (requestString.isEmpty())) {
      requestString = "select * from suppliers";
    }
    return Query(requestString);
  }
  
  private boolean CmdQuery(String cmd)
  {
    String[] lines = cmd.split("[\r\n]");
    for (String line : lines)
    {
      line = line.trim();
      if (!line.startsWith("#")) {
        return (line.startsWith("select")) || (line.startsWith("show"));
      }
    }
    return false;
  }
  
  private String Query(String cmd)
    throws SQLException
          
  {
    database db = new database();
    try
    {
      db.connect("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/project4", "root", "root");
    }
    catch (Exception ex)
    {
      db.disconnect();
      return formatError(ex);
    }
    
    boolean commandTypeIsQuery = CmdQuery(cmd.toLowerCase());
    ResultSet result = null;
    int updateResult = 0;
    int updateResult2 = 0;
    
    this.x = 1;
    if (commandTypeIsQuery) {
      result = db.Query(cmd);
    } else {
      try
      {
        String supplierSnum = "";
        boolean updateSupplier = false;
        if (cmd.toLowerCase().contains("insert into shipments"))
        {
          int first = cmd.indexOf("(");
          int last = cmd.indexOf(")");
          String temp = cmd.substring(first + 1, last);
          temp = temp.replaceAll("'", "");
          temp = temp.replaceAll(" ", "");
          String[] brokenString = temp.split(",");
          for (String word : brokenString) {
            try
            {
              if (Integer.valueOf(word).intValue() >= 100) {
                updateSupplier = true;
              }
            }
            catch (NumberFormatException e)
            {
              if (word.startsWith("S")) {
                supplierSnum = word;
              }
            }
          }
        }
        
        if (cmd.toLowerCase().contains("update shipments"))
        {
          ResultSet updateTest = null;
          updateTest = db.Query("select * from shipments where quantity < 90 and pnum = 'P3'");
          if (updateTest.next())
          {
            updateTest.beforeFirst();
            updateSupplier = true;
          }
        }
        if (updateSupplier)
        {
          int y = 0;int prep = 0;
          y = db.Update("create table beforeShipments like shipments");
          y = 0;
          y = db.Update("insert into beforeShipments select * from shipments");
          y = 0;
          
          updateResult = db.Update(cmd);
          
          prep = db.Update("update suppliers set status = status + 5 where snum in (select distinct snum from shipments left join beforeShipments using (snum, pnum, jnum, quantity) where beforeShipments.snum is null);");
          
          y = db.Update("DROP TABLE beforeShipments");
          
          return formatUpdateResult(updateResult, prep);
        }
        updateResult = db.Update(cmd);
        
        return formatUpdateResult(updateResult);
      }
      catch (SQLException ex)
      {
        return formatError(ex);
      }
    }
    try
    {
      return formatQueryResult(result);
    }
    catch (SQLException ex)
    {
      return formatError(ex);
    }
    finally
    {
      try
      {
        result.close();
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
      }
      db.disconnect();
    }
  }
  
  private String formatQueryResult(ResultSet result)
    throws SQLException
          
  {
    StringBuilder sb = new StringBuilder();
    ResultSetMetaData metaData = result.getMetaData();
    int columnCount = metaData.getColumnCount();
    
    sb.append("             <table class=\"result\" border=\"2\">");
    sb.append("                     <tr>");
    for (int i = 1; i <= columnCount; i++)
    {
      sb.append("<th class=\"result\">");
      sb.append(metaData.getColumnName(i));
      sb.append("</th>");
    }
    while (result.next())
    {
      sb.append("<tr>");
      for (int i = 1; i <= columnCount; i++)
      {
        sb.append("<td class=\"" + (result.getRow() % 2 == 0 ? "resultB" : "resultA") + "\">");
        Object obj = result.getObject(i);
        if (obj == null) {
          sb.append("<i>NULL</i>");
        } else {
          sb.append(obj.toString());
        }
        sb.append("</td>");
      }
      sb.append("</tr>");
    }
    sb.append("             </table>");
    
    return sb.toString();
  }
  
  private String formatUpdateResult(int updateResult)
  {
    return "               <table class=\"update\" border=\"2\">                       <tr>                               <td class=\"update\">                                       The statement executed successfully.<br />" + updateResult + " row(s) affected.<br/>" + "                               </td>" + "                       </tr>" + "               </table>";
  }
  
  private String formatUpdateResult(int updateResult, int prep)
  {
    return "               <table class=\"update\" border=\"2\">                       <tr>                               <td class=\"update\">                                       The statement executed successfully.<br />" + updateResult + " row(s) affected.<br/>" + "                               </td>" + "                       </tr>" + "                       <tr>" + "                               <td class=\"update2\"><strong>" + "                                       Business Logic Detected - Updating Supplier Status<br /><br />" + "                                       Businesss Logic updated " + prep + " supplier status marks.</strong>" + "                                </td>" + "                       </tr>" + "               </table>";
  }
  
  private String formatError(Exception ex)
  {
    return "<table class=\"error\" border=\"2\">       <tr>               <td class=\"error\">                       <b>Error executing the SQL statement:</b><br />" + ex.getMessage() + "               </td>" + "       </tr>" + "</table>";
  }
  
   
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
          
  {
    try
    {
      Logic(request, response);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      Logic(request, response);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  
}
