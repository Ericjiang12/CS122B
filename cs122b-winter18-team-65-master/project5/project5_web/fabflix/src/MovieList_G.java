import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
 * Servlet implementation class MovieList
 */
@WebServlet("/MovieList_G")
public class MovieList_G extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList_G() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String gen = request.getParameter("genre");
        
        try {
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");     
            Connection dbcon = ds.getConnection();
            Statement statement = dbcon.createStatement();
            
            String query = "SELECT  movies.*,GROUP_CONCAT(DISTINCT genres.name) AS ge_n, GROUP_CONCAT(DISTINCT st.name) AS star_n " + 
            		"FROM moviedb.movies JOIN moviedb.genres_in_movies on genres_in_movies.movieId=movies.id " + 
            		"JOIN moviedb.genres on genres_in_movies.genreId=genres.id, " + 
            		"moviedb.stars AS st, moviedb.stars_in_movies AS sim " + 
            		"WHERE movies.id=sim.movieId AND sim.starId=st.id AND movies.id in (select movies.id " + 
            		"FROM movies, moviedb.genres_in_movies AS gimm, moviedb.genres AS genn " + 
            		"WHERE genn.name = '" + gen+ "' AND  gimm.movieId=movies.id AND gimm.genreId=genn.id) " + 
            		"GROUP BY movies.id, movies.title, movies.year, movies.director ";

            ResultSet rs = statement.executeQuery(query); 
            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_genres = rs.getString("ge_n");
                String movie_stars = rs.getString("star_n");
                
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_genres", movie_genres);
                jsonObject.addProperty("movie_stars", movie_stars);
                
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
