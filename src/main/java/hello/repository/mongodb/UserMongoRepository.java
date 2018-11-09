package hello.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import hello.model.User;

public interface UserMongoRepository extends MongoRepository<User, Long> {

}
