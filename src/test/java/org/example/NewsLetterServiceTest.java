package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

class NewsLetterServiceTest {
    @Test
    public void test() {
        // Given
        NewsLetterService newsLetterService = new NewsLetterService(null, null);
        NewsLetter newsLetter = new NewsLetter("Titre", "mon texte");

        // When
        Executable action = () -> {
            newsLetterService.markAs(newsLetter, NewsLetterStatus.DRAFT);
        };

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = "DRAFT")
    public void testValidationFailed(NewsLetterStatus startState) {
        // Given
        NewsLetterService newsLetterService = new NewsLetterService(null, null);
        NewsLetter newsLetter = new NewsLetter("Titre", "mon texte");
        newsLetter.setStatus(startState);

        // When
        Executable action = () -> {
            newsLetterService.markAs(newsLetter, NewsLetterStatus.VALIDATED);
        };

        // Then
        Assertions.assertThrows(IllegalStateException.class, action);
    }

    @Test
    public void testVSuccess() {
        // Given
        NewsLetterRepository repository = Mockito.mock(NewsLetterRepository.class);
        NewsLetterService newsLetterService = new NewsLetterService(
                repository, null);
        NewsLetter newsLetter = new NewsLetter("Titre", "mon texte");

        // When
        NewsLetter updated = newsLetterService.markAs(newsLetter, NewsLetterStatus.VALIDATED);

        // Then
        Assertions.assertEquals(updated.getStatus(), NewsLetterStatus.VALIDATED);
        Mockito.verify(repository, Mockito.times(1)).save(newsLetter);
    }

    @Test
    public void testPSuccess() {
        // Given
        NewsLetterRepository repository = Mockito.mock(NewsLetterRepository.class);
        NotificationService notificationService = Mockito.mock(NotificationService.class);
        NewsLetterService newsLetterService = new NewsLetterService(
                repository, notificationService);
        NewsLetter newsLetter = new NewsLetter("Titre", "mon texte");
        newsLetter.setStatus(NewsLetterStatus.VALIDATED);

        // When
        NewsLetter updated = newsLetterService.markAs(newsLetter, NewsLetterStatus.PUBLISHED);

        // Then
        Assertions.assertEquals(updated.getStatus(), NewsLetterStatus.PUBLISHED);
        Mockito.verify(repository, Mockito.times(1)).save(newsLetter);
        Mockito.validateMockitoUsage();
    }
}