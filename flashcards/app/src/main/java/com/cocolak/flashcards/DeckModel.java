package com.cocolak.flashcards;

public class DeckModel {
    String deckName;
    String deckNumber;
    String deckNumberLeft;

    public DeckModel(String deckName, String deckNumber, String deckNumberLeft) {
        this.deckName = deckName;
        this.deckNumber = deckNumber;
        this.deckNumberLeft = deckNumberLeft;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getDeckNumber() {
        return deckNumber;
    }
    public String getDeckNumberLeft() {
        return deckNumberLeft;
    }
}
