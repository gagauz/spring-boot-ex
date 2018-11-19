package hello.repository.solr;

import org.springframework.data.solr.repository.SolrCrudRepository;

import hello.model.SolrProduct;

public interface ProductSolrRepository extends SolrCrudRepository<SolrProduct, String> {

}
