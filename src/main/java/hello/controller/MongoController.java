package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.model.Product;
import hello.repository.mongodb.ProductMongoRepository;

@RestController
public class MongoController {

    @Resource
    private ProductMongoRepository productMongoRepository;

    @RequestMapping(path = "/mongo", method = GET)
    public String mongo() throws IOException {
        StringBuilder sb = new StringBuilder();

        productMongoRepository.findAll().forEach(p -> {
            sb.append(p.toString()).append("<br/>");
        });

        return sb.toString();
    }

    @RequestMapping(path = "/mongo/createProducts", method = GET)
    public String createProducts(@RequestParam int count) {
        for (int i = 0; i < count; i++) {
            Product u = new Product();
            u.setName("Prod X " + i);
            u.setQty(i);
            productMongoRepository.insert(u);
        }
        return "DONE, inserted " + count + " products";
    }
}
