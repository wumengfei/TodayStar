package com.ctbri.staroftoday.volley.upload;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @ description:上传json
 */
public class MultipartRequest extends Request<JSONObject>{   
    private MultipartRequestParams params = null;
    private HttpEntity httpEntity = null;
    private final Listener<JSONObject> mListener;  
    
    public MultipartRequest(int method,MultipartRequestParams params, String url,
		Response.Listener<JSONObject> listener, Response.ErrorListener errorListener
		) {
        super(method, url, errorListener);
        this.params = params;
        this.mListener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String str = new String(baos.toByteArray());
            Log.e("test", "bodyString is :" + str);
        }
        return baos.toByteArray();
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // TODO Auto-generated method stub
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    public String getBodyContentType() {
        httpEntity.getContentType().getValue();
        return httpEntity.getContentType().getValue();
    }

    @Override  
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {  
    	try {
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }  }  
  
    @Override  
    protected void deliverResponse(JSONObject response) {  
        if (mListener != null) {  
            mListener.onResponse(response);  
        }  
    }
}
