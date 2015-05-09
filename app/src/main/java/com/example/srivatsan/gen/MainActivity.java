package com.example.srivatsan.gen;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private EditText pasteField;
    WebView webView = (WebView) findViewById(R.id.webview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        pasteField = (EditText)findViewById(R.id.paste);

        final String strURL = "192.168.25.60:8080//Gen/index.html";

        Button btnPaste = (Button) findViewById(R.id.button2);
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = pasteField.getText().toString();
                appendContent(text.getBytes());
                webView.loadUrl(strURL);
            }
        });

        webView.loadUrl(strURL);
    }

    private void appendContent(byte[] text){

        File f1 = new File(Environment.getExternalStorageDirectory().getPath(),"Gen");
        f1.mkdirs();
        Log.i("f1", String.valueOf(f1));
        File f = new File(f1.getPath()+File.separator+"index.html");
        try {
            FileOutputStream fout = new FileOutputStream(f,true);
            fout.write(text);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Empty File", "Not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initClipboardData() {
        paste();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        initClipboardData();
    }
   /* public void copy(View view){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String text = copyField.getText().toString();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "Text Copied",
                Toast.LENGTH_SHORT).show();
    }*/
    public void paste(){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData abc = myClipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String text = item.getText().toString();
        pasteField.setText(text);
        Toast.makeText(getApplicationContext(), "Text Pasted",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
