package nuitinfo.appli;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
//import java.util.jar.Attributes;
import org.xml.sax.Attributes;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class login extends Activity implements View.OnTouchListener, View.OnClickListener
{
	
	public static final int RESULT_Main = 1;
	public ProgressDialog progressDialog;
	private EditText UserEditText;
	private EditText PassEditText;
	private int user_id;
	private String access_token;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// initialisation d'une progress bar
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		// R�cup�ration des �l�ments de la vue d�finis dans le xml
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
            		// agir pour bouton 1
            		int usersize = UserEditText.getText().length();
    				int passsize = PassEditText.getText().length();
    				// si les deux champs sont remplis
    				if (usersize > 0 && passsize > 0)
    				{
    					progressDialog.show();
    					String user = UserEditText.getText().toString();
    					String pass = PassEditText.getText().toString();
    					// On appelle la fonction doLogin qui va communiquer avec le PHP
    					try {
							doLogin(user, pass);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	            	goListFriends(v);
    			    	//createDialog("Error2", "Please enter Username and Password");
    					//Intent myIntent = new Intent(v.getContext(), profile.class);
    	                //startActivityForResult(myIntent, 0);
    				}
    				else createDialog("Error", "Please enter Username and Password");
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
		// Cr�ation d'une popup affichant un message
		AlertDialog ad = new AlertDialog.Builder(this)
				.setPositiveButton("Ok", null).setTitle(title).setMessage(text)
				.create();
		ad.show();

	}
    
    public void quit(boolean success, Intent i)
	{
		// On envoie un r�sultat qui va permettre de quitter l'appli
		setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
		finish();
	}
    
    public void doLogin(final String login, final String pass) throws URISyntaxException
    {
		final String pw = md5(pass);
		
		URI uri = URIUtils.createURI("http", "www.floriandubois.com", -1, "nuitinfo/facebook/connexion.php", "login=" + login, null);
		HttpGet get = new HttpGet(uri);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

		HttpResponse response;
		HttpEntity entity;

		try
		{
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// On passe les param�tres login et password qui vont �tre r�cup�r�s
			// par le script PHP en post
			// On r�cup�re le r�sultat du script
			response = client.execute(get);
			entity = response.getEntity();
			InputStream is = entity.getContent();
			// On appelle une fonction d�finie plus bas pour traduire la r�ponse
			read(is);
			is.close();
			if (entity != null) entity.consumeContent();
		}
		catch (Exception e)
		{
			progressDialog.dismiss();
			//createDialog("Error", "Couldn't establish a connection");
		}
    }
	
    private void read(InputStream in) throws IOException, JSONException
	{
		// On traduit le r�sultat d'un flux
		String str = StreamConverter.convertStreamToString(in);
		JSONObject object = new JSONObject(str);
		user_id = object.getInt("id");
		access_token = object.getString("access_token");
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
	
	public void goListFriends(View v)
	{
		Intent i = new Intent(v.getContext(), RemplissageListe.class);
		i.putExtra("id", user_id);
		i.putExtra("access", access_token);
		startActivityForResult(i, 0);
	}
}