package com.tz.basis.config.database;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.tz")
@PropertySource({ "classpath:config/jdbc.properties" })
//@PropertySource({
//"file:/Users/dhong/Documents/workspace/sts-3.8.3.RELEASE/TzWeb/logs/config/jdbc.properties" })
@EnableTransactionManagement
public class DatabaseConfigProd {

}
