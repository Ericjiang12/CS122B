
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import java.io.IOException;
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

public class StarParser extends DefaultHandler{

	//List<String> myEmpls;
	
	private String tempVal;
	
	String temp_fn;
	String temp_ln;
	String temp_n;
	//to maintain context
	private Star tempEmp;
	static Vector<Star> myEmpls;
	int id=0;
	public StarParser(){
	myEmpls = new Vector<Star>();
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
			sp.parse("actors63.xml", this);
			
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
		
		Iterator<Star> it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("actor")) {
			//create a new instance of employee
			tempEmp = new Star();
			id++;
			String star_id="aa"+id;
			tempEmp.setid(star_id);
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(qName.equalsIgnoreCase("actor")) {
			//add it to the list
			myEmpls.add(tempEmp);
			
		}
		else if (qName.equalsIgnoreCase("stagename")) {
			if (!(tempVal.equals(null)|| tempVal.isEmpty()))
			{
				temp_n=tempVal;
				tempEmp.setName(temp_n);
				temp_n="";
			}
			
		}
		
			else if (qName.equalsIgnoreCase("dob")) {
			if (checkYear(tempVal)!=0)
			{
				tempEmp.setYear(Integer.parseInt(tempVal));}
			
			}
		
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		StarParser spe = new StarParser();
		spe.runExample();
		Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "pat10901");
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
        PreparedStatement addstar=null;

        String addstarquery="INSERT INTO stars (id, name, birthYear) " +
                "SELECT * FROM (SELECT ? AS id, ? AS name, ? AS yaer) as sq " + 
                "WHERE NOT EXISTS (  SELECT * FROM stars WHERE id = ? ) LIMIT 1; ";
        

        try {
            conn.setAutoCommit(false);
           
            addstar=conn.prepareStatement(addstarquery);
            
            int size=myEmpls.size();
            int count=(int) Math.ceil((double)size/2000);
            int j=0;
            while(j<count) {
            for(int i=j*2000 ;i<Math.min( 2000*(j+1),size) ;i++){ // implement the batch insert
            	addstar.setString(1, myEmpls.get(i).getid());
                addstar.setString(2, myEmpls.get(i).getName());
                addstar.setInt(3, myEmpls.get(i).getYear());
                addstar.setString(4, myEmpls.get(i).getid());
                addstar.addBatch();
            }
            
            addstar.executeBatch();
            conn.commit();
            j++;
            }
            
            addstar.close();
            conn.close();
            
           
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(addstar!=null) addstar.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	
}





