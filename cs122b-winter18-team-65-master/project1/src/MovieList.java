

import java.io.IOException;
import java.io.PrintWriter;
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

/**
 * Servlet implementation class MovieList
 */
@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "root";
        //String loginPasswd = "zj4395438";
        String loginPasswd = "pat10901";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Top 20 rated movies</TITLE></HEAD>");
        out.println("<BODY><H1>Top Twenty</H1>");

        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "SELECT  title, year, director,GROUP_CONCAT(DISTINCT ge.name) AS ge_n, \n" + 
            		"GROUP_CONCAT(DISTINCT st.name) as star_n, rating \n" + 
            		"FROM moviedb.ratings, moviedb.movies, \n" + 
            		"moviedb.stars as st, \n" + 
            		"moviedb.stars_in_movies as sim, \n" + 
            		"moviedb.genres as ge, \n" + 
            		"moviedb.genres_in_movies as gim\n" + 
            		"where movies.id=ratings.movieId \n" + 
            		"AND movies.id=sim.movieId \n" + 
            		"AND sim.starId=st.id \n" + 
            		"AND gim.movieId=movies.id \n" + 
            		"AND gim.genreId=ge.id\n" + 
            		"group by movies.title, movies.year, movies.director, ratings.rating\n" + 
            		"ORDER BY rating DESC\n" + 
            		"limit 20;";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            out.println("<TABLE border>");
            out.println("<tr>" + "<td>" + "row"+ "</td>"+ "<td>"  + "title" + "</td>" + "<td>" + "year" + "</td>" + "<td>" + "director" + "</td>" 
                    + "<td>" + "genre" + "</td>"+ "<td>" + "star" + "</td>"+ "<td>" + "rating" + "</td>"+ "</tr>");
            // Iterate through each row of rs
            int row = 1;
            while (rs.next()) {
            	String m_title = rs.getString("title");
                String m_year = rs.getString("year");
                String m_dir = rs.getString("director");
                String m_gel = rs.getString("ge_n");
                String m_stl = rs.getString("star_n");
                String m_rat = rs.getString("rating");
                out.println("<tr>" + "<td>" + row++ + "</td>"+ "<td>" + m_title + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_dir + "</td>" 
                + "<td>" + m_gel + "</td>"+ "<td>" + m_stl + "</td>"+ "<td>" + m_rat + "</td>"+ "</tr>");
            }

            out.println("</TABLE>");

            rs.close();
            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            } // end while
        } // end catch SQLException

        catch (java.lang.Exception ex) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
	}

}
