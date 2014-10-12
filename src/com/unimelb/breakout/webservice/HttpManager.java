package com.unimelb.breakout.webservice;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.unimelb.breakout.utils.JsonUtils;

import android.net.Uri;
import android.util.Log;

/**
 * This class is a utility class for making HTTP calls.
 * 
 * It can be used for building a HTTP connection with the server. 
 *
 * @author Siyuan Zhang
 */
public class HttpManager {
    private static final int CONNECTION_TIMEOUT =  (int) TimeUnit.SECONDS.toMillis(10);

    // Note that this object will handle cookies for us automatically.
    private final DefaultHttpClient httpClient = createDefaultHttpClient();

    /**
     * Get contents of a uri converted from JSON.
     */
    public <T> T getJson(Uri uri, Class<T> classOfT) throws IOException {
        return JsonUtils.fromJson(getString(uri), classOfT);
    }

    /**
     * Get contents of a uri as a string.
     */
    public String getString(Uri uri) throws IOException {
        return new String(getBytes(uri), Charsets.UTF_8);
    }

    /**
     * Get contents of a uri as a byte array.
     */
    public byte[] getBytes(Uri uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri.toString());
        HttpResponse response = httpClient.execute(httpGet);
        InputStream in = response.getEntity().getContent();
        try {
            return ByteStreams.toByteArray(in);
        } finally {
            close(in);
        }
    }

    /**
     * Close the input stream.
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e("HTTP", "Failed to close a closeable e: " + e);
            }
        }
    }
    
    /**
     * Get uri and ignore any response.
     */
    public void get(Uri uri) throws IOException {
        httpClient.execute(new HttpGet(uri.toString()));
    }

    /**
     * Post url encoded data to uri and return results converted from JSON.
     */
    public <T> T postJson(Uri uri, List<NameValuePair> dataToUrlEncode, Class<T> classOfT)
            throws IOException {
        String json = postString(uri, dataToUrlEncode);
        return JsonUtils.fromJson(json, classOfT);
    }

    /**
     * Post url encoded data to uri and return results as a String.
     */
    public String postString(Uri uri, List<NameValuePair> dataToUrlEncode) throws IOException {
        return new String(postBytes(uri, dataToUrlEncode), Charsets.UTF_8);
    }

    /**
     * Post url encoded data to uri and return results as a byte array.
     */
    public byte[] postBytes(Uri uri, List<NameValuePair> dataToUrlEncode) throws IOException {
        HttpPost httpPost = new HttpPost(uri.toString());
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(new UrlEncodedFormEntity(dataToUrlEncode));
        HttpResponse response = httpClient.execute(httpPost);
        InputStream in = response.getEntity().getContent();
        try {
            return ByteStreams.toByteArray(in);
        } finally {
            close(in);
        }
    }

    /**
     * Create a DefaultHttpClient with some default options set.
     * This method implements the Globals.SSL_IGNORE_HOSTNAME option.
     */
    public static DefaultHttpClient createDefaultHttpClient() {
        SchemeRegistry schemeRegistry = new DefaultHttpClient().getConnectionManager().getSchemeRegistry();
        // Create thread safe http client.
        HttpParams defaultParams = new DefaultHttpClient().getParams();
        ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(defaultParams, schemeRegistry);
        DefaultHttpClient httpClient = new DefaultHttpClient(clientConnectionManager, defaultParams);
        // Set timeout.
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), CONNECTION_TIMEOUT);
        return httpClient;
    }
}
