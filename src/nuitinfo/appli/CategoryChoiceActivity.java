package nuitinfo.appli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryChoiceActivity extends Activity implements View.OnClickListener {
	private long friend_id = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.typecadeau);

		Bundle b = getIntent().getExtras();
		friend_id = b.getLong("idAmi");

		Button btnMovie = (Button) findViewById(R.id.btnMovie);
		Button btnMusic = (Button) findViewById(R.id.btnMusic);
		Button btnBook = (Button) findViewById(R.id.btnBook);
		btnMovie.setOnClickListener(this);
		btnBook.setOnClickListener(this);
		btnMusic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(v.getContext(), GiftListActivity.class);
		i.putExtra("typeSelection", v.getId());
		i.putExtra("idAmi", friend_id);
		startActivityForResult(i, 0);
	}
}
