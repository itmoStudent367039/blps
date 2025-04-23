package ru.ifmo.monolith.config;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import lombok.Setter;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

public class AtomikosJtaPlatform extends AbstractJtaPlatform {

    @Setter
    private static TransactionManager transactionManager;
    @Setter
    private static UserTransaction userTransaction;

    @Override
    protected TransactionManager locateTransactionManager() {
        return transactionManager;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return userTransaction;
    }
}