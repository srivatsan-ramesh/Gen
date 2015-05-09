package com.example.srivatsan.gen.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by aravind on 9/5/15.
 */
public class HTTPD extends com.example.srivatsan.gen.util.NanoHTTPD {

    /**
     * Constructs an HTTP server on given port.
     */
    Context mContext;
    public HTTPD(Context mContext, int port) throws IOException {
        super(port, Environment.getExternalStorageDirectory());
        this.mContext = mContext;
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties params, Properties files, InputStream body)
    {
        Log.i("uri",uri);
        Log.i("method", method);
        Log.i("params", params.toString());
        if(uri.equals("/")) {
            String msg = "";
            msg += "<b>Sugan rocks</b>\n";
            msg += "<img src=\"file:///sdcard/Screenshot_2015-04-26-16-24-06.png\"></img>";
            return new com.example.srivatsan.gen.util.NanoHTTPD.Response("200", "text/html", msg);
        }
        else {

        }
        return new com.example.srivatsan.gen.util.NanoHTTPD.Response("200", "text/html", "none of these");
    }

}