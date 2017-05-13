package org.hssh.common;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hssh on 2017/5/13.
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@ConfigurationProperties
@EnableConfigurationProperties
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class PrintSqlConfiguration
{
    private final SqlSessionFactory sqlSessionFactory;

    private static Logger logger = LoggerFactory.getLogger(PrintSqlConfiguration.class);

    private Map<String, String> printSql = new LinkedHashMap<String, String>();

    public PrintSqlConfiguration(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @PostConstruct
    public void addPrintSqlInterceptor()
    {
        Interceptor interceptor;
        if("all".equals(printSql.get("model")))
        {
            logger.info("print sql model: all");
            interceptor = new PrintSqlAllInterceptor();
        }
        else
        {
            logger.info("print sql model: update");
            interceptor = new PrintSqlUpdateInterceptor();
        }

        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
    }

    public Map<String, String> getPrintSql()
    {
        return printSql;
    }

    public void setPrintSql(Map<String, String> printSql)
    {
        this.printSql = printSql;
    }
}
