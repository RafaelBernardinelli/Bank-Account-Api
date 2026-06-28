package br.com.rafaelb.bankaccount.application.ports;

import br.com.rafaelb.bankaccount.application.event.OperationEvent;

public interface OperationPublisher {

    void publish(OperationEvent event);

}