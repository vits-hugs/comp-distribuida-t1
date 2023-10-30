package ufsc.br.distribuida.t1.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import ufsc.br.distribuida.t1.Muntjac;
import ufsc.br.distribuida.t1.front.requests.GetRequest;
import ufsc.br.distribuida.t1.front.requests.PostRequest;
import ufsc.br.distribuida.t1.front.requests.PutRequest;


public class requestFunctions {
    public static GetRequest makeRequestGetIDMunt(String url, int id) {
        url = url + '/' + id;
        return new GetRequest(url);
    }

    public static PostRequest makeRequestPostMunt(String url,Muntjac muntjac) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(muntjac);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return new PostRequest(url,new JSONObject(jsonString));
    }

    public static PutRequest makeRequestModifyMunt(String url,int id, Muntjac muntjac) {
        url = url + '/' + id;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(muntjac);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return new PutRequest(url,new JSONObject(jsonString));


    }

}
