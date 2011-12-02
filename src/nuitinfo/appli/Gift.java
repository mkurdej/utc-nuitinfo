package nuitinfo.appli;

import java.util.HashMap;
import java.util.Map;

public class Gift {
	private int id;
	private String name;
	private String category;

	public Gift(int id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(id));
		map.put("category", category);
		map.put("name", name);

		return map;
	}
}
