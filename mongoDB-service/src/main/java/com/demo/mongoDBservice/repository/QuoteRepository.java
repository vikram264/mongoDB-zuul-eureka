package com.demo.mongoDBservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.demo.mongoDBservice.bean.Quote;

public interface QuoteRepository extends MongoRepository<Quote, Integer>
{
    List<Quote> findByName(String name);
    
    
}   
