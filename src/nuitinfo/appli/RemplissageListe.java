package nuitinfo.appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RemplissageListe extends Activity
{
	private ListView maListViewPerso;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listeami);

		// Rï¿½cupï¿½ration de la listview crï¿½ï¿½e dans le fichier main.xml
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);

		// Crï¿½ation de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = getList(2, "");

		// Crï¿½ation d'un SimpleAdapter qui se chargera de mettre les items prï¿½sents dans notre list (listItem) dans
		// la
		// vue affichageitem

		// Crï¿½ation d'un SimpleAdapter qui se chargera de mettre les items prï¿½sents dans notre list (listItem) dans
		// la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.itemami,
				new String[] { "img", "nom"}, new int[] { R.id.img, R.id.nomPrenom});

		// On attribue ï¿½ notre listView l'adapter que l'on vient de crï¿½er
		maListViewPerso.setAdapter(mSchedule);

		// Enfin on met un ï¿½couteur d'ï¿½vï¿½nement sur notre listView
		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				// on rï¿½cupï¿½re la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);

				// on crï¿½ï¿½ une boite de dialogue
				AlertDialog.Builder adb = new AlertDialog.Builder(RemplissageListe.this);
				// on attribue un titre ï¿½ notre boite de dialogue
				adb.setTitle("Sélection Item");
				// on insï¿½re un message ï¿½ notre boite de dialogue, et ici on affiche le titre de l'item cliquï¿½
				adb.setMessage("Votre choix : " + map.get("titre"));
				// on indique que l'on veut le bouton ok ï¿½ notre boite de dialogue
				adb.setPositiveButton("Ok", null);
				// on affiche la boite de dialogue
				adb.show();

				Intent i = new Intent(v.getContext(), ChoixType.class);
				i.putExtra("idAmi", map.get("id"));
				startActivityForResult(i, 0);
			}
		});
	}

	public ArrayList<HashMap<String, String>> getList(int userid, String access_token)
	{

		// On se connecte au serveur afin de communiquer avec le PHP
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try
		{
			// On établit un lien avec le script PHP
			URI uri = URIUtils.createURI("http", "www.floriandubois.com", -1, "nuitinfo/facebook/getFriendList.php", "user_id=" + userid + "&access_token=" + access_token, null);
			HttpGet get = new HttpGet(uri);
			Log.v("onsenfou", uri.toString());
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// par le script PHP en get
			// On récupère le résultat du script
			response = client.execute(get);
			entity = response.getEntity();
			InputStream is = entity.getContent();
			Log.v("onsenfou", "TEST");

			String str = convertStreamToString(is);

			Log.v("onsenfou", str);
			is.close();

			if (entity != null)
				entity.consumeContent();

			Log.v("onsenfou", str.substring(0, 10) + " - " + str.substring(str.length() - 10, str.length()));
			JSONArray friendList = new JSONArray(str);
			Log.v("onsenfou", "TEST");

			ArrayList<HashMap<String, String>> returnedList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < friendList.length(); ++i)
			{
				JSONObject friend = friendList.getJSONObject(i);
				Ami ami = new Ami(friend.getInt("id"), friend.getString("name"));
				returnedList.add(ami.getHashMap());
			}
			Log.v("onsenfou", "FIN");
			return returnedList;
		}
		catch (Exception e)
		{
			return null;
		}

	}

	public String convertStreamToString(InputStream is) throws IOException
	{
		if (is != null)
		{
			Writer writer = new StringWriter();
			char[] buffer = new char[4096];

			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				is.close();
			}
			return writer.toString();
		}
		else
		{
			return "";
		}
	}
}
