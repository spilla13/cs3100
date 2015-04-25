package rsck.chalkboard;

import android.os.AsyncTask;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class DjangoFunctions
{

    /************* Three Account Creation Functions *************/
    public JSONObject accessReg(final JSONObject regEntries)
    {
        String urlString = "http://cs3100.brod.es:3100/add/user";
        JSONObject jObj = null;
        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(regEntries.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            jObj = new JSONObject(resultString);
        }
        catch(Exception e)
        {

        }

        return jObj;
    }

    public JSONObject authenticate(final String username, final String password)
    {
        String urlString = "http://cs3100.brod.es:3100/token/new.json";

        ArrayList<NameValuePair> postParameters;
        postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("username", username));
        postParameters.add(new BasicNameValuePair("password", password));


        JSONObject returnJSON = null;
        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(new UrlEncodedFormEntity(postParameters));

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);
            returnJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnJSON;
    }

    public boolean register(final String username, final String password, final String email)
    {
        JSONObject regEntries = new JSONObject();

        try
        {
            //create the JSONObject
            regEntries.put("password", password);
            regEntries.put("username", username);
            regEntries.put("email", email);
        }
        catch(Exception e)
        {

        }

        JSONObject retReg = accessReg(regEntries);
        if(retReg.toString().contains("false"))
            return false;

        retReg = authenticate(username, password);
        if(retReg.toString().contains("false"))
            return false;

        //System.out.println(retReg.toString());

        return true;
    }

    /************* General Access Function *************/
    public JSONObject access(final String dataToGet, final String userID, final String token, JSONObject query)
    {
        String urlString = "http://cs3100.brod.es:3100/get/" + dataToGet + "/?user=" + userID + "&token=" + token;

        JSONObject returnJSON = null;
        try
        {


            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(query.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            returnJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {

        }
        return returnJSON;
    }

    /************* General Add Function *************/
    public JSONObject add(final String dataToAdd, final String userID, final String token, JSONObject query)
    {
        String urlString = "http://cs3100.brod.es:3100/add/" + dataToAdd + "/?user=" + userID + "&token=" + token;
        JSONObject returnJSON = null;

        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(query.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            returnJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnJSON;
    }

    /************* General Edit Function *************/
    public JSONObject edit(final String dataToEdit, final String userID, final String token, JSONObject query)
    {
        String urlString = "http://cs3100.brod.es:3100/edit/" + dataToEdit + "/?user=" + userID + "&token=" + token;
        JSONObject returnJSON = null;

        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(query.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            returnJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnJSON;
    }

    /************* General Remove Function *************/
    public JSONObject remove(final String dataToRemove, final String userID, final String token, JSONObject query)
    {
        String urlString = "http://cs3100.brod.es:3100/rm/" + dataToRemove + "/?user=" + userID + "&token=" + token;
        JSONObject returnJSON = null;

        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(query.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            returnJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnJSON;
    }
}
