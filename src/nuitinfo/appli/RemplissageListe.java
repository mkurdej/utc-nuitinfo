package nuitinfo.appli;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

		// Récupération de la listview créée dans le fichier main.xml
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);

		// Création de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();


		// On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
		for (int i = 0; i < 4; i++)
		{
			Ami ami = new Ami(0, "Tresse", "Bastien");
			listItem.add(ami.getHashMap());
		}

		// Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.itemami,
				new String[] { "img", "nomPrenom", "description" }, new int[] { R.id.img, R.id.nomPrenom, R.id.description });

		// On attribue à notre listView l'adapter que l'on vient de créer
		maListViewPerso.setAdapter(mSchedule);

		// Enfin on met un écouteur d'évènement sur notre listView
		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				// on récupère la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
				Intent i = new Intent(v.getContext(), ChoixType.class);
				i.putExtra("idAmi", map.get("id"));
				startActivityForResult(i, 0);
			}
		});
	}
}
