package com.example.srivatsan.gen;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class AppendDetailsActivity extends ActionBarActivity {

    Button buttonAddContent;
    EditText editTextAddContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_append_details);

        buttonAddContent = (Button) findViewById(R.id.buttonAddContent);
        editTextAddContent = (EditText) findViewById(R.id.editTextAddContent);

        buttonAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendContent(editTextAddContent.getText().toString().getBytes(Charset.forName("UTF-8")));
            }
        });
    }

    private void appendContent(byte[] text){

        File f1 = new File(Environment.getExternalStorageDirectory().getPath(),"Gen");
        f1.mkdirs();
        Log.i("f1",String.valueOf(f1));
        File f = new File(f1.getPath()+File.separator+"text.txt");
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
        getMenuInflater().inflate(R.menu.menu_append_details, menu);
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
