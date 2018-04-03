/*
 * This CartItem represents a single item in the shopping cart and has title and quantity as attributes.
 */
public class CartItem {

	private final String title;
	private int quantity;

	public CartItem(String title, int quantity){
		this.title   = title;
		this.quantity = quantity;
	}

	public String getTitle()   { return this.title; }
	public int getQuantity() { return this.quantity; }
	public void setQuantity(int q) { this.quantity = q; }
}
