

public class Star {
	private String id;
	private String name;

	private int year;
	
	
	public Star(){
		
	}
	
	public Star(String name, String id, int year) {
		this.id=id;
		this.name = name;
		this.year = year;
		
		
	}
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Star Details - ");
		sb.append("id:" + getid());
		sb.append(", ");
		sb.append("Name:" + getName());
		sb.append(", ");
		sb.append("Year:" + getYear());
		sb.append(".");
		
		return sb.toString();
	}
}
