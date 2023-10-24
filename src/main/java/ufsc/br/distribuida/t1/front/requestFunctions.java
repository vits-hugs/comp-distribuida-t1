package ufsc.br.distribuida.t1.front;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.*;
import ufsc.br.distribuida.t1.front.circuitbreaker.GetRequest;
import ufsc.br.distribuida.t1.front.circuitbreaker.Requester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class requestFunctions {
    final static String URL = "http://localhost:8080/api/muntjacs";
    public static GetRequest makeRequestGetIDMunt(String url,int ID) {
        url = url + '/' + ID;
        GetRequest getRequest = new GetRequest(url);
        return getRequest;
    }


}
