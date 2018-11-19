package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.model.Product;
import hello.model.SolrProduct;
import hello.repository.mongodb.ProductMongoRepository;
import hello.repository.solr.ProductSolrRepository;

@RestController
public class SolrController {

    @Resource
    private ProductSolrRepository productSolrRepository;

    @Resource
    private ProductMongoRepository productMongoRepository;

    @Resource
    private SolrClient solrClient;

    @RequestMapping(path = "/solr", method = GET)
    public String solr() throws IOException {

        testCollection();

        StringBuilder sb = new StringBuilder("{\n");

        productSolrRepository.findAll().forEach(p -> {
            sb.append(p.toString()).append(",\n");
        });

        sb.append("}");
        return sb.toString();
    }

    @RequestMapping(path = "/solr/index", method = GET)
    public String solrIndex() {

        int start = 0;
        final int page = 100;
        Page<Product> result;

        do {
            result = productMongoRepository.findAll(PageRequest.of(start, page));
            if (result.isEmpty()) {
                break;
            }
            start += page;

            result.getContent().stream().map(this::createSolrProduct).forEach(p -> productSolrRepository.save(p));
        } while (result.getSize() >= page);

        return "DONE";
    }

    public SolrProduct createSolrProduct(Product product) {
        SolrProduct result = new SolrProduct();
        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setQty(product.getQty());
        return result;
    }

    private void testCollection() {
        try {
            CoreAdminResponse aResponse = CoreAdminRequest.getStatus("product", solrClient);
            if (aResponse.getCoreStatus("product").size() < 1) {
                CoreAdminRequest.Create aCreateRequest = new CoreAdminRequest.Create();
                aCreateRequest.setCoreName("product");
                aCreateRequest.setInstanceDir("product");
                aCreateRequest.process(solrClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
