package hello.api.git;

import java.util.List;

public interface ProjectGitApi {
    List<Branch> getRepoBranches(Repo repo, String branchPrefix);

    List<Commit> getBranchCommits(Repo repo, Branch branch, int count);
}
