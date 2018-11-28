package hello.api.git.impl;

import hello.api.git.Repo;

public class RepoImpl implements Repo {

    private String authToken;
    private String name;
    private String owner;

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public static Repo create(String authToken, String name, String owner) {
        RepoImpl object = new RepoImpl();
        object.authToken = authToken;
        object.name = name;
        object.owner = owner;
        return object;
    }
}
