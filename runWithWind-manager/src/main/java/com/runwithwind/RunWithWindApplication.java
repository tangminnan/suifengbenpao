package com.runwithwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author runwithwind
 */

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RunWithWindApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RunWithWindApplication.class, args);
        System.out.println("             \n" +
                "                 (_)            \n" +
                " _ __ _   _ _ __  _ _ __   __ _ \n" +
                "| '__| | | | '_ \\| | '_ \\ / _`\n" +
                "| |  | |_| | | | | | | | | (_| |  \n" +
                "|_|   \\__,_|_| |_|_|_| |_|\\__, \n" +
                "                           __/ | \n" +
                "                          |___/                       ") ;
    }
}