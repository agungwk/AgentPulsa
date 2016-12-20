package com.bangjoni.agentpulsa.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by agung.kurniawan on 7/4/2016.
 */
public class WebRequest {

    private static final String TAG = "WebRequest";

    static String response = null;
    public final static int GETRequest = 1;
    public final static int POSTRequest = 2;

    public WebRequest() {
    }
    /**
     * Making web service call
     *
     * @url - url to make web request
     * @requestmethod - http request method
     */
    public static String makeWebServiceCall(String url, int requestmethod) throws IOException{
        return makeWebServiceCall(url, requestmethod, null);
    }
    /**
     * Making web service call
     *
     * @url - url to make web request
     * @requestmethod - http request method
     * @params - http request params
     */
    public static String makeWebServiceCall(String urladdress, int requestmethod,
                                     HashMap<String, String> params) throws IOException{
        URL url;
        String response = "";
        try {
            url = new URL(urladdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (requestmethod == POSTRequest) {
                conn.setRequestMethod("POST");
            } else if (requestmethod == GETRequest) {
                conn.setRequestMethod("GET");
            }

            if (params != null) {
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestresult.append("&");
                    requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    requestresult.append("=");
                    requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(requestresult.toString());

                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        } catch (IOException e) {
            throw new IOException("Error while connecting to server, please try again in a moment");
        }
        return response;
    }

    public static String makeJSONPostCall(String urladdress, JSONObject json) throws IOException{
        URL url;
        String response = "";
        try {
            String jsonMessage = json.toString();

            Log.d(TAG, "Request URL ==> "+urladdress);
            Log.d(TAG, "Request data ==> "+jsonMessage);

            url = new URL(urladdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Content-Type", "application/json");
//            conn.setFixedLengthStreamingMode(jsonMessage.getBytes().length);

            //open
            conn.connect();
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonMessage);
            os.flush();
            os.close();

            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        } catch (IOException e) {
            throw new IOException("Error while connecting to server, please try again in a moment");
        }

        Log.d(TAG, "Response ==> "+response);
        return response;
    }
}
