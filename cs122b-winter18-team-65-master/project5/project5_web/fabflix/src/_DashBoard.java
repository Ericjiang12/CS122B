import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class _DashBoard
 */
@WebServlet("/_DashBoard")
public class _DashBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public _DashBoard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");     
            Connection dbcon = ds.getConnection();
            Statement statement = dbcon.createStatement();
            
            String query = "select table_name from information_schema.Tables where table_type = 'BASE TABLE' AND table_schema = 'moviedb'";
            ResultSet rs = statement.executeQuery(query);
            JsonArray jsonArray = new JsonArray();
            Vector<String> tables = new Vector<String>();
            while (rs.next()) 
                tables.add(rs.getString("table_name"));
            
            for (String table:tables) {
	            query = "SHOW COLUMNS FROM " + table + "";
	            rs = statement.executeQuery(query);
	            String attribute = "";
	            while (rs.next()) 
	        			attribute += rs.getString("Field") + ":" + rs.getString("Type") + ",";
	            JsonObject jsonObject = new JsonObject();
	            jsonObject.addProperty("table", table);
	            jsonObject.addProperty("attributes", attribute);
	            jsonArray.add(jsonObject);
            }
            out.write(jsonArray.toString());
            
            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + e.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}
