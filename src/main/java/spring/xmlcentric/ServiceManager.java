package spring.xmlcentric;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 1. NoSuchBeanDefinitionException: No qualifying bean of type 'com.amazon....TransferService' available?
 * - "classpath*:/com/amazon/.../spring/xmlcentric/app-config.xml"
 * - but there is no xml in generated target
 * - in POM.xml, the resource is specified under: <resource><directory>configuration</directory></resource>
 *
 * 2. BeanDefinitionStoreException: Could not resolve placeholder 'jdbc.url' in string value "${jdbc.url}"
 * - application.properties is put under target/
 * - any directories or files placed within the resources directory are packaged in JAR
 *   with the exact same structure **starting at the base of the JAR**
 *
 ├── pom.xml
 └── src
 ├── main
 │   ├── java
 │   │   └── com
 │   │       └── chozhang
 │   │           └── app
 │   │               └── App.java
 │   └── resources
 │       ├── application.properties
 └── test
 ...
 *
 * $ jar -tf path/to/my-app-1.0-SNAPSHOT.jar
 * META-INF/
 * META-INF/MANIFEST.MF
 * com/
 * com/chozhang/
 * com/chozhang/app/
 * com/chozhang/app/App.class
 * application.properties
 * META-INF/maven/
 * META-INF/maven/com.chozhang.app/
 * META-INF/maven/com.chozhang.app/my-app/
 * META-INF/maven/com.chozhang.app/my-app/pom.xml
 * META-INF/maven/com.chozhang.app/my-app/pom.properties
 */
public class ServiceManager {
    public static final String XML_CONFIG_CLASSPATH = "META-INF/spring-configuration/app-config.xml";
    public static void main(String[] args) throws NoSuchFieldException {
        ApplicationContext context = new ClassPathXmlApplicationContext(XML_CONFIG_CLASSPATH);
        DataSource dataSource = context.getBean(DataSource.class);
        TransferService transferService = context.getBean(TransferService.class);
        System.out.println(transferService
                           .getClass().getDeclaredField("accountRepository"));
        System.out.println(dataSource.getUrl());
    }
}

class TransferService {
    private AccountRepository accountRepository;
    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}

class AccountRepository {
    private DataSource dataSource;
    public AccountRepository(DataSource dataSource) { this.dataSource = dataSource; }
}

class DataSource {
    private String url;
    private String username;
    private String password;

    public DataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public String getUrl() { return url; }
}
