package com.comittedpeople.englishlearningweb.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlashcardService {

    /**
     * Create a flashcard deck from a vocabulary lesson.
     *
     * @param userId   the user ID
     * @param lessonId the vocabulary lesson ID
     * @return the created flashcard deck
     */
    FlashcardDeck createDeckFromLesson(Long userId, Long lessonId);

    /**
     * Get the next card to study using spaced repetition logic.
     * Cards that have been answered incorrectly or not recently reviewed are prioritized.
     *
     * @param deckId the flashcard deck ID
     * @return the next flashcard to study, or null if all cards are reviewed
     */
    Flashcard getNextCard(String deckId);

    /**
     * Record the user's response to a flashcard.
     *
     * @param deckId the deck ID
     * @param cardIndex the card index
     * @param correct whether the user answered correctly
     */
    void recordCardResponse(String deckId, int cardIndex, boolean correct);

    /**
     * Get the study progress for a deck.
     *
     * @param deckId the deck ID
     * @return deck with progress information
     */
    FlashcardDeck getDeckProgress(String deckId);

    /**
     * Get all flashcard decks for a user.
     *
     * @param userId the user ID
     * @return list of the user's flashcard decks
     */
    List<FlashcardDeck> getUserDecks(Long userId);

    /**
     * Reset study progress for a deck.
     *
     * @param deckId the deck ID
     */
    void resetDeck(String deckId);

    /**
     * Represents a flashcard deck.
     */
    class FlashcardDeck {
        private String deckId;
        private Long userId;
        private Long lessonId;
        private String deckName;
        private List<Flashcard> cards;
        private int totalCards;
        private int masteredCards;

        public FlashcardDeck() {}

        public String getDeckId() { return deckId; }
        public void setDeckId(String deckId) { this.deckId = deckId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getLessonId() { return lessonId; }
        public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
        public String getDeckName() { return deckName; }
        public void setDeckName(String deckName) { this.deckName = deckName; }
        public List<Flashcard> getCards() { return cards; }
        public void setCards(List<Flashcard> cards) { this.cards = cards; }
        public int getTotalCards() { return totalCards; }
        public void setTotalCards(int totalCards) { this.totalCards = totalCards; }
        public int getMasteredCards() { return masteredCards; }
        public void setMasteredCards(int masteredCards) { this.masteredCards = masteredCards; }
    }

    /**
     * Represents a single flashcard.
     */
    class Flashcard {
        private int cardIndex;
        private String front;
        private String back;
        private String pronunciation;
        private int timesCorrect;
        private int timesIncorrect;
        private boolean mastered;

        public Flashcard() {}

        public int getCardIndex() { return cardIndex; }
        public void setCardIndex(int cardIndex) { this.cardIndex = cardIndex; }
        public String getFront() { return front; }
        public void setFront(String front) { this.front = front; }
        public String getBack() { return back; }
        public void setBack(String back) { this.back = back; }
        public String getPronunciation() { return pronunciation; }
        public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }
        public int getTimesCorrect() { return timesCorrect; }
        public void setTimesCorrect(int timesCorrect) { this.timesCorrect = timesCorrect; }
        public int getTimesIncorrect() { return timesIncorrect; }
        public void setTimesIncorrect(int timesIncorrect) { this.timesIncorrect = timesIncorrect; }
        public boolean isMastered() { return mastered; }
        public void setMastered(boolean mastered) { this.mastered = mastered; }
    }
}
