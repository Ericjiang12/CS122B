import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
//		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
//		System.out.println(valid);
//		if (!valid) {
//			System.out.println("Recaptcha Failed");
//		    return;
//		}
		System.out.print("hello");
		String account = request.getParameter("account");
		System.out.print(account);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.print(password);
		String loginUser = "root";
        String loginPasswd = "pat10901";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        try
        {
           Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           Statement statement = dbcon.createStatement();
           String query;
           if (account.equals("employee"))
        	   		query = "SELECT * FROM employees WHERE email = '" + username + "' AND password = '"+ password + "'";
           else
        	   		query = "SELECT * FROM customers WHERE email = '" + username + "' AND password = '"+ password + "'";
           System.out.print(query);
           // Perform the query
           ResultSet rs = statement.executeQuery(query);
           if (rs.next()){
        	   	System.out.println("has account!");
        	   	request.getSession().setAttribute("user", new User(username));
   			JsonObject responseJsonObject = new JsonObject();
   			responseJsonObject.addProperty("status", "success");
   			responseJsonObject.addProperty("account", account);
   			response.getWriter().write(responseJsonObject.toString());
           }else {
        	   	request.getSession().setAttribute("user", new User(username));
        	   	JsonObject responseJsonObject = new JsonObject();
       		responseJsonObject.addProperty("status", "fail");
       		if (account.equals("employee"))
        	   		query =  "SELECT * FROM employees WHERE email = '" + username + "'";
       		else
       			query =  "SELECT * FROM customers WHERE email = '" + username + "'";
        	   	rs = statement.executeQuery(query);
        	   	if (! rs.next())
   				responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
   			else
   				responseJsonObject.addProperty("message", "incorrect password");
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
