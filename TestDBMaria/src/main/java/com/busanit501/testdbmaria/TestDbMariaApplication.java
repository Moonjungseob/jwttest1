package com.busanit501.testdbmaria;

import com.busanit501.testdbmaria.entity.User;
import com.busanit501.testdbmaria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestDbMariaApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TestDbMariaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 애플리케이션이 시작될 때 샘플 데이터를 데이터베이스에 삽입



        System.out.println("Sample users saved to the database");
    }
}
