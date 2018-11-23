package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GitController {

    @RequestMapping(path = "/git", method = GET)
    @ResponseBody
    public Object git(@RequestParam String query) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "token 2173aa7d325ca4c9b6a1337838abd64c30ccaacd");

        HttpEntity<String> entity = new HttpEntity<>("{\"query\": \"" + query.replaceAll("\\\\n", "\n") + "\"}", headers);

        String object = restTemplate.postForObject("https://api.github.com/graphql", entity, String.class);
        return object;
    }
}
