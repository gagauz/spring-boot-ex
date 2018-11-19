package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.model.User;

@RestController
public class HelloController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(path = "/", method = GET)
    public String index() {
        StringBuilder sb = new StringBuilder("Greetings from Spring Boot!\n{\n");
        //        userRepository.findAll(PageRequest.of(0, 10)).forEach(u -> sb.append(u + ",\n"));

        AggregationOperation match = Aggregation.match(Criteria.where("name").regex("/.*1/"));
        Aggregation aggregation = Aggregation.newAggregation(match);

        mongoTemplate.aggregate(aggregation, User.class, User.class).forEach(u -> sb.append(u + ",\n"));

        sb.append("}");
        return sb.toString();
    }
}
