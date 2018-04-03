

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ShoppingCart
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String quantity = request.getParameter("quantity");
		String title = request.getParameter("title");

		User user = (User) httpRequest.getSession().getAttribute("user");
		Vector<CartItem> cart = user.getCart();
		PrintWriter out = response.getWriter();
		//show all the movies in the shopping cart and sent to the front end
		if (title == null) {
			response.setContentType("application/json");
			JsonArray jsonArray = new JsonArray();
			
			for(int i=0; i < cart.size(); ++ i) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", cart.get(i).getTitle());
                jsonObject.addProperty("quantity", cart.get(i).getQuantity());
                jsonArray.add(jsonObject);
            }
            out.write(jsonArray.toString());
		}
		//handle update, set the CartItem
		else if (title != null && quantity != null) {
			int q = Integer.parseInt(quantity); 
			for(int i=0; i < cart.size(); ++ i) {
				if (cart.get(i).getTitle().equals(title)) {
					cart.get(i).setQuantity(q);
					break;
					}
			}
		}
		//used to deliver quantity to the front end, search for the title in shopping cart
		else if(title != null && quantity == null) {
			response.setContentType("application/json");
			JsonArray jsonArray = new JsonArray();
			JsonObject jsonObject = new JsonObject();
			boolean flag = true;
			for(int i=0; i < cart.size(); ++ i) {
				if (cart.get(i).getTitle().equals(title)) {
	                int new_quantity = cart.get(i).getQuantity() + 1;
	                cart.get(i).setQuantity(new_quantity);
	                jsonObject.addProperty("title", cart.get(i).getTitle());
	                jsonObject.addProperty("quantity", cart.get(i).getQuantity());
	                jsonArray.add(jsonObject);
	                flag = false;
	                break;
				}
			}
			if (flag) {
				cart.add(new CartItem(title,1));
				jsonObject.addProperty("title", title);
                jsonObject.addProperty("quantity", 1);
                jsonArray.add(jsonObject);
			}
			out.write(jsonArray.toString());
		}
		request.getSession().setAttribute("user", user);
		out.close();
		for(int i=0; i < cart.size(); ++ i) {
			if (cart.get(i).getQuantity() == 0) 
				cart.remove(i--);
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
