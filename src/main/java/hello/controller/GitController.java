package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.api.git.Branch;
import hello.api.git.ProjectGitApi;
import hello.api.git.Repo;
import hello.api.git.impl.RepoImpl;

@RestController
public class GitController {

    @Autowired
    private ProjectGitApi projectGitApi;

    @RequestMapping(path = "/git", method = POST)
    @ResponseBody
    public Object git(@RequestParam String query, @RequestParam String auth) {
        Repo repo = RepoImpl.create(auth, "tracker", "gagauz");
        List<Branch> branches = projectGitApi.getRepoBranches(repo, null);
        for (Branch b : branches) {
            return projectGitApi.getBranchCommits(repo, b, 10);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(IntStream.range(0, 10).filter(i -> i % 3 == 0).sum());
        System.out.println("ATCGGCAGCT");
        System.out.println(dnaComplement("ATCGGCAGCT"));
        System.out.println(fizzBuzz(15));
        System.out.println(decodeMorse("             .   .           "));
        System.out.println(ConvertBinaryArrayToInt(Arrays.asList(1, 1, 1, 1)));
    }

    static String pairs0 = "ATCG";
    static String pairs1 = "TAGC";

    public static String dnaComplement(String dna) {
        if (null == dna) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        try {
            for (; i < dna.length(); i++) {
                sb.append(pairs1.charAt(pairs0.indexOf(dna.charAt(i))));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("Invalid sequence character '" + dna.charAt(i) + "' at index " + i);
        }
        return sb.toString();
    }

    public static String fizzBuzz(Integer n) {
        IntFunction<String> mapper3 = i -> {
            String r = "";
            if (i % 3 == 0) {
                r += "Word1";
            }
            if (i % 5 == 0) {
                r += "Word2";
                return r;
            }
            return String.valueOf(i);
        };
        return IntStream.range(1, n + 1).mapToObj(mapper3).collect(Collectors.joining("\n"));
    }

    private static final String SPACE = " ";
    private static final String EMPTY = "";

    public static String decodeMorse(String morseCode) {
        Scanner scanner = new Scanner(new StringReader(morseCode.trim()));
        scanner.useDelimiter(SPACE);

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            String code = scanner.next();
            if (EMPTY.equals(code)) {
                code = SPACE;
                scanner.skip(" ");
            } else {
                code = code;
            }
            sb.append("[" + code + "]");
        }
        scanner.close();
        return sb.toString();
    }

    public static int ConvertBinaryArrayToInt(List<Integer> binary) {
        int result = 0;
        for (int i : binary) {
            result <<= 1;
            result = result | i;
        }
        return result;
    }
}
