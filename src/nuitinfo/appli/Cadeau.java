package nuitinfo.appli;

import java.util.HashMap;

public class Cadeau
{
	private int id;
	private String nom;
	private String description;
	
	public Cadeau(int id, String nom, String des)
	{
		this.id = id;
		this.nom = nom;
		this.description = des;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public HashMap<String, String> getHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(id));
		map.put("nom", nom);
		map.put("description", description);
		
		return map;
	}
}
