package hello.api.git;

public interface Commit {
    String getId();

    String getMessage();

    Author getAuthor();
}
