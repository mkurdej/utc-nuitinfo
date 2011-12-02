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
import android.widget.TextView;

public class ListeCadeau extends Activity
{
	private ListView maListViewCadeau;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listecadeau);
		
		Bundle b = getIntent().getExtras();
		int index = b.getInt("typeSelection");
		TextView lblTypeCadeau = (TextView) findViewById(R.id.lblTypeCadeau);
		switch (index)
		{
			case R.id.btnMovie: lblTypeCadeau.setText("Préférences de films"); break;
			case R.id.btnMusic: lblTypeCadeau.setText("Préférences de musiques"); break;
			case R.id.btnBook: lblTypeCadeau.setText("Préférences de livres"); break;
		}

		// Récupération de la listview créée dans le fichier main.xml
		maListViewCadeau = (ListView) findViewById(R.id.listviewcadeau);

		// Création de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();


		// On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
		for (int i = 0; i < 4; i++)
		{
			Cadeau cadeau = new Cadeau(0, "Star Wars La triolgie", "C'est vachement coule !");
			listItem.add(cadeau.getHashMap());
		}

		// Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.itemcadeau,
				new String[] { "nom", "description" }, new int[] { R.id.nomCadeau, R.id.description });

		// On attribue à notre listView l'adapter que l'on vient de créer
		maListViewCadeau.setAdapter(mSchedule);

		// Enfin on met un écouteur d'évènement sur notre listView
		maListViewCadeau.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				// on récupère la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) maListViewCadeau.getItemAtPosition(position);
				Intent i = new Intent(v.getContext(), ChoixType.class);
				i.putExtra("idCadeau", map.get("id"));
				startActivityForResult(i, 0);
			}
		});
	}
}
