package nuitinfo.appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.InputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class nuitinfo2011Activity extends Activity implements View.OnClickListener
{
	private TextView tv;
	public static final int RESULT_Main = 1;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		//Intent i = new Intent(nuitinfoActivity.this, login.class);
		//startActivityForResult(i, RESULT_Main);
		
		Button buttonC = (Button) findViewById(R.id.buttonConn);
		buttonC.setOnClickListener(this);
		Button buttonL = (Button) findViewById(R.id.buttonLog);
		buttonL.setOnClickListener(this);
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
		
	}

}