package com.example.srivatsan.gen;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srivatsan.gen.util.HTTPD;
import com.example.srivatsan.gen.util.RecyclerViewAdapter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private EditText pasteField;
    WebView webView;
    Intent intent;
    String fileServerURL;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static  SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_element);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(new String[]{"12334","sdfghj"}/*dataset*/,"Text", getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();


       /* if(type!=null && action.equals(Intent.ACTION_SEND)){
            if(type.startsWith("application/pdf")){
                handlePDF(intent);
            }
            else if(type.startsWith("image/")){
                handleImage(intent);
            }
        }

        else if (type!=null && action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            if (type.startsWith("image/")) {
                handleMultipleImages(intent); // Handle multiple images being sent
            }
            else if(type.startsWith("application/pdf")){
                handleMultiplePDF(intent);
            }
        }*/


        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        pasteField = (EditText)findViewById(R.id.paste);
        WifiManager wifiMan = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        TextView ipadd = (TextView) findViewById(R.id.ipaddress);
        ipadd.setText("Point your PC browser to http://"+ip+":8080/Gen/" );
        try {
            HTTPD fileRendering = new HTTPD(getApplicationContext(), 8080);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        /*webView = (WebView) findViewById(R.id.webview);





             webView.setWebViewClient(new WebViewClient() {
                       @Override
                     public void onPageFinished(WebView view, String url) {
                               view.scrollTo(0, view.getBottom());
                          }
                   });
*/

        final String strURL = "http://" + ip + ":8080//Gen/index.html";
        fileServerURL = "http://" + ip + ":8080//Gen/index.html";
        if(intent.getAction()!=null)
        if(intent.getAction().equals(Intent.ACTION_SEND)) {
            ClipData clipData = intent.getClipData();
            Log.i("clipdata", clipData.toString());
            Uri uri = clipData.getItemAt(0).getUri();
            Log.i("uri",uri.toString());


            String absPath = getRealPathFromURI(uri);
            absPath = absPath.substring(absPath.lastIndexOf("emulated/0") + 11, absPath.length());
            Log.i("absPath", absPath);
            SharedPreferences.Editor editor = getSharedPreferences("ChatHistory", MODE_PRIVATE).edit();
            SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
            int no_of_chats = prefs.getInt("no_of_chats", 0);
            no_of_chats+=1;
            editor.putString(no_of_chats+"", "{type: 'image', content:'" + absPath+"'}");
            editor.putInt("no_of_chats",no_of_chats);
            editor.commit();
            String str = "<div class=\"pin\"><img src=\"../" + absPath + "\"></img></div>";
            appendContent(str.getBytes());
            mAdapter = new RecyclerViewAdapter(new String[]{"12334","sdfghj"}/*dataset*/,"Text", getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

        }

        ImageButton btnPaste = (ImageButton) findViewById(R.id.button2);
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("ChatHistory", MODE_PRIVATE).edit();
                SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
                int no_of_chats = prefs.getInt("no_of_chats", 0);
                no_of_chats+=1;
                editor.putString(no_of_chats+"", "{type: 'text', content:'" + pasteField.getText().toString()+"'}");
                editor.putInt("no_of_chats",no_of_chats);
                editor.commit();
                String text = "<div class=\"pin\"><p>" + pasteField.getText().toString() + "</p></div>";
                //String text = "<div style='width=100%;'>"+pasteField.getText().toString()+"</div>";
                appendContent(text.getBytes());
                mAdapter = new RecyclerViewAdapter(new String[]{"12334","sdfghj"}/*dataset*/,"Text", getApplicationContext());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        //webView.loadUrl(strURL);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String str = cursor.getString(column_index);
        cursor.close();
        return str;
    }

    void handleImage(Intent i){

        ArrayList<Uri> uris = i.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if(uris != null){
            Toast.makeText(MainActivity.this,"In file upload image", Toast.LENGTH_SHORT).show();
            //String text =
            //appendContent();
        }
    }

    void handlePDF(Intent i){
        Uri pdfUri = i.getParcelableExtra(Intent.EXTRA_MIME_TYPES);
        if(pdfUri != null){
            fileUpload((String.valueOf(pdfUri)));
        }
    }

    void handleMultipleImages(Intent i){
        ArrayList <Uri> imageMultipleUri = i.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if(imageMultipleUri != null){
            fileUpload((String.valueOf(imageMultipleUri)));
        }
    }

    void handleMultiplePDF(Intent i){
        ArrayList <Uri> pdfMultipleUri = i.getParcelableArrayListExtra(Intent.EXTRA_MIME_TYPES);
        if(pdfMultipleUri != null){
            fileUpload((String.valueOf(pdfMultipleUri)));
        }
    }

    private void appendContent(byte[] text){

        File f1 = new File(Environment.getExternalStorageDirectory().getPath(),"Gen");
        f1.mkdirs();
        Log.i("f1", String.valueOf(f1));
        File f = new File(f1.getPath()+File.separator+"index.html");
        if(!f.exists()) {
            try {
                String str = "<style>\n" +
                        "body {\n" +
                        "\tbackground: url(http://subtlepatterns.com/patterns/scribble_light.png) \n" +
                        "}\n" +
                        "\n" +
                        "#wrapper {\n" +
                        "\twidth: 90%;\n" +
                        "\tmax-width: 1100px;\n" +
                        "\tmin-width: 800px;\n" +
                        "\tmargin: 50px auto;\n" +
                        "}\n" +
                        "\n" +
                        "#columns {\n" +
                        "\t-webkit-column-count: 3;\n" +
                        "\t-webkit-column-gap: 10px;\n" +
                        "\t-webkit-column-fill: auto;\n" +
                        "\t-moz-column-count: 3;\n" +
                        "\t-moz-column-gap: 10px;\n" +
                        "\t-moz-column-fill: auto;\n" +
                        "\tcolumn-count: 3;\n" +
                        "\tcolumn-gap: 15px;\n" +
                        "\tcolumn-fill: auto;\n" +
                        "}\n" +
                        "\n" +
                        ".pin {\n" +
                        "\tdisplay: inline-block;\n" +
                        "\tbackground: #FEFEFE;\n" +
                        "\tborder: 2px solid #FAFAFA;\n" +
                        "\tbox-shadow: 0 1px 2px rgba(34, 25, 25, 0.4);\n" +
                        "\tmargin: 0 2px 15px;\n" +
                        "\twidth:80%;\n" +
                        "\t-webkit-column-break-inside: avoid;\n" +
                        "\t-moz-column-break-inside: avoid;\n" +
                        "\tcolumn-break-inside: avoid;\n" +
                        "\tpadding: 15px;\n" +
                        "\tpadding-bottom: 5px;\n" +
                        "\tbackground: -webkit-linear-gradient(45deg, #FFF, #F9F9F9);\n" +
                        "\topacity: 1;\n" +
                        "\t\n" +
                        "\t-webkit-transition: all .2s ease;\n" +
                        "\t-moz-transition: all .2s ease;\n" +
                        "\t-o-transition: all .2s ease;\n" +
                        "\ttransition: all .2s ease;\n" +
                        "}\n" +
                        "\n" +
                        ".pin img {\n" +
                        "\twidth: 100%;\n" +
                        "\tborder-bottom: 1px solid #ccc;\n" +
                        "\tpadding-bottom: 15px;\n" +
                        "\tmargin-bottom: 5px;\n" +
                        "}\n" +
                        "\n" +
                        ".pin p {\n" +
                        "\tfont: 12px/18px Arial, sans-serif;\n" +
                        "\tcolor: #333;\n" +
                        "\tmargin: 0;\n" +
                        "}\n" +
                        "\n" +
                        "@media (min-width: 960px) {\n" +
                        "\t#columns {\n" +
                        "\t\t-webkit-column-count: 4;\n" +
                        "\t\t-moz-column-count: 4;\n" +
                        "\t\tcolumn-count: 4;\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "@media (min-width: 1100px) {\n" +
                        "\t#columns {\n" +
                        "\t\t-webkit-column-count: 5;\n" +
                        "\t\t-moz-column-count: 5;\n" +
                        "\t\tcolumn-count: 5;\n" +
                        "\t}\n" +
                        "}\n" +
                        "</style>\n<div id=\"wrapper\"><div id=\"columns\">";
                FileOutputStream fout = new FileOutputStream(f, true);
                //String str2 = text.toString();
                fout.write((str).getBytes());
                fout.flush();
                fout.close();
                FileOutputStream fout1 = new FileOutputStream(f, true);
                //String str2 = text.toString();
                fout1.write(text);
                fout1.flush();
                fout1.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                FileOutputStream fout = new FileOutputStream(f, true);
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
    }

    private void fileUpload(String f){
        Log.i("File", String.valueOf(f));
        new UploadAsync().execute(f);
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
        if(intent.hasExtra("sendtext")){
            if(intent.getBooleanExtra("sendtext",false)){
                SharedPreferences.Editor editor = getSharedPreferences("ChatHistory", MODE_PRIVATE).edit();
                SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
                int no_of_chats = prefs.getInt("no_of_chats", 0);
                no_of_chats+=1;
                editor.putString(no_of_chats+"", "{type: 'text', content:'" + pasteField.getText().toString()+"'}");
                editor.putInt("no_of_chats",no_of_chats);
                editor.commit();
                String pastef = pasteField.getText().toString();
                String text1;
                if(pastef.contains("http://") || pastef.contains("https://") || pastef.contains("www.")){
                    text1 = "<div class=\"pin\"><a href='"+pastef+"'><p>" + pasteField.getText().toString() + "</p></a></div>";
                }
                else
                    text1 = "<div class=\"pin\"><p>" + pasteField.getText().toString() + "</p></div>";
                //String text = "<div style='width=100%;'>"+pasteField.getText().toString()+"</div>";
                appendContent(text1.getBytes());
                mAdapter = new RecyclerViewAdapter(new String[]{"12334","sdfghj"}/*dataset*/,"Text", getApplicationContext());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
        //Toast.makeText(getApplicationContext(), "Text Pasted",
                //Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        int serverResponseCode = 0;

        if (!sourceFile.isFile()) {

            runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("err", "source file does not exist");
                }
            });

            return 0;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(fileServerURL);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; filename=" + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(MainActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return serverResponseCode;

        } // End else block
    }

    private class UploadAsync extends AsyncTask<String, Void, Void> {

        private boolean fileUploadSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... f) {

           /* try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(fileServerURL);
                    InputStreamEntity entity = new InputStreamEntity(new FileInputStream(String.valueOf(f)),-1);
                    entity.setContentType("binary/octet-stream");
                    entity.setChunked(true);
                    httpPost.setEntity(entity);
                    Toast.makeText(FileUpload.this,"In background", Toast.LENGTH_SHORT).show();
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    fileUploadSuccess=true; //TODO have to check if file is uploaded successfully
            }catch (Exception e){
                e.printStackTrace();
            }*/

            uploadFile(f.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (fileUploadSuccess) {
                Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
            } else if (!fileUploadSuccess) {
                Toast.makeText(getApplicationContext(), "File not uploaded", Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mNotificationBuilder;
            mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext())

                    .setContentTitle(getResources().getString(R.string.app_name))
                            //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notif_logo))
                    .setSmallIcon(R.mipmap.ic_menu_share)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setAutoCancel(false)
                    .setOngoing(true)
                            //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                            //.setSound(soundUri)
                    .setOnlyAlertOnce(true)
                    //.setColor(getResources)
            ;
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("sendtext",true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            mNotificationBuilder.setContentText("Share the text on clipboard")
                    .setContentIntent(pIntent)

                    .setWhen(System.currentTimeMillis());

            mNotificationBuilder.addAction(R.mipmap.ic_menu_share,"SHARE",pIntent);
            mNotificationManager.notify(0, mNotificationBuilder.build());
            mNotificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
