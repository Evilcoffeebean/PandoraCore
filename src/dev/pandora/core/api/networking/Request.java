package dev.pandora.core.api.networking;

import dev.pandora.core.utils.UtilLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Synch on 2017-03-13.
 */
public class Request {

    //Vengeancey

    private static final String API_HOST = "whatever";
    private Queue<String> postQueue = new LinkedBlockingQueue();

    public Request() {
        Thread t = new Thread(() -> {
            long last = System.currentTimeMillis();
           while (true) {
               if (System.currentTimeMillis() - last < 10) continue;
               if (postQueue.isEmpty()) continue;
               String post = postQueue.remove();
               post(post);
               UtilLogger.log("Request", "Executing POST Request: Queue size is: " + postQueue.size());
               last = System.currentTimeMillis();
           }
        });
        t.start();
    }

    //Setting up connectors which won't be a part of the accessible API
    private String get(String url) {
        try {
            URL website = new URL(API_HOST + url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Could not fetch data using the primary Pandora node";
        }
    }

    protected void post(String url) {
        try {
            URL u = new URL(API_HOST + url);
            System.out.println(u.toString());
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            UtilLogger.log("Response", "Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

