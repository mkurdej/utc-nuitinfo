package nuitinfo.appli;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
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

public class GiftListActivity extends Activity {
	private ListView giftListView;
	private long friendId = -1;
	private String giftType;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listecadeau);

		Bundle b = getIntent().getExtras();
		int index = b.getInt("typeSelection");
		friendId = b.getLong("idAmi");

		TextView lblTypeCadeau = (TextView) findViewById(R.id.lblTypeCadeau);
		switch (index) {
		case R.id.btnMovie:
			lblTypeCadeau.setText("Préférences de films");
			giftType = "movies";
			break;
		case R.id.btnMusic:
			lblTypeCadeau.setText("Préférences de musiques");
			giftType = "music";
			break;
		case R.id.btnBook:
			lblTypeCadeau.setText("Préférences de livres");
			giftType = "books";
			break;
		}

		// Récupération de la listview créée dans le fichier main.xml
		giftListView = (ListView) findViewById(R.id.listviewcadeau);

		// Création de la ArrayList qui nous permettra de remplir la listView
		ArrayList<Map<String, String>> listItem = getRecommandations();

		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présents dans notre list (listItem) dans la
		// vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
				listItem, R.layout.itemcadeau, new String[] { "name" },
				new int[] { R.id.name });

		// On attribue à notre listView l'adapter que l'on vient de créer
		giftListView.setAdapter(mSchedule);

		// Enfin on met un écouteur d'évènement sur notre listView
		giftListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

			}
		});
	}

	public ArrayList<Map<String, String>> getRecommandations() {
		ArrayList<Map<String, String>> returnedList = new ArrayList<Map<String, String>>();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try {
			// On établit un lien avec le script PHP
			URI uri = URIUtils.createURI("http", "www.floriandubois.com", -1,
					"nuitinfo/facebook/getRecommandationsWithCategory.php",
					"friend_id=" + friendId + "&category=" + giftType, null);
			// HttpGet get = new HttpGet(uri);
			HttpPost post = new HttpPost(uri);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// get.setHeader("Content-Type",
			// "application/x-www-form-urlencoded");
			// par le script PHP en get
			// On récupère le résultat du script
			// response = client.execute(get);
			response = client.execute(post);
			entity = response.getEntity();
			InputStream is = entity.getContent();

			String str = StreamConverter.convertStreamToString(is);

			is.close();

			if (entity != null)
				entity.consumeContent();

			JSONArray friendList = new JSONArray(str);

			for (int i = 0; i < friendList.length(); ++i) {
				JSONObject friend = friendList.getJSONObject(i);
				Gift cadeau = new Gift(friend.getInt("id"),
						friend.getString("name"), friend.getString("category"));
				returnedList.add(cadeau.getMap());
			}
		} catch (Exception e) {
			System.out
					.println("nuitinfo.appli.ListeCadeau.getRecommandations - Error : "
							+ e);
		}

		return returnedList;
	}
}
