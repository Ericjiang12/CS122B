import java.time.ZoneId;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
/**
 * Servlet implementation class ConformInfo
 */
@WebServlet("/ConfirmInfo")
public class ConfirmInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("application/json");
		
		String first_name = request.getParameter("fn");
		String last_name = request.getParameter("ln");
		String ccid = request.getParameter("ccid");
		String exp_date = request.getParameter("exp_date");
		
		String loginUser = "root";
        String loginPasswd = "pat10901";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        try
        {
           Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           Statement statement = dbcon.createStatement();
           String query = "SELECT * FROM creditcards WHERE firstName = '" + first_name + "' AND lastName = '"+ last_name + "'"
        		   +" AND id = '"+ccid+"'"
        		   +" AND expiration = '"+exp_date+"'";

           // Perform the query
           ResultSet rs = statement.executeQuery(query);
           if (rs.next()){
        	   		
        	   		HttpServletRequest httpRequest = (HttpServletRequest) request;
        	   		User user = (User) httpRequest.getSession().getAttribute("user");
        	   		/*get user id*/
        	   		String user_email= user.getUsername();
        	   		String ID_query="SELECT id FROM customers WHERE email ='"+user_email+"'";
        	   		ResultSet cust_id=statement.executeQuery(ID_query);
        	   		
        	   		if(cust_id.next())
        	   		{
        	   			String customerid = cust_id.getString("id");
        	   		
        	   			/*get date*/
        	   			Date date = new Date();
        	   			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	   			int year  = localDate.getYear();
        	   			int month = localDate.getMonthValue();
        	   			int day   = localDate.getDayOfMonth();
            	   		String today=year+"-"+month+"-"+day;
            	   		
        			Vector<CartItem> cart = user.getCart();
        			for(int i=0; i < cart.size(); ++ i) {
        				
        				/*get movie id*/
        				String movie_tt=cart.get(i).getTitle();
        				String movie_query="SELECT id FROM movies WHERE title ='"+movie_tt+"'";
        				ResultSet movie_id=statement.executeQuery(movie_query);
            	   		if(movie_id.next())
            	   		{
            	   			String movieid = movie_id.getString("id");
            	   		
            	   			String sub_qury = "INSERT INTO sales (customerId, movieid, saleDate) VALUES (";
    						
            	   			sub_qury+="'"+customerid+"',";
    						sub_qury+="'"+movieid+"',";
    						sub_qury+="'"+today+"')";
    						System.out.println(sub_qury);
    						statement.executeUpdate(sub_qury);
    						
            	   		}
        			
        				
        			}
        	   		
        	   		}
        	   		
        	   		JsonObject responseJsonObject = new JsonObject();
   				responseJsonObject.addProperty("status", "success");
   				responseJsonObject.addProperty("message", "success");
   				response.getWriter().write(responseJsonObject.toString());
           }
           else {
        	   		JsonObject responseJsonObject = new JsonObject();
        	   		responseJsonObject.addProperty("status", "fail");
   				responseJsonObject.addProperty("message", "provided info is incorrect");
   				response.getWriter().write(responseJsonObject.toString());
           }
           rs.close();
           statement.close();
           dbcon.close();
         }
     catch (SQLException ex) {
           while (ex != null) {
                 System.out.println ("SQL Exception:  " + ex.getMessage ());
                 ex = ex.getNextException ();
             }  // end while
         }  // end catch SQLException

     catch(java.lang.Exception ex)
         {
             return;
         }	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
