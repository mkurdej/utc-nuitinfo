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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Pour être plus précis, c'est le remplissage de la liste d'ami.
 * @author M4veR1K & Lowery
 *
 */
public class RemplissageListe extends Activity implements Runnable
{
	private ListView maListViewPerso;
	private ProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> listItem;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg)
		{
			progressDialog.dismiss();
			
			SimpleAdapter mSchedule = new SimpleAdapter(RemplissageListe.this.getBaseContext(), listItem, R.layout.itemami,
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

					Intent i = new Intent(v.getContext(), ChoixType.class);
					i.putExtra("idAmi", Long.parseLong(map.get("id")));
					startActivityForResult(i, 0);
				}
			});
		}
		
		
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listeami);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);

		// Rï¿½cupï¿½ration de la listview crï¿½ï¿½e dans le fichier main.xml
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);

		// Crï¿½ation de la ArrayList qui nous permettra de remplir la listView
		progressDialog.show();
		launchWait();	
	}
	
	public void launchWait()
	{
		progressDialog.show();
		Thread t = new Thread(this);
		t.start();
	}

	public ArrayList<HashMap<String, String>> getListAmis(int userid, String access_token)
	{
		ArrayList<HashMap<String, String>> returnedList = null;
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

			returnedList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < friendList.length(); ++i)
			{
				JSONObject friend = friendList.getJSONObject(i);
				Ami ami = new Ami(friend.getInt("id"), friend.getString("name"));
				returnedList.add(ami.getHashMap());
			}
		}
		catch (Exception e) {
			Log.v("Debug", "RemplissageList.getListAmi - Exception : " + e);
		}

		return returnedList;
	}


	public void run()
	{
		listItem = getListAmis(2, "");
		handler.sendEmptyMessage(0);
	}
	
}
