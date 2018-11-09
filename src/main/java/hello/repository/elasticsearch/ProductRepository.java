package hello.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import hello.model.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {

}
