package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xl0e.json.mapper.JsonMapper;
import com.xl0e.json.mapper.JsonMapperImpl;
import com.xl0e.json.writer.JsonIndentWriter;

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
    public String solr(@RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int count) throws IOException {

        testCollection();

        StringBuilder sb = new StringBuilder("{\n");

        Iterable<SolrProduct> result;

        if (null == query) {
            result = productSolrRepository.findAll(PageRequest.of(from, count));
        } else {
            result = productSolrRepository.findByRawQuery(query, PageRequest.of(from, count));
        }

        result.forEach(p -> {
            sb.append(p.toString()).append(",\n");
        });

        sb.append("}");
        return sb.toString();
    }

    @RequestMapping(path = "/solr/query", method = { GET, POST })
    public String solrQuery(@RequestParam String query, @RequestParam String facet, @RequestParam String intervals) throws Exception {
        SolrQuery solrQuery = new SolrQuery()
                .setQuery(query)
                .addIntervalFacets(facet, intervals.split(";"));
        QueryResponse response = solrClient.query("product", solrQuery);
        JsonMapper mapper = JsonMapperImpl.instanse(getClass().getResourceAsStream("/jsonmmaper/QueryResponse.json"));
        StringWriter sw = new StringWriter();
        mapper.map(response, new JsonIndentWriter(sw));
        return sw.toString();
    }

    @RequestMapping(path = "/solr/index", method = GET)
    public String solrIndex() {

        int start = 0;
        final int page = 100;
        Page<Product> result;

        Pageable pageRequest = PageRequest.of(start, page);

        do {
            result = productMongoRepository.findAll(pageRequest);
            if (result.isEmpty()) {
                break;
            }
            pageRequest = pageRequest.next();

            List<SolrProduct> solrDocuments = result.getContent().stream().map(this::createSolrProduct).collect(Collectors.toList());
            productSolrRepository.saveAll(solrDocuments);
        } while (true);

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
