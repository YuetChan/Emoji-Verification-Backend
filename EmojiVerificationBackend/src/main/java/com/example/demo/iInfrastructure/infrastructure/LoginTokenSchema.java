package com.example.demo.iInfrastructure.infrastructure;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.demo.domain.entity.LoginToken;
import com.example.demo.iIfrastructure.infrastructure.exceptions.DbException;
import com.example.demo.iInfrastructure.ILoginTokenSchema;

public class LoginTokenSchema implements ILoginTokenSchema {
	
    private MongoOperations mongoOperations;
    
	public LoginTokenSchema(MongoOperations mongoOperations) {
		
		super();
		this.mongoOperations = mongoOperations;
		
	}
	
	@Override
	public LoginToken save(LoginToken tokenToBeSaved) {
		
		LoginToken savedToken = null;
		
		Query query = new Query();
		query.addCriteria(Criteria
				.where("UserId").is(tokenToBeSaved.getUserId()));
    	
		Update update = new Update();
		update.setOnInsert("UserId", tokenToBeSaved.getUserId());
		update.setOnInsert("TokenString", tokenToBeSaved.getTokenString());
		update.setOnInsert("ExpirationDate", tokenToBeSaved.getExpirationDate());
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.upsert(true);
		findAndModifyOptions.returnNew(true);
		
    	savedToken 
    	= mongoOperations.findAndModify(
    			query, update, findAndModifyOptions, LoginToken.class, "LoginToken");
    	
    	if(savedToken.getUserId() == tokenToBeSaved.getUserId())
    		return savedToken;
    	else 
    		throw new DbException("Existed token");
    	
	}
	
	@Override
	public LoginToken update(LoginToken tokenToBeUpdated) {
		
		LoginToken updatedToken = null;
		
		Query query = new Query();
		query.addCriteria(Criteria
				.where("UserId").is(tokenToBeUpdated.getUserId()));
    	
		Update update = new Update();
		update.set("TokenString", tokenToBeUpdated.getTokenString());
		update.set("ExpirationDate", tokenToBeUpdated.getExpirationDate());
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.upsert(true);
		findAndModifyOptions.returnNew(true);
		
    	updatedToken 
    	= mongoOperations.findAndModify(
    			query, update, findAndModifyOptions, LoginToken.class, "LoginToken");
    	
    	return updatedToken;
	}

	@Override
	public LoginToken findByUserId(int userId) {
		
		LoginToken foundToken = null;
		
		Query query = new Query();
  	  	query.addCriteria(Criteria.where("UserId").is(userId));
  	  	
  	  	foundToken 
  	  	= mongoOperations.findOne(
  			query, LoginToken.class, "LoginToken");
  	  	
  	  	return foundToken;
		
	}
	
	public LoginToken findByTokenString(String tokenString) {
		
		LoginToken foundToken = null;
		
		Query query = new Query();
  	  	query.addCriteria(Criteria.where("TokenString").is(tokenString));
  	  	
  	  	foundToken 
  	  	= mongoOperations.findOne(
  			query, LoginToken.class, "LoginToken");
  	  	
  	  	return foundToken;
		
	}
	
	
	@Override
	public void deleteAll() {
		
    	mongoOperations.remove(new Query(), LoginToken.class, "LoginToken");
    	
	}

}
