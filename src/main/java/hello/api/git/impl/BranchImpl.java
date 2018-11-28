package hello.api.git.impl;

import java.util.Map;

import hello.api.git.Branch;

public class BranchImpl implements Branch {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Branch create(Map<String, ?> map) {
        BranchImpl branch = new BranchImpl();
        branch.name = (String) map.get("name");
        return branch;
    }
}
