package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.model.Product;
import hello.repository.elasticsearch.ProductRepository;
import hello.repository.mongodb.ProductMongoRepository;

@RestController
public class EsController {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private ProductMongoRepository productMongoRepository;

    @RequestMapping(path = "/es", method = GET)
    public String es() throws IOException {
        StringBuilder sb = new StringBuilder("Greetings from Spring Boot!\n{\n");

        productRepository.findAll().forEach(p -> {
            sb.append(p.toString()).append(",\n");
        });

        sb.append("}");
        return sb.toString();
    }

    @RequestMapping(path = "/es/index", method = GET)
    public String esIndex() {

        int start = 0;
        final int page = 100;
        Page<Product> result;
        do {
            result = productMongoRepository.findAll(PageRequest.of(start, page));
            if (result.isEmpty()) {
                break;
            }
            start += page;

            result.getContent().forEach(p -> productRepository.save(p));
        } while (result.getSize() >= page);

        return "DONE";
    }
}
