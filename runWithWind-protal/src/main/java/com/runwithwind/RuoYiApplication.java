package com.runwithwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author runwithwind
 */

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling    //开启调度任务
public class RuoYiApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("             \n" +
                "                 (_)            \n" +
                " _ __ _   _ _ __  _ _ __   __ _ \n" +
                "| '__| | | | '_ \\| | '_ \\ / _`\n" +
                "| |  | |_| | | | | | | | | (_| |  \n" +
                "|_|   \\__,_|_| |_|_|_| |_|\\__, \n" +
                "                           __/ | \n" +
                "                          |___/                       ") ;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // TODO Auto-generated method stub
        builder.sources(this.getClass());
        return super.configure(builder);
    }
}