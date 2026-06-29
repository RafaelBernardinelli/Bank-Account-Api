package br.com.rafaelb.bankaccount.presentation.controller;

import br.com.rafaelb.bankaccount.presentation.request.DepositRequest;
import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.presentation.response.OperationResponse;
import br.com.rafaelb.bankaccount.application.strategy.OperationExecutionStrategy;
import br.com.rafaelb.bankaccount.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operações financeiras: depósito, saque, transferência e extrato")
public class TransactionController {

    private final OperationExecutionStrategy strategy;

    @Operation(
            summary = "Realizar depósito",
            description = "Realiza um depósito em uma conta bancária"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/deposit")
    public ResponseEntity<OperationResponse> deposit(
            @RequestBody @Valid DepositRequest request
    ) {
        return ResponseEntity.ok(strategy.deposit(request));
    }

    @Operation(
            summary = "Realizar saque",
            description = "Realiza um saque em uma conta bancária"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<OperationResponse> withdraw(
            @RequestBody @Valid WithdrawRequest request
    ) {
        return ResponseEntity.ok(strategy.withdraw(request));
    }

    @Operation(
            summary = "Realizar transferência",
            description = "Transfere valores entre contas bancárias"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conta origem/destino não encontrada")
    })
    @PostMapping("/transfer")
    public ResponseEntity<OperationResponse> transfer(
            @RequestBody @Valid TransferRequest request
    ) {
        return ResponseEntity.ok(strategy.transfer(request));
    }
}