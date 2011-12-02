package nuitinfo.appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class login extends Activity implements View.OnTouchListener, View.OnClickListener
{
	public static final String strURL = "www.floriandubois.com";
	public static final int RESULT_Main = 1;
	public ProgressDialog progressDialog;
	private EditText UserEditText;
	private EditText PassEditText;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// initialisation d'une progress bar
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		// Récupération des éléments de la vue définis dans le xml
		UserEditText = (EditText) findViewById(R.id.username);
		PassEditText = (EditText) findViewById(R.id.password);
		
		Button button = (Button) findViewById(R.id.okbutton);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.cancelbutton);
		button.setOnClickListener(this);
    }
	
    public boolean onTouch(View v, MotionEvent event) {
    	Button tmp = (Button)v;
	    tmp.setTextSize(Math.abs(event.getX() - v.getWidth()/2) + Math.abs(event.getY() - v.getHeight()/2));
	    return true;
    }
    
    public void onClick(View v) {
        switch(v.getId())
        {
            //Si l'identifiant de la vue est celui du bouton OK
            case R.id.okbutton:
            		doFacebook();
            	break;
    		
            //Si l'identifiant de la vue est celui du bouton annuler		
            case R.id.cancelbutton:
            		/* agir pour bouton 2 */
            		quit(false, null);
            	break;
    			
    	/* etc. */
        }
    }
    
    public void createDialog(String title, String text)
	{
		// Création d'une popup affichant un message
		AlertDialog ad = new AlertDialog.Builder(this)
				.setPositiveButton("Ok", null).setTitle(title).setMessage(text)
				.create();
		ad.show();

	}
    
    public void quit(boolean success, Intent i)
	{
		// On envoie un résultat qui va permettre de quitter l'appli
		setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
		finish();
	}
    
    public void doFacebook()
    {
    	Toast.makeText(this, "Facebook Loading...\n Please make sure you are connected to internet.", Toast.LENGTH_SHORT).show();
		String url="https://www.facebook.com/dialog/oauth?client_id=274832242563339&redirect_uri=http%3A%2F%2Fwww.floriandubois.com%2Fnuitinfo%2Ffacebook%2Ftest-facebook-sdk.php&state=8eaccfdedff6c9a828addeaf75a086a4&scope=user_likes%2Cfriends_birthday%2Cfriends_likes";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
    }
	
    private void read(InputStream in) throws IOException, JSONException
	{
    	boolean	error_occured	= false;
    	int user_id;
    	
		// On traduit le résultat d'un flux
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        
        String line = null;
        while((line = reader.readLine()) != null)
        {
            str.append(line + "\n");
        }
        JSONObject js = new JSONObject(str.toString());
        user_id = js.getInt("id");
		if (user_id < 0)
		{
			progressDialog.dismiss();
			switch (user_id)
			{
				case -1:
					createDialog("Error", "Invalid username and/or password");
					break;
				case -2:
					createDialog("Error", "Error Sending paramters");
					break;
			}
			error_occured = true;
		}
		else
		{
			progressDialog.dismiss();
			Intent i = new Intent();
			i.putExtra("userid", user_id);
			quit(true,i); 
		}
	}
    
	private String md5(String in)
	{
		MessageDigest digest;
		try
		{
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++)
			{
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}