import java.util.Vector;

/*
 * This User class only has the username field in this example.
 */
public class User {
	private final String username;
	private final Vector<CartItem> cart;
	
	public User(String username) {
		this.username = username;
		this.cart = new Vector<CartItem>();
	}
	
	public String getUsername() {
		return this.username;
	}
	public Vector<CartItem> getCart() {
		return this.cart;
	}
}
