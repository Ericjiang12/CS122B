
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainSAXParser extends DefaultHandler{
	private String tempVal;
	private String tempDirector;
	private movies tempMovie;
	static Vector<movies> movielist;
	static Vector<String> movieid;

	private int checkYear(String year) {
		if (Pattern.matches("^(17|18|19|20|21)\\d{2}$", year))
			return Integer.parseInt(year);
		return 0;
	}
	
	public MainSAXParser(){
		movielist = new Vector<movies>();
	}
	
	public void runExample() {
		parseDocument();
	}

	private void parseDocument() {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			File mainfile = new File ("mains243.xml");
			sp.parse(mainfile, this);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		tempVal = "";
		if(qName.equalsIgnoreCase("dirname")) {
			;
		}else if (qName.equalsIgnoreCase("film")) {
			tempMovie = new movies();
		}else if (qName.equalsIgnoreCase("fid")) {
			;
		}else if (qName.equalsIgnoreCase("t")) {
			;
		}else if (qName.equalsIgnoreCase("year")) {
			;
		}else if (qName.equalsIgnoreCase("cat")) {
			;
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("dirname")) {
			tempDirector = tempVal;
		}else if (qName.equalsIgnoreCase("film")) {
			movielist.add(tempMovie);
		}else if (qName.equalsIgnoreCase("fid")) {
			tempMovie.setId(tempVal);
		}else if (qName.equalsIgnoreCase("t")) {
			tempMovie.setTitle(tempVal);
			tempMovie.setDirector(tempDirector);
		}else if (qName.equalsIgnoreCase("year")) {
			tempMovie.setYear(checkYear(tempVal));
		}else if (qName.equalsIgnoreCase("cat")) {
			if (tempVal != null)
				tempMovie.setGenres(tempVal);
		}
	}
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		MainSAXParser spe = new MainSAXParser();
		spe.runExample();
		Connection conn = null;
		movieid = new Vector<String>();
		for(int i=0; i<movielist.size();++i) {
			if (movieid.contains(movielist.get(i).getId()))
				movielist.get(i).setDup(true);
			else
				movieid.add(movielist.get(i).getId());
		}

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
        
        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "pat10901");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement addmovie=null;
        PreparedStatement addgenre=null;
        PreparedStatement addlink=null;
        String addmoviequery = "INSERT INTO movies (id, title, year, director) " +
                "SELECT * FROM (SELECT ? AS id, ? AS title, ? AS year, ? AS director) AS tmp " + 
                "WHERE NOT EXISTS (SELECT * FROM movies WHERE id = ?) LIMIT 1; ";
        
        String addgenrequery = "INSERT INTO genres (name) " +
                "SELECT * FROM (SELECT ? AS name) AS tmp " + 
                "WHERE NOT EXISTS (SELECT * FROM genres WHERE name = ?) LIMIT 1; ";
        
        String addlinkquery = "INSERT INTO genres_in_movies (genreId, movieId) " +
                "SELECT * FROM (SELECT (SELECT id from genres where name = ? LIMIT 1) AS genreId, (SELECT id from movies where title = ? LIMIT 1) AS movieId ) AS tmp " + 
                "WHERE NOT EXISTS (  SELECT * FROM genres_in_movies WHERE genreId in (SELECT id from genres where name = ?) AND movieId in (SELECT id from movies where title = ?)) LIMIT 1; ";
        
        try {
            conn.setAutoCommit(false);
           
            addmovie=conn.prepareStatement(addmoviequery);
            addgenre=conn.prepareStatement(addgenrequery);
            addlink=conn.prepareStatement(addlinkquery);

            int size=movielist.size();
            int count=(int) Math.ceil((double)size/2000);
            int j=0;
            while(j<count) {
            		String Error = "";
            	 	for(int i=j*2000 ;i<Math.min( 2000*(j+1),size) ;i++){
            	 		if (movielist.get(i).getDup()) {
            	 			Error += "Error: Duplicate movie ID " + movielist.get(i).toString() + "\n";
            	 			continue;
            	 		}
            	 		if (movielist.get(i).getId() == null || movielist.get(i).getId().isEmpty()) {
            	 			Error += "Error: Movie " + movielist.get(i).getTitle() + " does not have an id\n";
            	 			continue;
            	 		}
	                addmovie.setString(1, movielist.get(i).getId());
	                addmovie.setString(2, movielist.get(i).getTitle());
	                addmovie.setInt(3, movielist.get(i).getYear());
	                addmovie.setString(4, movielist.get(i).getDirector());
	                addmovie.setString(5, movielist.get(i).getId());
	                addmovie.addBatch();
	
	                for(int x=0; x< movielist.get(i).getGenres().size(); x++){
	                   addgenre.setString(1,movielist.get(i).getGenres().get(x));
	                   addgenre.setString(2,movielist.get(i).getGenres().get(x));
	                   addgenre.addBatch();
	                   
	                   addlink.setString(1, movielist.get(i).getGenres().get(x));
	                   addlink.setString(2, movielist.get(i).getTitle());
	                   addlink.setString(3, movielist.get(i).getGenres().get(x));
	                   addlink.setString(4, movielist.get(i).getTitle());
	                   addlink.addBatch();
	                }
	            }
            	 	addmovie.executeBatch();
    	            addgenre.executeBatch();
    	            addlink.executeBatch();
    	            conn.commit();
    	            j++;
    	            System.out.println(Error);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(addmovie!=null) addmovie.close();
            if(addgenre!=null) addgenre.close();
            if(addlink!=null) addlink.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
}