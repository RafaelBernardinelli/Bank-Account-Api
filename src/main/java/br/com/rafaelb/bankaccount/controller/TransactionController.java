package br.com.rafaelb.bankaccount.controller;

import br.com.rafaelb.bankaccount.application.dto.request.CreateAccountRequest;
import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.dto.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.dto.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.dto.response.OperationReceiptResponse;
import br.com.rafaelb.bankaccount.application.dto.response.OperationResponse;
import br.com.rafaelb.bankaccount.application.dto.response.StatementResponse;
import br.com.rafaelb.bankaccount.application.dto.response.TransactionResponse;
import br.com.rafaelb.bankaccount.application.usecase.*;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operações financeiras: depósito, saque, transferência e extrato")
public class TransactionController {

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferUseCase transferUseCase;
    private final GetStatementUseCase getStatementUseCase;
    private final GetOperationReceiptUseCase getOperationReceiptUseCase;

    @Operation(
            summary = "Realizar depósito",
            description = "Realiza um depósito em uma conta bancária"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/deposits")
    public ResponseEntity<OperationResponse> deposit(
            @RequestBody @Valid DepositRequest request
    ) {
        return ResponseEntity.ok(depositUseCase.execute(request));
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
    @PostMapping("/withdrawals")
    public ResponseEntity<OperationResponse> withdraw(
            @RequestBody @Valid WithdrawRequest request
    ) {
        return ResponseEntity.ok(withdrawUseCase.execute(request));
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
    @PostMapping("/transfers")
    public ResponseEntity<OperationResponse> transfer(
            @RequestBody @Valid TransferRequest request
    ) {
        return ResponseEntity.ok(transferUseCase.execute(request));
    }

    @Operation(
            summary = "Consultar extrato",
            description = "Retorna o extrato de transações de uma conta com filtros opcionais"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{accountId}/statement")
    public ResponseEntity<StatementResponse> statement(
            @Parameter(description = "ID da conta", example = "1")
            @PathVariable Long accountId,

            @Parameter(description = "Data inicial do filtro")
            @RequestParam(required = false) Instant startDate,

            @Parameter(description = "Data final do filtro")
            @RequestParam(required = false) Instant endDate,

            @Parameter(description = "Tipo de transação (DEPOSIT, WITHDRAW, TRANSFER)")
            @RequestParam(required = false) TransactionType transactionType,

            @PageableDefault(
                    size = 100,
                    page = 0,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                getStatementUseCase.execute(accountId, startDate, endDate, transactionType, pageable)
        );
    }

    @Operation(
            summary = "Buscar recibo de operação",
            description = "Retorna o recibo de uma operação financeira pelo ID da transação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recibo encontrado"),
            @ApiResponse(responseCode = "404", description = "Operação não encontrada")
    })
    @GetMapping("/{operationId}/receipt")
    public ResponseEntity<OperationReceiptResponse> getOperationReceipt(
            @Parameter(description = "ID da operação", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID operationId
    ) {
        return ResponseEntity.ok(getOperationReceiptUseCase.execute(operationId));
    }
}