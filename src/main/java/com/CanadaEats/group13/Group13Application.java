package com.CanadaEats.group13;

import com.CanadaEats.group13.database.TestConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Group13Application {
    public static void main(String[] args) {

        SpringApplication.run(Group13Application.class, args);
        TestConnection connection = new TestConnection();
        connection.runSampleQueries();
    }
}
