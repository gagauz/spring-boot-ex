package hello.api.git.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hello.api.git.Branch;
import hello.api.git.Commit;
import hello.api.git.JsonUtils;
import hello.api.git.ProjectGitApi;
import hello.api.git.Repo;

@Service
public class ProjectGitApiImpl implements ProjectGitApi {

    @Override
    public List<Branch> getRepoBranches(Repo repo, String branchPrefix) {

        String refPrefix = "refs/heads/" + (null == branchPrefix ? "" : branchPrefix);

        String query = "query { repository(owner:'" + repo.getOwner() + "', name:'" + repo.getName()
                + "') { refs(first: 10, , refPrefix:'" + refPrefix + "') { nodes { name } } } }";

        Map<String, ?> response = postForObject(repo, HashMap.class, query);

        return JsonUtils.parseArray(response, "data/repository/refs/nodes")
                .map(List::stream)
                .map(s -> s.map(BranchImpl::create).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }

    @Override
    public List<Commit> getBranchCommits(Repo repo, Branch branch, int count) {

        String refPrefix = "refs/heads/" + branch.getName();

        String query = "query { repository(owner:'" + repo.getOwner() + "', name:'" + repo.getName()
                + "') { ref(qualifiedName:'" + refPrefix
                + "') { target { ... on Commit { id history(first: " + count
                + ") { totalCount pageInfo { startCursor endCursor hasNextPage } edges { node { oid message author { name email date } } } } } } } } }";
        //        String query = "query { node(id: 'MDEwOlJlcG9zaXRvcnk4NDM5MTQ3') { ... on Repository { ref(qualifiedName: 'master) { target { ... on Commit { id history(first: 30) { totalCount pageInfo { hasNextPage } edges { node { oid message author { name email date } } } } } } } } } }";

        Map<String, ?> response = postForObject(repo, HashMap.class, query);

        PageImpl page = JsonUtils.parseObject(response, "data/repository/ref/target/history/pageInfo").map(PageImpl::create).orElse(null);
        page.setTotalCount(JsonUtils.parseInt(response, "data/repository/ref/target/history/totalCount").orElse(0));

        return JsonUtils.parseArray(response, "data/repository/ref/target/history/edges")
                .map(List::stream)
                .map(s -> s.map(m -> (Map<String, ?>) m.get("node")).map(CommitImpl::create).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }

    protected <T> T postForObject(Repo repo, Class<T> respClass, String query) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "token " + repo.getAuthToken());

        String body = "{\"query\": \"" + query.replaceAll("'", "\\\\\"") + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject("https://api.github.com/graphql", entity, respClass);
    }

    public static void main(String[] args) {
        System.out.println("asadfds'asdfasdfasd".replaceAll("'", "\\\\\""));
    }

}
