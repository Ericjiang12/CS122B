

public class Sim {

	private String movieid;

	private String name;
	
	
	public Sim(){
		
	}
	
	public Sim(String movieid, String name) {
		this.movieid=movieid;
		this.name = name;
		
		
	}
	public String getMovieid() {
		return movieid;
	}

	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Sim Details - ");
		sb.append("Name:" + getName());
		sb.append(", ");
		sb.append("MovieID:" + getMovieid());
		sb.append(".");
		
		return sb.toString();
	}
}
