package br.com.rafaelb.bankaccount.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {

    private String field;
    private String description;
}