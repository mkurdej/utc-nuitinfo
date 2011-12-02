package nuitinfo.appli;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListeCadeau extends Activity
{
	private ListView maListViewCadeau;
	private long friendId = -1;
	private String typeCadeau;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listecadeau);

		Bundle b = getIntent().getExtras();
		int index = b.getInt("typeSelection");
		friendId = b.getLong("idAmi");
		
		TextView lblTypeCadeau = (TextView) findViewById(R.id.lblTypeCadeau);
		switch(index)
		{
			case R.id.btnMovie:
				lblTypeCadeau.setText("Préférences de films");
				typeCadeau = "movies";
				break;
			case R.id.btnMusic:
				lblTypeCadeau.setText("Préférences de musiques");
				typeCadeau = "music";
				break;
			case R.id.btnBook:
				lblTypeCadeau.setText("Préférences de livres");
				typeCadeau = "books";
				break;
		}

		// Récupération de la listview créée dans le fichier main.xml
		maListViewCadeau = (ListView) findViewById(R.id.listviewcadeau);

		// Création de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = getRecommandations();

		// Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la
		// vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.itemcadeau,
				new String[] { "name" }, new int[] { R.id.name });

		// On attribue à notre listView l'adapter que l'on vient de créer
		maListViewCadeau.setAdapter(mSchedule);

		// Enfin on met un écouteur d'évènement sur notre listView
		maListViewCadeau.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{

			}
		});
	}

	public ArrayList<HashMap<String, String>> getRecommandations()
	{
		ArrayList<HashMap<String, String>> returnedList = new ArrayList<HashMap<String, String>>();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try
		{
			// On établit un lien avec le script PHP
			URI uri = URIUtils.createURI("http", "www.floriandubois.com", -1, "nuitinfo/facebook/getRecommandationsWithCategory.php", "friend_id=" + friendId + "&category=" + typeCadeau, null);
			HttpGet get = new HttpGet(uri);
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// par le script PHP en get
			// On récupère le résultat du script
			response = client.execute(get);
			entity = response.getEntity();
			InputStream is = entity.getContent();

			String str = StreamConverter.convertStreamToString(is);

			is.close();

			if (entity != null)
				entity.consumeContent();

			JSONArray friendList = new JSONArray(str);

			for (int i = 0; i < friendList.length(); ++i)
			{
				JSONObject friend = friendList.getJSONObject(i);
				Cadeau cadeau = new Cadeau(friend.getInt("id"), friend.getString("name"), friend.getString("category"));
				returnedList.add(cadeau.getHashMap());
			}
		}
		catch (Exception e)
		{
			System.out.println("nuitinfo.appli.ListeCadeau.getRecommandations - Error : " + e);
		}

		return returnedList;
	}
}
