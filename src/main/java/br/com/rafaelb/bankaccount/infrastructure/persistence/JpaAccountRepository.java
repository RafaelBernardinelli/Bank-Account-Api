package br.com.rafaelb.bankaccount.infrastructure.persistence;

import br.com.rafaelb.bankaccount.domain.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumberAndAccountDigit(
            String accountNumber,
            String accountDigit
    );

    boolean existsByHolderDocument(
            String holderDocument
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Account a
        WHERE a.id = :id
    """)
    Optional<Account> findByIdForUpdate(Long id);

}