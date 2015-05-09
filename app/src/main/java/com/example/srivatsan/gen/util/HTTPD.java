package com.example.srivatsan.gen.util;

import android.content.Context;
import android.os.Environment;

import java.io.IOException;

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
}