package com.cocolak.flashcards;

public class DeckModel {
    String deckName;
    String deckNumber;

    public DeckModel(String deckName, String deckNumber) {
        this.deckName = deckName;
        this.deckNumber = deckNumber;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getDeckNumber() {
        return deckNumber;
    }
}
