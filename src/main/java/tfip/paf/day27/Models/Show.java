package tfip.paf.day27.Models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Show {
    
    private int id;
	private String name;
	private String summary;

	public void setId(int id) { this.id = id; }
	public int getId() { return this.id; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setSummary(String summary) { this.summary = summary; }
	public String getSummary() { return this.summary; }
    
    public static Show toShow(String jsonStr) {
        Show show = new Show();
		JsonReader reader = Json.createReader(new StringReader(jsonStr));
		JsonObject o = reader.readObject();
		show.setName(o.getString("name"));
		show.setId(o.getInt("id"));
		return show;
    }
}
