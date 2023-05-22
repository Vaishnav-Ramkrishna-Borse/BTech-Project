import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;
import java.io.*;

//import matrix.util.StringList;
//
//import com.matrixone.apps.domain.util.MqlUtil;
//import com.matrixone.apps.domain.util.FrameworkUtil;
//import matrix.db.*;
//import com.matrixone.apps.domain.util.EnoviaResourceBundle;

public class HTML_table {

	static StringBuilder htmlTable = new StringBuilder();

	private static StringBuilder htmlGenerator(String tableName, ArrayList<ArrayList<String>> tableData,
			ArrayList<String> htmlColumnNames) {

		htmlTable.append("  <div class=\"column\">\r\n" + "<h2>" + tableName + "</h2>"
				+ "    <table style=\"page-break-after: always;\">");
		htmlTable.append("<tr>");
		htmlTable.append("<th style=\"background-color:Lightgray;\"><b>#</b></th>");
		for (int m = 0; m < htmlColumnNames.size(); m++) {
			htmlTable.append("<th style=\"background-color:LightGray;\"><b>" + htmlColumnNames.get(m) + "</b></th>");
		}
		htmlTable.append("</tr>\n");
		for (int k = 0; k < tableData.get(0).size(); k++) {
			htmlTable.append("<tr>");
			htmlTable.append("<td >" + (k + 1) + "</td> ");

			for (int i = 0; i < tableData.size(); i++) {
				htmlTable.append("<td >" + tableData.get(i).get(k) + "</td>");
			}
			htmlTable.append("</tr>\n");

		}
		htmlTable.append(" </table>\r\n" + "  </div>");

		return htmlTable;

	}

	private static StringBuilder createHtmlTable(ResultSet rs, String tableName) throws SQLException {

		// ResultSet rs = stmt.executeQuery(sql);

		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		System.out.println(columnCount);
		ArrayList<String> columnNames = new ArrayList<String>();
		for (int i = 1; i <= columnCount; i++) {
			columnNames.add(rsMetaData.getColumnLabel(i));
		}
		System.out.println(columnNames);

		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < columnCount; i++) {
			tableData.add(new ArrayList<String>());
		}

		int j = 0;
		while (rs.next()) {

			for (int i = 0; i < columnCount; i++) {
				// ArrayList<String> arrList = new ArrayList<String>();
				tableData.get(i).add(rs.getString(rsMetaData.getColumnName(i + 1)));
			}
			j++;
			if (j == 50)
				break;
		}
		System.out.println(tableData);

		htmlTable = htmlGenerator(tableName, tableData, columnNames);

		rs.close();

		return htmlTable;

	}

	private static Statement stmt = null;
	private static Connection conn = null;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		htmlTable.append("<!DOCTYPE html>\n");
		htmlTable.append("<html><head>" + "<style>\r\n" + "* {\r\n" + "  box-sizing: border-box;\r\n" + "}\r\n" + "\r\n"
				+ ".row {\r\n" + "  margin-left:-5px;\r\n" + "  margin-right:-5px;\r\n" + "}\r\n" + "  \r\n"
				+ ".column {\r\n" + "  margin: auto;\r\n" + "  float: center;\r\n" + "  width: 90%;\r\n"
				+ "  padding: 20px;\r\n" + "}\r\n" + "\r\n" + ".row::after {\r\n" + "  content: \"\";\r\n"
				+ "  clear: both;\r\n" + "  display: table;\r\n" + "}\r\n" + "\r\n" + "th,td{\r\n"
				+ "  border-collapse: collapse;\r\n" + "  border: 1px solid #ddd;\r\n" + "  padding: 10px;\r\n"
				+ "}\r\n" + "table {\r\n" + "  border-collapse: collapse;\r\n" + "  width: 100%;\r\n"
				+ "  border: 1px solid #ddd;\r\n" + "}\r\n" + "\r\n" + "th{\r\n" + "  padding: 10px;\r\n" + "}\r\n"
				+ "\r\n" + "tr:nth-child(even) {\r\n" + "  background-color: #f2f2f2;\r\n" + "}\r\n"
				+ "</style></head>");
		htmlTable.append("<body>");
		htmlTable.append("<div class=\"row\">\r\n");
		htmlTable.append("<h1>3DEXPERIENCE Upgrade Assessment Tool:</h1>");

		try {

			final String DB_URL, USER, PASS, outputFilename;

			// JDBC_DRIVER = "org.h2.Driver";
			// System.out.println(JDBC_DRIVER);
			DB_URL = "jdbc:h2:C:/temp/test_db";
			System.out.println(DB_URL);
			USER = "sa";
			System.out.println(USER);
			PASS = "";
			System.out.println(PASS);

			outputFilename = "C:/Vaishnav files/name_count6.html";// C:/Users/Admin/Documents/name_count.html(creating
																	// new html file using
																	// property file.)
			System.out.println(outputFilename);

			FileWriter fw = new FileWriter(outputFilename);

			Class.forName("org.h2.Driver");

			System.out.println("Connecting to database...");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			System.out.println("Connected database successfully...");
//////////////////////SQL queries////////////////////////////////////////////////
			String sql1 = "SELECT distinct(\"schema name\"),count(\"Text\") as Obj_Count FROM TBL_VAISHNAV GROUP BY \"schema name\" ORDER BY Obj_Count DESC;";
			ResultSet rs = stmt.executeQuery(sql1);
			createHtmlTable(rs, "Number of modifications per schema");

			String sql2 = "SELECT * from TBL_VAISHNAV where \"Action Date\" > '2/21/2023' order by \"Action Date\"";
			ResultSet rs1 = stmt.executeQuery(sql2);
			createHtmlTable(rs1, "Customization data for relationship and policy");

			String sql3 = "Select distinct(\"schema type\"), count(distinct(\"schema name\")) from TBL_VAISHNAV where \"Action Date\" > '2/21/2023' group by \"schema type\"";
			rs1 = stmt.executeQuery(sql3);
			createHtmlTable(rs1, "Count of customized schema name for each schema type");

			String sql4 = "Select distinct(\"JPO Name\"), count(\"Function name\") from JPO_INFO group by \"JPO Name\" order by count(\"Function name\") desc";
			rs1 = stmt.executeQuery(sql4);
			createHtmlTable(rs1, "Count of functions per JPO");

			String sql5 = "SELECT count(distinct(\"JPO Name\")) FROM JPO_INFO";
			rs1 = stmt.executeQuery(sql5);
			createHtmlTable(rs1, "Count of JPOs");
///////////////////////////////////////////////////////////////////////////////////////////////////////
			htmlTable.append("</div>\r\n" + "\r\n" + "</body>\r\n" + "</html>");

			fw.write(htmlTable.toString());

			System.out.println("Data successfully fetched and copied to " + outputFilename);

			fw.close();

			rs.close();
			rs1.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");

	}
}
