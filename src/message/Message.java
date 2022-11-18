package message;

import java.io.Serializable;

public class Message implements Serializable {

    private final String author;
    private final String content;

    public Message(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
