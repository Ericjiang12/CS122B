

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class search_timer
 */
@WebFilter("/search_timer")
public class search_timer implements Filter {

    /**
     * Default constructor. 
     */
    public search_timer() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		long startTime = System.nanoTime();
		chain.doFilter(request, response);
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		 try{
		    	String content = "ts:" + Float.toString(elapsedTime) + ";";
		    	File file =new File("project5_log_file.txt");
		    	if(!file.exists()){
		    	   file.createNewFile();
		    	}
		    	FileWriter fw = new FileWriter(file,true);
		    	BufferedWriter bw = new BufferedWriter(fw);
		    	bw.write(content);
		    	bw.close();
	      }catch(IOException ioe){
	         System.out.println("Exception occurred:");
	    	 	ioe.printStackTrace();
	       }
		 return;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
