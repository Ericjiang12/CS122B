

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class TMovieList
 */
@WebServlet("/NFT_search")
public class NFT_search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NFT_search() {
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
        String key_word = request.getParameter("query");
        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            String mid_query="";
            String [] key_words = key_word.split(" ");
            for (String a: key_words)
            {
            	mid_query+='+';
            	mid_query+=a;
            	mid_query+="* ";
            	
            }
            
            String m_query = "SELECT title FROM movies WHERE (MATCH (title) AGAINST ('";
            String end_query="' IN BOOLEAN MODE)) ";
            String movie_query=m_query+mid_query+end_query;
            
            int count_number=0;
            if(key_word.length()<3)
            	count_number=1;
            else if (key_word.length()>7)
            	count_number=3;
            else
            	count_number=2;
            String c_number = Integer.toString(count_number);
            
            
            String fuzzy_query=" OR (ed(";
            String fuzzy_query_end=",title) <="
            		+c_number+ ") ";
            String fuzzy_query_middle="'"+key_word+"'";
            String fuzzy_query_final=fuzzy_query+fuzzy_query_middle+fuzzy_query_end;
            
            String like_query="OR (title like '%"+key_word+"%')";
            
            String query="SELECT  movies.id, title, year, director,GROUP_CONCAT(DISTINCT ge.name) as ge_n, GROUP_CONCAT(DISTINCT st.name) as star_n " + 
            		"FROM moviedb.movies, moviedb.stars as st, moviedb.stars_in_movies as sim,   " + 
            		"moviedb.genres as ge, moviedb.genres_in_movies as gim " + 
            		"where movies.id=sim.movieId AND sim.starId=st.id AND gim.movieId=movies.id " + 
            		"AND gim.genreId=ge.id AND movies.title in (";
            String end=	") GROUP BY movies.id, movies.title, movies.year, movies.director;";
           // SELECT title FROM movies WHERE MATCH (title) AGAINST ('+The* +f*' IN BOOLEAN MODE) )\r\n" + 
            String final_query=query+movie_query+like_query+fuzzy_query_final+end;	
            System.out.print(final_query);
            // Perform the query
            ResultSet rs = statement.executeQuery(final_query);
            JsonArray jsonArray = new JsonArray();
            
            // Iterate through each row of rs
            while (rs.next()) {
            	
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_genres = rs.getString("ge_n");
                String movie_stars = rs.getString("star_n");
                System.out.print(movie_title);
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
