package nuitinfo.appli;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import nuitinfo.bdd.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View;

public class nuitinfoActivity extends Activity
{
	private TextView tv;
	public static final int RESULT_Main = 1;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		Intent i = new Intent(nuitinfoActivity.this, login.class);
		startActivityForResult(i, RESULT_Main);
		 
        tv = new TextView(this);
        setContentView(tv);
    }
	
    private void startup(Intent i) 
	{
		// Récupère l'identifiant        
		int user = i.getIntExtra("userid",-1);
		 
		//Affiche les identifiants de l'utilisateur
        tv.setText("UserID: "+String.valueOf(user)+" logged in");
    }
 
 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{ 
        if(requestCode == RESULT_Main && resultCode == RESULT_CANCELED)  
            finish(); 
        else 
            startup(data);
    }
}