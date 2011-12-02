package nuitinfo.appli;

import java.util.HashMap;

public class Cadeau
{
	private int id;
	private String name;
	private String category;
	
	public Cadeau(int id, String nom, String category)
	{
		this.id = id;
		this.name = nom;
		this.category = category;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String nom)
	{
		this.name = nom;
	}
	
	public HashMap<String, String> getHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(id));
		map.put("category", category);
		map.put("name", name);
		
		return map;
	}
}
