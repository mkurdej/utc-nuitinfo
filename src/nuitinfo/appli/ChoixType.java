package nuitinfo.appli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoixType extends Activity implements View.OnClickListener
{
	private long idAmi = -1;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.typecadeau);
		
		Bundle b = getIntent().getExtras();
		idAmi = b.getLong("idAmi");
		
		Button btnMovie = (Button) findViewById(R.id.btnMovie);
		Button btnMusic = (Button) findViewById(R.id.btnMusic);
		Button btnBook = (Button) findViewById(R.id.btnBook);
		btnMovie.setOnClickListener(this);
		btnBook.setOnClickListener(this);
		btnMusic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Intent i = new Intent(v.getContext(), ListeCadeau.class);
		i.putExtra("typeSelection", v.getId());
		i.putExtra("idAmi", idAmi);
		startActivityForResult(i, 0);
	}
}
