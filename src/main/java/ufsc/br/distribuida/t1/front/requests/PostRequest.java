package ufsc.br.distribuida.t1.front.requests;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest implements Request {

    String url;
    JSONObject jsonBody;
    public PostRequest(String url,JSONObject jsonBody) {
        this.url = url;
        this.jsonBody = jsonBody;
    }

    @Override
    public JSONObject DO() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        // Habilita a saída para enviar dados no corpo da solicitação
        con.setDoOutput(true);

        // Obtém a saída da conexão
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }


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
