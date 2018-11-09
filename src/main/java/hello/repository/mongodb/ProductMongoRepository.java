package hello.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import hello.model.Product;

public interface ProductMongoRepository extends MongoRepository<Product, String> {

}
