package hello.application;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import hello.Config;

@SpringBootApplication(scanBasePackageClasses = Config.class)
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");
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
