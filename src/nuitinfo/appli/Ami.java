package nuitinfo.appli;

import java.util.HashMap;

public class Ami
{
	private int id;
	private String nom;
	private String mail;
	
	public Ami(int id, String nom)
	{
		this.id = id;
		this.nom = nom;
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
	
	public String getMail()
	{
		return mail;
	}
	
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	
	public HashMap<String, String> getHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(id));
		map.put("nom", nom);
		map.put("description", "Tableur");
		map.put("img", String.valueOf(R.drawable.ic_launcher));
		
		return map;
	}
}
