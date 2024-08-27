package com.busanit501.testdbmaria.repository;

import com.busanit501.testdbmaria.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends MongoRepository<User, String> {
    // 사용자 정의 쿼리 메서드를 추가할 수 있습니다.
}