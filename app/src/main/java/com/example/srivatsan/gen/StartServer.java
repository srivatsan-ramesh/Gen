package com.example.srivatsan.gen;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.srivatsan.gen.util.HTTPD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class StartServer extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_server);

        String str = "<img src=\"../Screenshot_2015-04-26-16-24-06.png\"></img>";
        appendContent(str.getBytes());

        try {
            HTTPD fileRendering = new HTTPD(getApplicationContext(), 8080);
            Log.i("server", "started");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void appendContent(byte[] text){

        File f1 = new File(Environment.getExternalStorageDirectory().getPath(),"Gen");
        f1.mkdirs();
        Log.i("f1",String.valueOf(f1));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_server, menu);
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
