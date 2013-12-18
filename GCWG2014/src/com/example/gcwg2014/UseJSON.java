package com.example.gcwg2014;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class UseJSON {
	
	static final String TAG_RESULTS = "results";
    static final String TAG_GEO = "geometry";
    static final String TAG_LOCATION = "location";
    static final String TAG_LAT = "lat";
    static final String TAG_LNG = "lng";
    private String lat = null;
    private String lng = null;
    private JSONArray res = null;

	public LatLng getPosition(String arg0){
		String locationString = arg0;
		String x = null;
		char space = " ".charAt(0);
		String[] streetArray = null;
		int j=0;
		for(int i=0; i>locationString.length(); i++){
			
			if(locationString.charAt(i)!=space){
					x = x+String.valueOf(locationString.charAt(i));
			}
			else {
				streetArray[j]=x;
				j++;
			}
		}
		streetArray[j]=x;
		
		String streetString=null;
		
		for(int i=0; i>streetArray.length; i++){
			streetString = streetString+", "+streetArray[i];
		}


        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address="+streetString+"&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            res = jsonObject.getJSONArray(TAG_RESULTS);
            for(int i = 0; i < res.length(); i++){
                JSONObject c = res.getJSONObject(i);

                JSONObject loc = c.optJSONObject(TAG_GEO).optJSONObject(TAG_LOCATION);

            String lat =loc.getString(TAG_LAT);

            String lng = loc.getString(TAG_LNG);


            }
        }
        catch (JSONException e){ }


double lat1 = Double.parseDouble(lat);  
double lng1 = Double.parseDouble(lng);

LatLng position = new LatLng(lat1,lng1);
return position;
	
	}
}
