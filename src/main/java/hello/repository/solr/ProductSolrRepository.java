package hello.repository.solr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import hello.model.SolrProduct;

public interface ProductSolrRepository extends SolrCrudRepository<SolrProduct, String> {

    @Query("id:*?0* OR name:*?0*")
    public Page<SolrProduct> findByIdOrName(String searchQuery, Pageable pageable);

    @Query("?0")
    public Page<SolrProduct> findByRawQuery(String searchQuery, Pageable pageable);

}
