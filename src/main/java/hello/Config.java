package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = { "hello.repository.mongodb" })
@EnableElasticsearchRepositories(basePackages = { "hello.repository.elasticsearch" })
public class Config {
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("localhost", 27017);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "user");
    }
}
