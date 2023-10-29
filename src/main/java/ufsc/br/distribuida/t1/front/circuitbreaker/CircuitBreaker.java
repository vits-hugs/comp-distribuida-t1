package ufsc.br.distribuida.t1.front.circuitbreaker;

import org.json.JSONObject;
import ufsc.br.distribuida.t1.front.requests.Request;

import java.io.FileNotFoundException;
import java.net.ConnectException;

public class CircuitBreaker {
    public enum StateEnum {
        CLOSED,
        HALF_OPEN,
        OPEN;
    }

    private StateEnum stateStore;

    private int success;
    public final static int PENALTY = 10000;

    private int fileErrors;
    private int generalError;

    private long timeLastTrip;

    public CircuitBreaker(){
        this.stateStore = StateEnum.CLOSED;
        this.fileErrors = 0;
        this.timeLastTrip = 0;
        this.success = 0;
    }

    void setState(StateEnum state){
        this.stateStore = state;
    }

    StateEnum getState(){
        return this.stateStore;
    }


    public JSONObject ExecuteAction(Request action) throws Exception {

        if (getState().equals(StateEnum.OPEN)) {
//             The circuit breaker is Open.
            if ((System.currentTimeMillis() - this.timeLastTrip) > PENALTY) {
                this.setState(StateEnum.HALF_OPEN);
            } else {
                System.out.println(this.getState());
                System.out.println("In circuit Break mode");
                System.out.println("time left" + String.valueOf(PENALTY - (System.currentTimeMillis() - this.timeLastTrip)));
                throw new RuntimeException("In circuit Break mode");
            }
        }

        // The circuit breaker is Half-open or Closed, execute the action.
        try {
            JSONObject obj = action.DO();
            this.success++;
            if (this.success >= 3){
                this.fileErrors = 0;
                this.generalError = 0;
                this.setState(StateEnum.CLOSED);
            }
            System.out.println(this.getState());
            return obj;
        } catch (Exception ex) {
            TrackException(ex);
            System.out.println(this.getState());
            throw ex;
        }
    }

    private void TrackException(Exception ex) {
        this.timeLastTrip = System.currentTimeMillis();
        this.success = 0;

        // 3 general or file errors trip a breaker, one connect too
        if (ex.getClass().equals(FileNotFoundException.class)){
            System.out.println("FileNotFoundException");
            this.fileErrors++;
            if (this.fileErrors >= 3){
                this.setState(StateEnum.OPEN);
            }
        } else if (ex.getClass().equals(ConnectException.class)){
            System.out.println("ConnectException");
            this.setState(StateEnum.OPEN);
        } else {
            this.generalError++;
            if (this.generalError >= 3){
                this.setState(StateEnum.OPEN);
            }
        }

        if (this.getState().equals(StateEnum.HALF_OPEN)){
            this.setState(StateEnum.OPEN);
        }
    }
}