package hello.application;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import hello.Config;
import hello.model.Product;

@SpringBootApplication(scanBasePackageClasses = Config.class)
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);

        if (hasArg(args, "-createproducts")) {
            for (int i = 0; i < 1000; i++) {
                Product u = new Product();
                u.setName("Prod X " + i);
                u.setQty(i);
                mongoTemplate.insert(u);
            }
        }
        if (hasArg(args, "-printbeans")) {
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        }
    }

    private static boolean hasArg(String[] args, String arg) {
        return Stream.of(args).anyMatch(arg::equalsIgnoreCase);
    }

}
