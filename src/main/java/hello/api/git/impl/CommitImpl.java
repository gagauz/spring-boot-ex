package hello.api.git.impl;

import java.util.Map;

import hello.api.git.Author;
import hello.api.git.Commit;

public class CommitImpl implements Commit {

    private String id;
    private String message;
    private Author author;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public static Commit create(Map<String, ?> map) {
        CommitImpl commit = new CommitImpl();
        commit.id = (String) map.get("oid");
        commit.message = (String) map.get("message");
        commit.author = AuthorImpl.create((Map<String, ?>) map.get("author"));

        return commit;
    }
}
