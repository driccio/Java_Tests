package org.example;

public class NewsLetter {
    private String title;
    private String text;
    private NewsLetterStatus status = NewsLetterStatus.DRAFT;

    public NewsLetter(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public NewsLetter setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public NewsLetter setText(String text) {
        this.text = text;
        return this;
    }

    public NewsLetterStatus getStatus() {
        return status;
    }

    public NewsLetter setStatus(NewsLetterStatus status) {
        this.status = status;
        return this;
    }
}
