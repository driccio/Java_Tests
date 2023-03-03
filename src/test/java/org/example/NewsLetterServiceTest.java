package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import static org.example.NewsLetterStatus.*;

class NewsLetterServiceTest {
    NewsLetterRepository repository = Mockito.mock(NewsLetterRepository.class);
    NotificationService notificationService = Mockito.mock(NotificationService.class);
    NewsLetterService newsLetterService = new NewsLetterService(repository, notificationService);

    @ParameterizedTest
    @EnumSource
    public void transition_to_draft_should_fail(NewsLetterStatus status) {
        NewsLetter newsLetter = buildNewsLetter(status);

        Executable action = () -> newsLetterService.markAs(newsLetter, DRAFT);

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = "DRAFT")
    public void transition_to_validated_should_fail(NewsLetterStatus status) {
        // Given
        NewsLetter newsLetter = buildNewsLetter(status);

        checkTransitionFailed(newsLetter, VALIDATED);
    }

    @Test
    public void transition_to_validated_should_succeed() {
        // Given
        NewsLetter newsLetter = buildNewsLetter(DRAFT);

        // When
        NewsLetter updated = newsLetterService.markAs(newsLetter, VALIDATED);

        // Then
        Assertions.assertEquals(updated.getStatus(), VALIDATED);
        Mockito.verify(repository, Mockito.times(1)).save(newsLetter);

        Mockito.verifyNoMoreInteractions(repository, notificationService);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = "VALIDATED")
    public void transition_to_published_should_fail(NewsLetterStatus status) {
        // Given
        NewsLetter newsLetter = buildNewsLetter(status);

        checkTransitionFailed(newsLetter, PUBLISHED);
    }

    @Test
    public void transition_to_published_should_succeed() {
        // Given
        NewsLetter newsLetter = buildNewsLetter(VALIDATED);

        // When
        NewsLetter updated = newsLetterService.markAs(newsLetter, PUBLISHED);

        // Then
        Assertions.assertEquals(updated.getStatus(), PUBLISHED);
        Mockito.verify(repository, Mockito.times(1)).save(newsLetter);
        Mockito.verify(notificationService, Mockito.times(1)).notify(newsLetter, PUBLISHED);

        Mockito.verifyNoMoreInteractions(repository, notificationService);
    }

    private NewsLetter buildNewsLetter(NewsLetterStatus status) {
        return new NewsLetter("Titre", "mon texte").setStatus(status);
    }

    private void checkTransitionFailed(NewsLetter newsLetter, NewsLetterStatus status) {
        Executable action = () -> newsLetterService.markAs(newsLetter, status);

        Assertions.assertThrows(IllegalStateException.class, action);
    }
}