

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
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
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		System.out.println("LoginFilter: " + httpRequest.getRequestURI());
		
		if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}
		
		if (httpRequest.getSession().getAttribute("user") == null) {
			httpResponse.sendRedirect("/project2-login-example/login.html");
			return;
		} else {
			chain.doFilter(request, response);
			return;
		}
	}
	
	private boolean isUrlAllowedWithoutLogin(String requestURI) {
		requestURI = requestURI.toLowerCase();
		
		if (requestURI.endsWith("login.html") || requestURI.endsWith("login.js") 
				|| requestURI.endsWith("login")) {
			return true;
		}
		return false;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
