package com.baomidou.mybatisplus.no.spring.mapper;


import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public abstract class MapperBaseTest<M> {

    protected M initMapper() {
        SqlSession session = initSqlSessionFactory().openSession(true);
        return session.getMapper((Class<M>) (((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]));
    }

    private SqlSessionFactory initSqlSessionFactory() {
        DataSource dataSource = dataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("UnitTest", transactionFactory, dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration(environment);
        configuration.addMapper((Class<M>) (((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]));
        configuration.setLogImpl(StdOutImpl.class);
        return new MybatisSqlSessionFactoryBuilder().build(configuration);
    }

    private DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:mem:test");
        dataSource.setUsername("root");
        dataSource.setPassword("test");
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("runscript from '"
                    + getClass().getResource(getSqlFile()).toURI().toString().substring(6) + "'");
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    protected abstract String getSqlFile();


}
