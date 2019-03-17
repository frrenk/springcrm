package pl.piaseckif.springrest.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("pl.piaseckif.springrest")
@PropertySource({"classpath:persistence-mysql.properties"})
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Bean
    public DataSource dataSource() {

        ComboPooledDataSource ds = new ComboPooledDataSource();

        try {
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        logger.info("jdbc url = "+env.getProperty("jdbc.url"));
        logger.info("jdbc user = "+env.getProperty("jdbc.user"));

        ds.setJdbcUrl(env.getProperty("jdbc.url"));
        ds.setUser(env.getProperty("jdbc.user"));
        ds.setPassword(env.getProperty("jdbc.password"));

        ds.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        ds.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
        ds.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
        ds.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

        return ds;
    }

    @Bean
    public DataSource securityDataSource() {
        ComboPooledDataSource ds = new ComboPooledDataSource();

        try {
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        logger.info("jdbcs url = "+env.getProperty("jdbcs.url"));
        logger.info("jdbcs user = "+env.getProperty("jdbcs.user"));

        ds.setJdbcUrl(env.getProperty("jdbcs.url"));
        ds.setUser(env.getProperty("jdbcs.user"));
        ds.setPassword(env.getProperty("jdbcs.password"));

        ds.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        ds.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
        ds.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
        ds.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

        return ds;


    }

    private Properties getHibernateProperties() {

        Properties props = new Properties();

        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

        return props;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {

        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();

        sf.setDataSource(dataSource());
        sf.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sf.setHibernateProperties(getHibernateProperties());

        return sf;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txm = new HibernateTransactionManager();
        txm.setSessionFactory(sessionFactory);

        return txm;

    }
}
