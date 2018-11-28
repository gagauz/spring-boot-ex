package hello;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@ComponentScan(basePackages = "hello.api.git.impl")
@EnableMongoRepositories(basePackages = { "hello.repository.mongodb" })
@EnableElasticsearchRepositories(basePackages = { "hello.repository.elasticsearch" })
@EnableSolrRepositories(basePackages = { "hello.repository.solr" })
public class Config {
    //    @Bean
    //    public MongoClient mongoClient() {
    //        return new MongoClient("localhost", 27017);
    //    }
    //
    //    @Bean
    //    public MongoTemplate mongoTemplate() {
    //        return new MongoTemplate(mongoClient(), "user");
    //    }
    //
    //    @Bean
    //    public ElasticsearchTemplate elasticsearchTemplate(@Autowired NodeClient nodeClient) {
    //        return new ElasticsearchTemplate(nodeClient);
    //    }
}
