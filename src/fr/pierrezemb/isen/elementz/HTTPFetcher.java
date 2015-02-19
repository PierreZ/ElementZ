package fr.pierrezemb.isen.elementz;

/**
 * Created by pierrezemb on 19/02/15.
 * Based on http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 * https://stackoverflow.com/questions/21404252/post-request-send-json-data-java-httpurlconnection
 */

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HTTPFetcher {



    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://gl.pierrezemb.fr";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    public List<Player> sendPost(String name, int score) throws Exception {


        String url = "http://gl.pierrezemb.fr";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String input = "{ \"name\": \""+name+"\",\"score\":\""+score+"\"}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(input.getBytes());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        // http://www.javabeat.net/parsing-json-using-java-and-gson-library/
        Gson myGson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray userArray =  jsonParser.parse(response.toString()).getAsJsonArray();
        List PlayerUsers = new ArrayList<>();
        for ( JsonElement aUser : userArray ){
           Player aPlayerUser = myGson.fromJson(aUser, Player.class);
            PlayerUsers.add(aPlayerUser);
        }
        return PlayerUsers;
    }

}