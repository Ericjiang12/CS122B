import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SimParser extends DefaultHandler{

	//List<String> myEmpls;
	
	private String tempVal;
	
	String temp_fn;
	String temp_ln;
	String temp_n;
	//to maintain context
	private Sim tempEmp;
	static  Vector<Sim> myEmpls;
	
	public SimParser(){
	myEmpls = new Vector<Sim>();
	}
	
	public void runExample() {
		parseDocument();
		printData();
	}
	private int checkYear(String year) {
		if (Pattern.matches("^(17|18|19|20|21)\\d{2}$", year))
			return Integer.parseInt(year);
		return 0;
	}
	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse("casts124.xml", this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	/**
	 * Iterate through the list and print
	 * the contents
	 */
	private void printData(){
		
		System.out.println("No of Employees '" + myEmpls.size() + "'.");
		
		Iterator<Sim> it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("m")) {
			//create a new instance of employee
			tempEmp = new Sim();

		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(qName.equalsIgnoreCase("m")) {
			//add it to the list
			myEmpls.add(tempEmp);
			
		}
		else if (qName.equalsIgnoreCase("f")) {
			if (!(tempVal.equals(null)|| tempVal.isEmpty()))
			{
				//temp_n=tempVal;
				tempEmp.setMovieid(tempVal);
				//temp_n="";
			}
			
		}
		
			else if (qName.equalsIgnoreCase("a")) {
			if (!(tempVal.equals(null)|| tempVal.isEmpty() || tempVal.equals("sa")|| tempVal.equals("s a")))
			{
				tempEmp.setName(tempVal);
				}
			else
			{
				tempEmp.setName("Wally Cox");
				
				
			}
			}
			
		
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		SimParser spe = new SimParser();
		spe.runExample();
		Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "pat10901");
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
        PreparedStatement addsim=null;
        PreparedStatement addstar=null;

        String addstarquery =  "INSERT INTO stars (id,name) " +
                "SELECT * FROM (SELECT ? AS id, ? AS name) AS tmp " + 
                "WHERE NOT EXISTS (  SELECT id FROM stars where name= ?) LIMIT 1; ";
        
        String addsimquery= "INSERT INTO stars_in_movies (starId, movieId) values ( (SELECT id from stars where name = ? LIMIT 1), ? );";
        

        try {
            conn.setAutoCommit(false);
           
            addsim=conn.prepareStatement(addsimquery);
            addstar=conn.prepareStatement(addstarquery);
            
            int size=myEmpls.size();
            int count=(int) Math.ceil((double)size/2000);
            int j=0;
            int x=1;
            for (int i=0;i<size;i++)
            {
            	String star_id="";
                star_id="yy"+x;
            	x++;
                addstar.setString(1,star_id);
                addstar.setString(2,myEmpls.get(i).getName());
                addstar.setString(3,myEmpls.get(i).getName());
                addstar.addBatch();
                //System.out.println(addstar);
            }
            
            addstar.executeBatch();
            conn.commit();
            
            while(j<count) {
            for(int i=j*2000 ;i<Math.min( 2000*(j+1),size) ;i++){ // implement the batch insert
            	
            	addsim.setString(1, myEmpls.get(i).getName());
                addsim.setString(2, myEmpls.get(i).getMovieid());

                addsim.addBatch();
            }
            
         
            addsim.executeBatch();
            conn.commit();
            j++;
            }
            
            
            //conn.close();
            
           
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(addsim!=null) addsim.close();
            if(addstar!=null) addstar.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
	
	}
	
}




