package ufsc.br.distribuida.t1.front.requests;

import org.json.JSONObject;

import java.io.IOException;

public interface Request {

    JSONObject DO() throws IOException;

}
