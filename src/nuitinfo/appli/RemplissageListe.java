package nuitinfo.appli;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class RemplissageListe extends ListActivity {
	private int user_id;
	private String access_token;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent i = getIntent();
		//user_id = i.getIntExtra("id", 0);
		//access_token = i.getStringExtra("access");
		user_id = 1526860101;
		access_token = "AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD";
		
		List<HashMap<String, String>> listItem = getListAmis(user_id, access_token);
		SimpleAdapter adapter = new SimpleAdapter(RemplissageListe.this.getBaseContext(), listItem, R.layout.itemami,
				new String[] { "img", "nom"}, new int[] { R.id.img, R.id.nomPrenom});
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
		String item = map.get("nom");
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		Intent i = new Intent(v.getContext(), ChoixType.class);
		i.putExtra("idAmi", Long.parseLong(map.get("id")));
		startActivityForResult(i, 0);
	}
	
	public List<HashMap<String, String>> getListAmis(int userid, String access_token)
	{
		List<HashMap<String, String>> returnedList = null;
		// On se connecte au serveur afin de communiquer avec le PHP
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try
		{
			// On �tablit un lien avec le script PHP
			URI uri = URIUtils.createURI("http", "www.floriandubois.com", -1, "nuitinfo/facebook/getFriendList.php", "user_id=" + userid + "&access_token=" + access_token, null);
			HttpGet get = new HttpGet(uri);
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// par le script PHP en get
			// On r�cup�re le r�sultat du script
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
}
