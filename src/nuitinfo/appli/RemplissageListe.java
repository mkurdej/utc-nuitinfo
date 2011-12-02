package nuitinfo.appli;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RemplissageListe extends Activity
{
	private final String url = "www.floriandubois.comm/nuitinfo/facebook/getList.php";
	
	private ListView maListViewPerso;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listeami);

		// R�cup�ration de la listview cr��e dans le fichier main.xml
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);

		int user_id;
		// Cr�ation de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = getList(2, "");

		// Cr�ation d'un SimpleAdapter qui se chargera de mettre les items pr�sents dans notre list (listItem) dans la
		// vue affichageitem
		
		// Cr�ation d'un SimpleAdapter qui se chargera de mettre les items pr�sents dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.itemami,
				new String[] { "img", "nomPrenom", "description" }, new int[] { R.id.img, R.id.nomPrenom, R.id.description });

		// On attribue � notre listView l'adapter que l'on vient de cr�er
		maListViewPerso.setAdapter(mSchedule);

		// Enfin on met un �couteur d'�v�nement sur notre listView
		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				// on r�cup�re la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);

				// on cr�� une boite de dialogue
				AlertDialog.Builder adb = new AlertDialog.Builder(RemplissageListe.this);
				// on attribue un titre � notre boite de dialogue
				adb.setTitle("S�lection Item");
				// on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
				adb.setMessage("Votre choix : " + map.get("titre"));
				// on indique que l'on veut le bouton ok � notre boite de dialogue
				adb.setPositiveButton("Ok", null);
				// on affiche la boite de dialogue
				adb.show();

				Intent i = new Intent(v.getContext(), ChoixType.class);
				i.putExtra("idAmi", map.get("id"));
				startActivityForResult(i, 0);

			}
		});
	}
	
	public ArrayList<HashMap<String, String>> getList(int userid, String access_token) {
		
		// On se connecte au serveur afin de communiquer avec le PHP
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try
		{
			// On �tablit un lien avec le script PHP
			HttpGet get = new HttpGet(url+"?user_id="+userid+"&access_token="+access_token);
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// par le script PHP en get
			// On r�cup�re le r�sultat du script
			response = client.execute(get);
			entity = response.getEntity();
			InputStream is = entity.getContent();
			
			InputStreamReader reader = new InputStreamReader(is);
			char[] cs = null;
			reader.read(cs);
			String str = new String(cs);
			
			is.close();
			if (entity != null) entity.consumeContent();
			
			JSONObject object = new JSONObject(str);
			JSONArray friendList = object.getJSONArray("data");
			
			ArrayList<HashMap<String, String>> returnedList = new ArrayList<HashMap<String, String>>();
			
			for(int i=0; i<friendList.length(); ++i) {
				JSONObject friend = friendList.getJSONObject(i);
				Ami ami = new Ami(friend.getInt("id"), friend.getString("name"));
				returnedList.add(ami.getHashMap());
			}
			return returnedList;
		}
		catch (Exception e)
		{
			return null;
		}
		
	}
}
