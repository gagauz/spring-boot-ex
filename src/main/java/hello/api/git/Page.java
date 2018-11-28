package hello.api.git;

public interface Page {
    String getStartCursor();

    String getEndCursor();

    boolean isHasNext();

    int getTotalCount();
}
