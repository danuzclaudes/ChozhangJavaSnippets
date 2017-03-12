package spring.xmlcentric;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class AppConfig {

    @Inject
    private DataSource dataSource;

    @Bean(name = "accountRepo")  // Todo: no need to annotate @Inject on @Bean methods??
    public AccountRepository accountRepository() {
        // <bean id="accountRepository" class="..."><constructor-arg ref="jdbcDataSource"/></bean>
        return new AccountRepository(dataSource);
    }

    @Bean
    public TransferService transferService(@Qualifier("accountRepo") AccountRepository accountRepository) {
        // <bean id="transferService" class="..."><constructor-arg ref="accountRepository"/></bean>
        // can use inter-bean injection as well;
        return new TransferService(accountRepository);
    }

}