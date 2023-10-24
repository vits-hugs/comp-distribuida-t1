package ufsc.br.distribuida.t1.front.circuitbreaker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class GetRequest implements Requester {

    final static String URL = "http://localhost:8080/api/muntjacs";

    private String url;

    public GetRequest(String url) {
        this.url = url;
    }

    @Override
    public JSONObject DO() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();
        JSONObject json = new JSONObject(content.toString());

        return json;
    }
}
