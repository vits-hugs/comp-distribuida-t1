package ufsc.br.distribuida.t1.front.circuitbreaker;

import java.security.Timestamp;

public interface ICircuitBreakerStateStore {
    CircuitBreakerStateEnum State();

    Exception LastException();

    Timestamp LastStateChangedDateUtc();

    void Trip(Exception ex);

    void Reset();

    void HalfOpen();

    boolean IsClosed();
}
