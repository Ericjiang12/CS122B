

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class SingleStar
 */
@WebServlet("/SingleStar")
public class SingleStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleStar() {
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

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        System.out.println(name);
        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();
            System.out.println(name);
            String query = "SELECT stars.name, stars.birthYear, GROUP_CONCAT(movies.title) as mvs " + 
            		"FROM movies, stars, stars_in_movies as sim " + 
            		"WHERE stars.name = '"+ name + "' AND stars.id=sim.starId AND sim.movieId=movies.id " + 
            		"GROUP BY stars.name, stars.birthYear";
            System.out.println(query);
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            System.out.println(name);
            // Iterate through each row of rs
            rs.next();
            String star_name = rs.getString("name");
            String star_year = rs.getString("birthYear");
            String star_movies = rs.getString("mvs");
                
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("star_name", star_name);
            jsonObject.addProperty("star_year", star_year);
            jsonObject.addProperty("star_movies", star_movies);

            out.write(jsonObject.toString());

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
