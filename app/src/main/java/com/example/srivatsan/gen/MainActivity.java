package com.example.srivatsan.gen;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.srivatsan.gen.util.HTTPD;

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
    String fileServerURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
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

        webView = (WebView) findViewById(R.id.webview);



                try {
                    HTTPD fileRendering = new HTTPD(getApplicationContext(), 8080);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

             webView.setWebViewClient(new WebViewClient() {
                       @Override
                     public void onPageFinished(WebView view, String url) {
                               view.scrollTo(0, view.getBottom());
                          }
                   });

        final String strURL = "http://" + ip + ":8080//Gen/index.html";
        fileServerURL = "http://" + ip + ":8080//Gen/index.html";

        if(intent.getClipData()!=null) {
            ClipData clipData = intent.getClipData();
            Log.i("clipdata", clipData.toString());
            Uri uri = clipData.getItemAt(0).getUri();
            Log.i("uri",uri.toString());


            String absPath = getRealPathFromURI(uri);
            absPath = absPath.substring(absPath.lastIndexOf("emulated/0") + 11, absPath.length());
            Log.i("absPath", absPath);

            String str = "<div style='width=100%'><img style='width:50%' src=\"../" + absPath + "\"></img></div>";
            appendContent(str.getBytes());
            webView.loadUrl(strURL);

        }

        ImageButton btnPaste = (ImageButton) findViewById(R.id.button2);
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "<div style='width=100%;'>"+pasteField.getText().toString()+"</div>";
                appendContent(text.getBytes());
                webView.loadUrl(strURL);
            }
        });
        webView.loadUrl(strURL);
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
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
