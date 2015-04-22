package rsck.chalkboard;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;
import java.io.*;
import java.net.*;

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
        String urlString = "http://cs3100.brod.es:3100/token/new.json?username=" + username + "&password=" + password;
        JSONObject returnJSON = null;
        try
        {
            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
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
    public JSONObject access(final String dataToGet, final String userID, final String token)
    {
        String urlString = "http://cs3100.brod.es:3100/get/" + dataToGet + "/?user=" + userID + "&token=" + token;
        JSONObject passedJSON = null;
        JSONObject returnedJSON = null;
        try
        {
            passedJSON = new JSONObject(dataToGet);

            //HTTP code to contact the Django server and send it the JSON to register
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);

            //passes the Django server the JSON for registration
            StringEntity regString = new StringEntity(passedJSON.toString());
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(regString);

            HttpResponse response = httpclient.execute(httppost);

            //return string from Django server
            HttpEntity httpEntity = response.getEntity();
            String resultString = EntityUtils.toString(httpEntity);

            returnedJSON = new JSONObject(resultString);
        }
        catch(Exception e)
        {

        }

        return returnedJSON;
    }
}
