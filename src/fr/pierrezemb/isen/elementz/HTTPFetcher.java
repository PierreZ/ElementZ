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

public class HTTPFetcher {

    public static String url = "http://elementz.pierrezemb.fr";
    //public static String url = "http://127.0.0.1:3000/";

    public Gson myGson;


    // HTTP GET request
    public List<Player> sendGet() throws Exception {

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

        // How to use Gson
        // http://www.javabeat.net/parsing-json-using-java-and-gson-library/
        myGson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray userArray =  jsonParser.parse(response.toString()).getAsJsonArray();
        List PlayerUsers = new ArrayList<fr.pierrezemb.isen.elementz.Player>();
        for ( JsonElement aUser : userArray ){
            fr.pierrezemb.isen.elementz.Player aPlayerUser = myGson.fromJson(aUser, fr.pierrezemb.isen.elementz.Player.class);
            PlayerUsers.add(aPlayerUser);
        }
        return PlayerUsers;

    }

    // HTTP POST request
    public void sendPost(String name, int score) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        System.out.println(name+":"+score);

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        myGson = new Gson();

        Player p = new Player();
        p.name = name;
        p.score = score;

        String input = myGson.toJson(p);
        System.out.println(input);

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
    }

}