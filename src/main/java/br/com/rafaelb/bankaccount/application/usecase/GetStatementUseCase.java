package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.application.dto.response.PaginatedResponse;
import br.com.rafaelb.bankaccount.application.dto.response.StatementResponse;
import br.com.rafaelb.bankaccount.application.dto.response.TransactionResponse;
import br.com.rafaelb.bankaccount.application.exception.AccountNotFoundException;
import br.com.rafaelb.bankaccount.application.exception.InvalidDateRangeException;
import br.com.rafaelb.bankaccount.application.mapper.AccountTransactionMapper;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import br.com.rafaelb.bankaccount.domain.repository.AccountRepository;
import br.com.rafaelb.bankaccount.domain.repository.AccountTransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStatementUseCase {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;
    private final AccountTransactionMapper accountTransactionMapper;

    @Transactional(readOnly = true)
    public StatementResponse execute(Long accountId, Instant startDate, Instant endDate, TransactionType transactionType, Pageable pageable) {
        validatePeriod(startDate, endDate);

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Page<AccountTransaction> transactions = findTransactions(
                accountId,
                startDate,
                endDate,
                transactionType,
                pageable
        );

        List<TransactionResponse> content =
                transactions.getContent()
                        .stream()
                        .map(accountTransactionMapper::toTransactionResponse)
                        .toList();

        return StatementResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountDigit(account.getAccountDigit())
                .balance(account.getBalance())
                .transactions(
                        PaginatedResponse.<TransactionResponse>builder()
                                .content(content)
                                .totalElements(transactions.getTotalElements())
                                .totalPages(transactions.getTotalPages())
                                .currentPage(transactions.getNumber())
                                .pageSize(transactions.getSize())
                                .build()
                )
                .build();
    }

    private Page<AccountTransaction> findTransactions(
            Long accountId,
            Instant startDate,
            Instant endDate,
            TransactionType transactionType,
            Pageable pageable) {

        boolean hasPeriod = startDate != null && endDate != null;
        boolean hasType = transactionType != null;

        if (hasPeriod && hasType) {
            return transactionRepository
                    .findByAccountIdAndCreatedAtBetweenAndTransactionType(
                            accountId,
                            startDate,
                            endDate,
                            transactionType,
                            pageable
                    );
        }

        if (hasPeriod) {
            return transactionRepository
                    .findByAccountIdAndCreatedAtBetween(
                            accountId,
                            startDate,
                            endDate,
                            pageable
                    );
        }

        if (hasType) {
            return transactionRepository
                    .findByAccountIdAndTransactionType(
                            accountId,
                            transactionType,
                            pageable
                    );
        }

        return transactionRepository.findByAccountId(
                accountId,
                pageable
        );
    }

    private void validatePeriod(Instant startDateTime, Instant finalDateTime) {

        if (startDateTime != null
                && finalDateTime != null
                && startDateTime.isAfter(finalDateTime)) {

            throw new InvalidDateRangeException(
                    "Start date must be before end date."
            );
        }

    }
}
