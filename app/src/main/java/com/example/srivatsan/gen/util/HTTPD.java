package com.example.srivatsan.gen.util;

import android.net.wifi.WifiConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLEngineResult;

/**
 * Created by aravind on 9/5/15.
 */
public class HTTPD extends com.example.srivatsan.gen.util.NanoHTTPD {

    /**
     * Constructs an HTTP server on given port.
     */
    public HTTPD() throws IOException {
        super(8080);
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties params, Properties files, InputStream body)
    {
        String msg = "";
        msg += "<b>Sugan rocks</b>";
        return new com.example.srivatsan.gen.util.NanoHTTPD.Response("200", "text/html", msg);
    }

}