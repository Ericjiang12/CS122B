import java.io.*;
import java.sql.*;

import java.io.IOException;
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
@WebServlet("/MovieList_S")
public class MovieList_S extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList_S() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String nn = request.getParameter("name");
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String loginUser = "root";
	    String loginPasswd = "pat10901";
	    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
	    try {
	    Class.forName("com.mysql.jdbc.Driver").newInstance();

        Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        // Declare our statement
        Statement statement = dbcon.createStatement();
        String end_q = " GROUP BY movies.id, movies.title, movies.year, movies.director; ";
		
        String query = "SELECT  movies.id, title, year, director,GROUP_CONCAT(DISTINCT ge.name) as ge_n, GROUP_CONCAT(DISTINCT st.name) as star_n\r\n" + 
				"FROM moviedb.movies, moviedb.stars as st, moviedb.stars_in_movies as sim, \r\n" + 
				"moviedb.genres as ge, moviedb.genres_in_movies as gim\r\n" + 
				"where movies.id=sim.movieId AND sim.starId=st.id AND gim.movieId=movies.id AND gim.genreId=ge.id ";
		
		if (title != null && !title.isEmpty())
		{
			String title_q=" AND title like ";
			query+=title_q+"'%"+title+"%' ";
			
		}
		if (year != null && !year.isEmpty())
		{
			String year_q=" AND year like ";
			query+=year_q+"'"+year+"' ";
			
		}
		if (director != null && !director.isEmpty())
		{
			String director_q=" AND director like ";
			query+=director_q+"'%"+director+"%' ";
			
		}
		if (nn != null && !nn.isEmpty())
		{
			String nn_q=" AND name like ";
			query+=nn_q+"'%"+nn+"%' ";
			
		}
		query+=end_q;
		
		
        ResultSet rs = statement.executeQuery(query);
	
		boolean rs_exist=false;
		JsonArray jsonay= new JsonArray();
        while (rs.next()) {
			// login success:
			rs_exist=true;
			
			String movie_id = rs.getString("id");
            String movie_title = rs.getString("title");
            String movie_year = rs.getString("year");
            String movie_director = rs.getString("director");
            String ge_list = rs.getString("ge_n");
            String st_list = rs.getString("star_n");

			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("movie_id", movie_id);
			responseJsonObject.addProperty("movie_title", movie_title);
			responseJsonObject.addProperty("movie_year", movie_year);
			responseJsonObject.addProperty("movie_director", movie_director);
			responseJsonObject.addProperty("movie_genres", ge_list);
			responseJsonObject.addProperty("movie_stars", st_list);
			jsonay.add(responseJsonObject);
			//response.getWriter().write(responseJsonObject.toString());
		} 
		
        if(!rs_exist) {
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "fail");
			responseJsonObject.addProperty("message", "no matching movie");
			response.getWriter().write(responseJsonObject.toString());
		}
        else
        {
        	System.out.println("send movie out");
        	out.write(jsonay.toString());
        	
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
          }  // end catc
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
