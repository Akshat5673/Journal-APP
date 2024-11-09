package com.nineleaps.journalApp.Dao;

import com.nineleaps.journalApp.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryDao {
    
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> getUsersForSentimentAnalysis(){
        Query query = new Query();
//        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne(""));
        query.addCriteria(Criteria.where("isSentimentAnalysisOpted").is(true));

        // we can also check the above conditions by checking if the email field matches the regular expression of a valid email
         query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$"));

        // if we want to use or operator
//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.orOperator(c
//                Criteria.where("email").exists(true)
//                ,Criteria.where("isSentimentAnalysisOpted").is(true)
//                )
//        );

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }


}
