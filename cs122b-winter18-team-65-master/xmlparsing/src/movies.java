import java.util.Vector;

public class movies {
	
	private String id;
	private String title;
	private int year;
	private String director;
	private Vector<String> genres;
	private Boolean dup;
	
	public movies(){
		this.genres = new Vector<String>();
		this.dup = false;
	}
	
	public movies(String id, String title, int year, String director, Vector<String> genres) {
		this.id = id;
		this.title = title;
		this.year  = year;
		this.director = director;
		this.genres = genres;
		this.dup = false;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getGenresString() {
		return String.join(",", genres);
	}
	
	public Vector<String> getGenres() {
		return this.genres;
	}
	
	public void setGenres(String genres) {
		this.genres.add(genres);
	}
	
	public Boolean getDup() {
		return this.dup;
	}
	
	public void setDup(Boolean dup) {
		this.dup = dup;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("Id:" + getId());
		sb.append(", ");
		sb.append("Title:" + getTitle());
		sb.append(", ");
		sb.append("Year:" + getYear());
		sb.append(", ");
		sb.append("Director:" + getDirector());
		sb.append(".");
		sb.append("Genres:" + getGenresString());
		sb.append(".");
		return sb.toString();
	}
}
