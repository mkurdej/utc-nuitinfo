package nuitinfo.appli;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class nuitinfoActivity extends Activity implements OnClickListener
{
	private TextView tv;
	public static final int RESULT_Main = 1;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		Button buttonC = (Button) findViewById(R.id.buttonConn);
		buttonC.setOnClickListener(this);
		Button buttonL = (Button) findViewById(R.id.buttonLog);
		buttonL.setOnClickListener(this);
		
    }
	
    private void startup(Intent i) 
	{
		// R�cup�re l'identifiant        
		int user = i.getIntExtra("userid",-1);
		
		// Affiche les identifiants de l'utilisateur
        //tv.setText("UserID: "+String.valueOf(user)+" logged in");
    }
 
 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{ 
        if(requestCode == RESULT_Main && resultCode == RESULT_CANCELED)  
            finish(); 
        else 
            startup(data);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId())
        {
            //Si l'identifiant de la vue est celui du bouton OK
            case R.id.buttonConn:
            		doFacebook();
            	break;
    		
            //Si l'identifiant de la vue est celui du bouton annuler		
            case R.id.buttonLog:
            		doConnection();
            	break;
    			
    	/* etc. */
        }
		
	} 
	
	public void doFacebook()
	{
		Toast.makeText(this, "Facebook Loading...\n Please make sure you are connected to internet.", Toast.LENGTH_SHORT).show();
		String url="http://www.floriandubois.com/nuitinfo/facebook/login.php";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivityForResult(i, RESULT_Main);
	}
	
	public void doConnection()
	{
		Intent i = new Intent(nuitinfoActivity.this, login.class);
		startActivityForResult(i, RESULT_Main);
	}
}