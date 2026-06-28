package br.com.rafaelb.bankaccount.presentation.controller;

import br.com.rafaelb.bankaccount.presentation.response.TransactionDetailsResponse;
import br.com.rafaelb.bankaccount.presentation.response.StatementResponse;
import br.com.rafaelb.bankaccount.application.usecase.GetTransactionDetailsUseCase;
import br.com.rafaelb.bankaccount.application.usecase.GetStatementUseCase;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operações financeiras: extrato e detalhamento")
public class AccountStatementController {

    private final GetStatementUseCase getStatementUseCase;
    private final GetTransactionDetailsUseCase getTransactionDetailsUseCase;

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
            summary = "Buscar detalhes de uma transação",
            description = "Retorna os detalhes de uma transação específica, incluindo informações como tipo, valor, data e status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{operationId}/transaction")
    public ResponseEntity<TransactionDetailsResponse> details(
            @Parameter(description = "ID da operação", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID operationId
    ) {
        return ResponseEntity.ok(getTransactionDetailsUseCase.execute(operationId));
    }
}