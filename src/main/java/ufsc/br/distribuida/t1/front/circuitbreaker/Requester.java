package ufsc.br.distribuida.t1.front.circuitbreaker;

import org.json.JSONObject;

import java.io.IOException;

public interface Requester {

    JSONObject DO() throws IOException;

}
