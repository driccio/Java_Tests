package org.example;

public class NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;
    private final NotificationService notificationService;

    public NewsLetterService(
            final NewsLetterRepository newsLetterRepository,
            final NotificationService notificationService) {
        this.newsLetterRepository = newsLetterRepository;
        this.notificationService = notificationService;
    }

    public NewsLetter markAs(NewsLetter newsLetter, NewsLetterStatus status) {

        switch (status) {
            case DRAFT -> {
                throw new IllegalArgumentException("DRAFT can't be used");
            }
            case VALIDATED -> {
                if (newsLetter.getStatus() != NewsLetterStatus.DRAFT) {
                    throw new IllegalStateException("Only DRAFT can move to VALIDATED");
                }

                newsLetter.setStatus(status);
            }
            case PUBLISHED -> {
                if (newsLetter.getStatus() != NewsLetterStatus.VALIDATED) {
                    throw new IllegalStateException("Only VALIDATED can move to PUBLISHED");
                }

                newsLetter.setStatus(status);
                notificationService.notify(newsLetter, status);
            }
            case ARCHIVED -> {
                newsLetter.setStatus(status);
            }
        }

        this.newsLetterRepository.save(newsLetter);

        return newsLetter;
    }
}
