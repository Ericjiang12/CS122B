
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class MovieList
 */
@WebServlet("/ft_search")
public class ft_search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public ft_search() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "root";
        String loginPasswd = "pat10901";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("application/json"); // Response mime type
        JsonArray jsonArray = new JsonArray();
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        String key_word = request.getParameter("query");
        if (key_word == null || key_word.trim().isEmpty()) {
			response.getWriter().write(jsonArray.toString());
			return;
		}	
        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();
            //for(int i=0; i<)
            String mid_query="";
            String [] key_words = key_word.split(" ");
            for (String a: key_words)
            {
            	mid_query+='+';
            	mid_query+=a;
            	mid_query+="* ";
            	
            }
            String m_query = "SELECT title FROM movies WHERE (MATCH (title) AGAINST ('";
            String s_query = "SELECT name FROM stars WHERE (MATCH (name) AGAINST ('";
            
            int count_number=0;
            if(key_word.length()<3)
            	count_number=1;
            else if (key_word.length()>7)
            	count_number=3;
            else
            	count_number=2;
            String c_number = Integer.toString(count_number);
            
            String fuzzy_query=" OR (ed(";
            String fuzzy_query_end_m=",title) <="
            		+c_number+ "));";
            String fuzzy_query_end_s=",name) <="
            		+c_number+ ")); "; 
            String fuzzy_query_middle="'"+key_word+"'";
            String fuzzy_query_final_m=fuzzy_query+fuzzy_query_middle+fuzzy_query_end_m;
            
            String fuzzy_query_final_s=fuzzy_query+fuzzy_query_middle+fuzzy_query_end_s;
            
            String like_query_m="OR (title like '%"+key_word+"%')";
            String like_query_s="OR (name like '%"+key_word+"%')";
            
            String end_query="' IN BOOLEAN MODE)";
            String movie_query=m_query+mid_query+end_query+like_query_m+fuzzy_query_final_m;
            
            String star_query=s_query+mid_query+end_query+like_query_s+fuzzy_query_final_s;
            
           
            System.out.print(movie_query);
            
            System.out.print(star_query);
            
           
            
            // Perform the query
            //System.out.print(final_query);
            ResultSet rs = statement.executeQuery(movie_query);
            
            int count=1;
            //Vector<String> movie_list;
            // Iterate through each row of rs
            while (rs.next()) {
            	 if (count >10)
                 {
                 	break;	
                 }
                String movie_title = rs.getString("title");
                System.out.print(movie_title);
                jsonArray.add(generateJsonObject(movie_title, "movie"));
                count++;
                //jsonArray.add(jsonObject);
               
                }
            
            rs = statement.executeQuery(star_query);
            while (rs.next()) {
            	  if (count >10)
                  {
                  	break;	
                  }
                String star_name = rs.getString("name");
                System.out.print(star_name);
                jsonArray.add(generateJsonObject(star_name, "star"));
                count++;
                //jsonArray.add(jsonObject);
              
                
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
	private static JsonObject generateJsonObject(String movie_title, String categoryName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", movie_title);
		//jsonObject.addProperty("category", categoryName);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", categoryName);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
