package ru.ifmo.monolith.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class TransactionalConfig {

    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        var userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() throws Throwable {
        var userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn({"userTransaction", "atomikosTransactionManager"})
    public PlatformTransactionManager transactionManager() throws Throwable {
        var userTransaction = userTransaction();
        var atomikosTransactionManager = atomikosTransactionManager();
        var jtaTransactionManager = new JtaTransactionManager(userTransaction, atomikosTransactionManager);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    @Bean(name = "atomikosJtaPlatform")
    public AtomikosJtaPlatform atomikosJtaPlatform() throws Throwable {
        AtomikosJtaPlatform.setTransactionManager(atomikosTransactionManager());
        AtomikosJtaPlatform.setUserTransaction(userTransaction());
        return new AtomikosJtaPlatform();
    }
}
