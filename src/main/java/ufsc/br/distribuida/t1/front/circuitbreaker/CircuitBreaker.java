package ufsc.br.distribuida.t1.front.circuitbreaker;

import org.json.JSONObject;
import ufsc.br.distribuida.t1.front.requests.Request;

import java.io.IOException;

public class CircuitBreaker
{
    private ICircuitBreakerStateStore stateStore;

//    private object halfOpenSyncObject = new object ();
    public boolean IsClosed() { return stateStore.IsClosed(); }

    public boolean IsOpen() {  return !IsClosed(); }

    public JSONObject ExecuteAction(Request action)
    {
        try {
            JSONObject obj = action.DO();
            return obj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        if (IsOpen())
//        {
////             The circuit breaker is Open.
//
//        }
//
//        // The circuit breaker is Closed, execute the action.
//        try
//        {
////            action();
//        }
//        catch (Exception ex)
//        {
//            // If an exception still occurs here, simply
//            // retrip the breaker immediately.
//            this.TrackException(ex);
//
//            // Throw the exception so that the caller can tell
//            // the type of exception that was thrown.
////            throw;
//        }
    }

    private void TrackException(Exception ex)
    {
        // For simplicity in this example, open the circuit breaker on the first exception.
        // In reality this would be more complex. A certain type of exception, such as one
        // that indicates a service is offline, might trip the circuit breaker immediately.
        // Alternatively it might count exceptions locally or across multiple instances and
        // use this value over time, or the exception/success ratio based on the exception
        // types, to open the circuit breaker.
        this.stateStore.Trip(ex);
    }
}