package flashcards;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CardUtility {
    final Scanner sc = new Scanner(System.in);

    private final Set<Card> cards;
    private final Map<Card, Integer> cardCounterHard;

    public CardUtility(Set<Card> cards, Map<Card, Integer> cardCounterHard) {
        this.cards = cards;
        this.cardCounterHard = cardCounterHard;
    }

    public void importFromFile() {
        System.out.println("File name:");
        String fileName = sc.nextLine();
        importFile(fileName);
    }

    public void importFromFile(String fileName) {
        importFile(fileName);
    }

    private void importFile(String fileName) {
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            int counter = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                Card card = new Card(line.split(":")[0], line.split(":")[1]);
                cards.add(card);
                cardCounterHard.put(card, Integer.parseInt(line.split(":")[2]));
                counter++;
            }
            System.out.printf("%d cards have been loaded.\n", counter);
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public void exportToFile(String fileName) {
        export(fileName);
    }

    public void exportToFile() {
        System.out.println("File name:");
        String fileName = sc.nextLine();
        export(fileName);
    }

    private void export(String fileName) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (Card card : cards) {
                printWriter.println(card.getCardName() + ":" + card.getDefinition() + ":" + cardCounterHard.get(card));
            }
            System.out.printf("%d cards have been saved.\n", cards.size());
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public void exportLogToFile() {
        System.out.println("File name:");
        String fileName = sc.nextLine();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {

        } catch (IOException e) {
            System.out.println("File not found.");
        }
        System.out.println("The log has been saved");
    }
}