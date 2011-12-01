package nuitinfo.appli;

import java.util.HashMap;

public class Ami
{
	private int id;
	private String nom;
	private String prenom;
	private String mail;
	
	public Ami(int id, String nom, String prenom)
	{
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
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
	
	public String getPrenom()
	{
		return prenom;
	}
	
	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
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
		map.put("nomPrenom", prenom + " " + nom);
		map.put("description", "Tableur");
		map.put("img", String.valueOf(R.drawable.fleur));
		
		return map;
	}
}
