package br.com.rafaelb.bankaccount.presentation.controller;

import br.com.rafaelb.bankaccount.presentation.request.CreateAccountRequest;
import br.com.rafaelb.bankaccount.presentation.response.AccountResponse;
import br.com.rafaelb.bankaccount.application.usecase.CreateAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operações relacionadas a contas bancárias")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;

    @Operation(
            summary = "Criar nova conta",
            description = "Cria uma nova conta bancária com número, dígito e documento do titular (CPF/CNPJ)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @RequestBody @Valid CreateAccountRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createAccountUseCase.execute(request));
    }
}