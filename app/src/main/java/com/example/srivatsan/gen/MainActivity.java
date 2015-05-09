package com.example.srivatsan.gen;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;




public class MainActivity extends ActionBarActivity {
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private EditText copyField,pasteField;
    WebView webView = (WebView) findViewById(R.id.webview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        copyField = (EditText)findViewById(R.id.copy);
        pasteField = (EditText)findViewById(R.id.paste);
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
    public void copy(View view){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String text = copyField.getText().toString();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "Text Copied",
                Toast.LENGTH_SHORT).show();
    }
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
