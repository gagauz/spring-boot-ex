package hello.api.git.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import hello.api.git.Author;

public class AuthorImpl implements Author {

    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXXXX");

    private String name;
    private String email;
    private Date date;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static Author create(Map<String, ?> map) {
        AuthorImpl author = new AuthorImpl();
        author.name = (String) map.get("name");
        author.email = (String) map.get("email");
        author.date = Date.from(ZonedDateTime.parse((String) map.get("date"), fmt).toInstant());

        return author;
    }
}
