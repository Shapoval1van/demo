package com.ncube.demo.repository;

import com.ncube.demo.repository.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
}
