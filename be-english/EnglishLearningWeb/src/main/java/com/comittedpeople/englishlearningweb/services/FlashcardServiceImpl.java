package com.comittedpeople.englishlearningweb.services;

import com.comittedpeople.englishlearningweb.domain.DocVocabContent;
import com.comittedpeople.englishlearningweb.repositories.DocVocabContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardServiceImpl.class);
    private static final int MASTERY_THRESHOLD = 3;

    @Autowired
    private DocVocabContentRepository vocabContentRepository;

    private final Map<String, FlashcardDeck> deckStore = new ConcurrentHashMap<>();
    private final Map<Long, List<String>> userDecks = new ConcurrentHashMap<>();

    @Override
    public FlashcardDeck createDeckFromLesson(Long userId, Long lessonId) {
        logger.info("Creating flashcard deck for user {} from lesson {}", userId, lessonId);

        List<DocVocabContent> vocabItems = vocabContentRepository.findAll().stream()
                .filter(v -> v.getLesson() != null && v.getLesson().getId().equals(lessonId))
                .collect(Collectors.toList());

        if (vocabItems.isEmpty()) {
            logger.warn("No vocabulary content found for lesson {}", lessonId);
            return null;
        }

        FlashcardDeck deck = new FlashcardDeck();
        deck.setDeckId(UUID.randomUUID().toString().substring(0, 10));
        deck.setUserId(userId);
        deck.setLessonId(lessonId);
        deck.setDeckName("Vocab Lesson " + lessonId);

        List<Flashcard> cards = new ArrayList<>();
        for (int i = 0; i < vocabItems.size(); i++) {
            DocVocabContent vocab = vocabItems.get(i);
            Flashcard card = new Flashcard();
            card.setCardIndex(i);
            card.setFront(vocab.getContent() != null ? vocab.getContent() : "");
            card.setBack(vocab.getDescription() != null ? vocab.getDescription() : "");
            card.setPronunciation(vocab.getSpelling() != null ? vocab.getSpelling() : "");
            card.setTimesCorrect(0);
            card.setTimesIncorrect(0);
            card.setMastered(false);
            cards.add(card);
        }

        deck.setCards(cards);
        deck.setTotalCards(cards.size());
        deck.setMasteredCards(0);

        deckStore.put(deck.getDeckId(), deck);
        userDecks.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(deck.getDeckId());

        logger.info("Flashcard deck {} created with {} cards", deck.getDeckId(), cards.size());
        return deck;
    }

    @Override
    public Flashcard getNextCard(String deckId) {
        FlashcardDeck deck = deckStore.get(deckId);
        if (deck == null) {
            logger.warn("Deck {} not found", deckId);
            return null;
        }

        // Prioritize cards that are not mastered, then by least times correct
        Optional<Flashcard> nextCard = deck.getCards().stream()
                .filter(c -> !c.isMastered())
                .min(Comparator.comparingInt(Flashcard::getTimesCorrect)
                        .thenComparing(Comparator.comparingInt(Flashcard::getTimesIncorrect).reversed()));

        if (nextCard.isPresent()) {
            logger.debug("Next card for deck {}: index {}", deckId, nextCard.get().getCardIndex());
            return nextCard.get();
        }

        logger.info("All cards mastered in deck {}", deckId);
        return null;
    }

    @Override
    public void recordCardResponse(String deckId, int cardIndex, boolean correct) {
        FlashcardDeck deck = deckStore.get(deckId);
        if (deck == null || cardIndex < 0 || cardIndex >= deck.getCards().size()) {
            logger.warn("Invalid deck {} or card index {}", deckId, cardIndex);
            return;
        }

        Flashcard card = deck.getCards().get(cardIndex);
        if (correct) {
            card.setTimesCorrect(card.getTimesCorrect() + 1);
            if (card.getTimesCorrect() >= MASTERY_THRESHOLD && !card.isMastered()) {
                card.setMastered(true);
                deck.setMasteredCards(deck.getMasteredCards() + 1);
                logger.info("Card {} in deck {} is now mastered!", cardIndex, deckId);
            }
        } else {
            card.setTimesIncorrect(card.getTimesIncorrect() + 1);
            // Reset mastery progress on incorrect answer
            if (card.isMastered()) {
                card.setMastered(false);
                card.setTimesCorrect(0);
                deck.setMasteredCards(Math.max(0, deck.getMasteredCards() - 1));
            }
        }

        logger.debug("Card {} response recorded: correct={}, timesCorrect={}, mastered={}",
                cardIndex, correct, card.getTimesCorrect(), card.isMastered());
    }

    @Override
    public FlashcardDeck getDeckProgress(String deckId) {
        return deckStore.get(deckId);
    }

    @Override
    public List<FlashcardDeck> getUserDecks(Long userId) {
        List<String> deckIds = userDecks.getOrDefault(userId, Collections.emptyList());
        return deckIds.stream()
                .map(deckStore::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void resetDeck(String deckId) {
        FlashcardDeck deck = deckStore.get(deckId);
        if (deck == null) {
            return;
        }

        for (Flashcard card : deck.getCards()) {
            card.setTimesCorrect(0);
            card.setTimesIncorrect(0);
            card.setMastered(false);
        }
        deck.setMasteredCards(0);

        logger.info("Deck {} has been reset", deckId);
    }
}
