package flashcards;

import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 0) {
            boolean isExport = false;
            String exportFileName = "";
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-import")) {
                    Card.cardUtility.importFromFile(args[i + 1]);
                }
                if (args[i].equals("-export")) {
                    isExport = true;
                    exportFileName = args[i + 1];
                }
            }
            actions(isExport, exportFileName);
        } else {
            actions(false, "");
        }
    }

    private static void actions(boolean isExport, String exportFileName) {
        String action = "";
        while (!"exit".equals(action)) {
            System.out.printf("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            action = sc.nextLine();
            switch (action) {
                case "add":
                    Card.addCard();
                    System.out.println();
                    break;
                case "remove":
                    Card.removeCard();
                    System.out.println();
                    break;
                case "import":
                    Card.cardUtility.importFromFile();
                    break;
                case "export":
                    Card.cardUtility.exportToFile();
                    break;
                case "ask":
                    Card.ask();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    if (isExport) {
                        Card.cardUtility.exportToFile(exportFileName);
                    }
                    break;
                case "log":
                    Card.cardUtility.exportLogToFile();
                    break;
                case "hardest card":
                    Card.getHardestCard();
                    break;
                case "reset stats":
                    Card.resetStats();
                    break;
            }
        }
    }
}