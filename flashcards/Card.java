package flashcards;

import java.util.*;

public class Card implements Comparable<Card> {
    private final String cardName;
    private final String definition;
    private final static Scanner sc = new Scanner(System.in);
    private final static Set<Card> cards = new HashSet<>();
    private final static Map<Card, Integer> cardCounterHard = new TreeMap<>();
    public static CardUtility cardUtility = new CardUtility(cards, cardCounterHard);

    public Card(String card, String definition) {
        this.cardName = card;
        this.definition = definition;
    }

    private static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );
        return sortedEntries;
    }

    static void resetStats() {
        cardCounterHard.clear();
        System.out.println("Card statistics has been reset.");
    }

    static void getHardestCard() {
        if (!cardCounterHard.isEmpty()) {
            List<Map.Entry<Card, Integer>> cardCounterHardSorted = entriesSortedByValues(cardCounterHard);
            Map.Entry<Card, Integer> first = cardCounterHardSorted.get(0); //first is card with biggest number of errors
            List<String> hardest = new ArrayList<>();
            for (Map.Entry<Card, Integer> entry : cardCounterHardSorted) {
                if (entry.getValue().equals(first.getValue())) {
                    hardest.add(entry.getKey().getCardName());
                }
            }
            if (hardest.size() > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hardest.size(); i++) {
                    sb.append("\"").append(hardest.get(i)).append("\"");
                    if (i != hardest.size() -1) {
                        sb.append(", ");
                    }
                }
                if (first.getValue() == 1) {
                    System.out.printf("The hardest cards are %s. You have %d error answering them.\n", sb.toString(), first.getValue());
                } else {
                    System.out.printf("The hardest cards are %s. You have %d errors answering them.\n", sb.toString(), first.getValue());
                }
            } else {
                if (first.getValue() == 1) {
                    System.out.printf("The hardest card is %s. You have %d error answering it.\n", first.getKey().getCardName(), first.getValue());
                } else {
                    System.out.printf("The hardest card is \"%s\". You have %d errors answering it.\n", first.getKey().getCardName(), first.getValue());
                }
            }

        } else {
            System.out.println("There are no cards with errors.");
        }
    }

    static void removeCard() {
        System.out.println("The card:");
        String cardName = sc.nextLine();
        if (!isCardByNameExists(cardName)) {
            System.out.printf("Can't remove \"%s\": there is no such card.\n", cardName);
        } else {
            try {
                cards.remove(getCardByNameFromCards(cardName));
                cardCounterHard.remove(getCardByNameFromHardest(cardName));
                System.out.print("The card has been removed.\n");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private static Card getCardByNameFromCards(String cardName) {
        for (Card card : cards) {
            if (card.getCardName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    private static Card getCardByNameFromHardest(String cardName) {
        for (Map.Entry<Card, Integer> entry : cardCounterHard.entrySet()) {
            if (entry.getKey().getCardName().equals(cardName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    static void addCard() {
        System.out.print("The card:\n");
        String cardName = sc.nextLine();
        if (Card.isCardByNameExists(cardName)) {
            System.out.printf("The card \"%s\" already exists.\n", cardName);
            return;
        }

        System.out.print("The definition of the card:\n");
        String definition = sc.nextLine();
        if (isCardByDefinitionExitst(definition)) {
            System.out.printf("The definition \"%s\" already exists.\n", definition);
            return;
        }

        cards.add(new Card(cardName, definition));
        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", cardName, definition);

    }

    private static boolean isCardByDefinitionExitst(String definition) {
        for (Card card : cards) {
            if (card.getDefinition().equals(definition)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCardByNameExists(String name) {
        for (Card card : cards) {
            if (card.getCardName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    static void ask() {
        System.out.println("How many times to ask?");
        int timesToAsk = sc.nextInt();
        sc.nextLine();
        int attempt = 0;
        while (attempt < timesToAsk) {
            for (Card card : cards) {
                attempt++;
                System.out.printf("Print the definition of \"%s\":\n", card.getCardName());
                String answer = sc.nextLine();
                if (answer.equals(card.getDefinition())) {
                    System.out.print("Correct answer.\n");
                    updateHardest(card, false);
                } else {
                    String correctKey = "";
                    for (Card card2 : cards) {
                        if (card2.getDefinition().equals(answer)) {
                            correctKey = card2.getCardName();
                        }
                    }
                    if (!correctKey.equals("")) {
                        System.out.printf("Wrong answer. The correct one is \"%s\", you've just written the definition of " +
                                "\"%s\".\n", card.getDefinition(), correctKey);
                    } else {
                        System.out.printf("Wrong answer. The correct one is \"%s\".\n", card.getDefinition());
                    }

                    updateHardest(card, true);

                }
                if (attempt == timesToAsk) {
                    break;
                }
            }
        }
    }

    private static void updateHardest(Card card, boolean isMistake) {
        if (!cardCounterHard.containsKey(card)) {
            if (isMistake) {
                cardCounterHard.put(card, 1);
            } else {
                cardCounterHard.put(card, 0);
            }
        } else {
            cardCounterHard.put(card, cardCounterHard.get(card) + 1);
        }
    }

    public String getCardName() {
        return cardName;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardName, card.cardName) &&
                Objects.equals(definition, card.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardName, definition);
    }

    @Override
    public int compareTo(Card card) {
        return card.getCardName().compareTo(this.cardName);
    }
}
