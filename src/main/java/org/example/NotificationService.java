package org.example;

public class NotificationService {
    public void notify(NewsLetter newsLetter, NewsLetterStatus status) {
        newsLetter.setStatus(null);
    }
}
