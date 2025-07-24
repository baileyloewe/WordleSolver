import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class wordleSolver {
    protected Vector<String> Words = new Vector<>();
    protected Vector<String> Guesses = new Vector<>();
    protected HashMap<Integer, String> inSolutionWrongPos = new HashMap<>();
    protected HashMap<Integer, String> solution = new HashMap<>();
    protected HashMap<String, Double> letterScore = new HashMap<>();
    protected HashMap<String, Double> wordScore = new HashMap<>();
    protected Vector<String> notIn = new Vector<>();
    protected int wordCount;

    public static void main(String[] args) {
        wordleSolver ws = new wordleSolver();
        ws.setWords();
        while (true) {
            ws.printWords();
            ws.requestGuessAndLetters();
            ws.callAllReduceFunctions(ws);
            ws.callAllReduceFunctions(ws);
            ws.setScores();
            ws.suggestGuess();
        }
    }

    public boolean alreadyGuessed(String s) {
        for (String word : Guesses) {
            if (word.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean inSolution(String s) {
        for (Integer key : solution.keySet()) {
            if (s.equalsIgnoreCase(solution.get(key))) {
                return true;
            }
        }
        return false;
    }

    public void suggestGuess() {
        if (Guesses.size() >= 2) {
            this.getScore();
            double bestScore = 0;
            String bestScoreWord = "";
            for (String word : Words) {
                if (wordScore.get(word) > bestScore && !alreadyGuessed(word)) {
                    bestScore = wordScore.get(word);
                    bestScoreWord = word;
                }
            }
            System.out.println("Suggested Guess: " + bestScoreWord);
        }
        else if (Guesses.size() == 1) {
            System.out.println("Suggested Guess: slack");
        }
        else if (Guesses.isEmpty()) {
            System.out.println("Suggested Guess: tried");
        }
    }

    public void callAllReduceFunctions(wordleSolver ws) {
        ws.reduceWordListRemoveCompletelyWrongLetters();
        ws.reduceWordListRemoveWrongLettersAtPos();
        ws.reduceWordListUntilContainsCorrectLetters();
        ws.reduceWordListUntilCorrectLetterPosition();
    }

    public void removeWords() {
        for (int i = 0; i < Words.size(); i++) {
            if (Words.get(i).isEmpty()) {
                Words.remove(i);
                i--;
            }
        }
    }

    public void requestGuessAndLetters() {
        System.out.println("Input your guess: ");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().toLowerCase();
        Guesses.add(input);
        if (input.equals("solved")) {
            System.out.println("Congratulations, exiting");
            System.exit(0);
        } else {
            System.out.println("Input a value corresponding to the Wordle results for each letters." +
                    "\n0 means the letter was correct and correctly positioned." +
                    "\n1 means the letter was correct but incorrectly positioned." +
                    "\n2 means it was completely wrong");
            for (int i = 0; i < 5; i++) {
                String currentLetter = Character.toString(input.charAt(i));
                System.out.print(currentLetter.toUpperCase() + ": ");
                int j = in.nextInt();
                if (j == 0) {
                    solution.put(i, currentLetter);
                } else if (j == 1) {
                    inSolutionWrongPos.put(i, currentLetter);
                } else if (j == 2) {
                    notIn.add(currentLetter);
                }
            }
        }
    }

    public void printWords() {
        wordCount = 0;
        for (String word : Words) {
            wordCount++;
        }
        System.out.println("Word count: " + wordCount);
    }

    public void setWords() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/wordle-allowed-guesses.txt"))) {
            String line = br.readLine();
            while (line != null) {
                Words.add(line.toLowerCase());
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reduceWordListUntilCorrectLetterPosition() {
        String currentWord;
        for (int i = 0; i < Words.size(); i++) {
            currentWord = Words.get(i);
            for (int key : solution.keySet()) {
                if (!Character.toString(currentWord.charAt(key)).equals(solution.get(key))) {
                    Words.set(i, "");
                }
            }
        }
        removeWords();
    }

    public void reduceWordListUntilContainsCorrectLetters() {
        StringBuilder charsInWord = new StringBuilder();
        int numMatchesFound = 0;
        for (int key : inSolutionWrongPos.keySet()) {
            charsInWord.append(inSolutionWrongPos.get(key));
        }
        for (int key : solution.keySet()) {
            charsInWord.append(solution.get(key));
        }
        // This loop changes the word to be compared
        for (int i = 0; i < Words.size(); i++) {
            String currentWordToCompare = Words.get(i);
            // This loop goes through each letter of the word we are comparing
            for (int j = 0; j < charsInWord.length(); j++) {
                // This loop goes through each letter of the charsInWord, so we compare each letter to the letters in the word we are comparing
                for (int k = 0; k < currentWordToCompare.length(); k++) {
                    if (Character.toString(currentWordToCompare.charAt(k)).equals(Character.toString(charsInWord.charAt(j)))) {
                        numMatchesFound++;
                        break;
                    }
                }
            }
            if (numMatchesFound < charsInWord.length()) {
                Words.set(i, "");
            }
            numMatchesFound = 0;
        }
        removeWords();
    }

    public void reduceWordListRemoveWrongLettersAtPos() {
        String currentLetter;
        for (int key : inSolutionWrongPos.keySet()) {
            currentLetter = inSolutionWrongPos.get(key);
            for (int j = 0; j < Words.size(); j++) {
                if (!Words.get(j).isEmpty() && currentLetter.equals(Character.toString(Words.get(j).charAt(key)))) {
                    Words.set(j, "");
                }
            }
        }
        removeWords();
    }

    public void reduceWordListRemoveCompletelyWrongLetters() {
        String notInString;
        String currentWord;
        for (int i = 0; i < notIn.size(); i++) {
            notInString = notIn.get(i);
            for (int j = 0; j < Words.size(); j++) {
                currentWord = Words.get(j);
                for (int k = 0; k < currentWord.length(); k++) {
                    if (!inSolution(Character.toString(currentWord.charAt(k))) && !Words.get(j).isEmpty() && !Words.get(j).isEmpty() && notInString.equals(Character.toString(currentWord.charAt(k)))) {
                        Words.set(j, "");
                    }
                }
            }
        }
        removeWords();
    }

    public void getScore() {
        wordScore.clear();
        double score = 0;
        for (String word : Words) {
            for (int i = 0; i < word.length(); i++) {
                score += letterScore.get(Character.toString(word.charAt(i)));
            }
            wordScore.put(word, score);
        }
    }

    public void setScores() {
        letterScore.put("s", (double) 4106 / 41223);
        letterScore.put("e", (double) 3993 / 41223);
        letterScore.put("a", (double) 3615 / 41223);
        letterScore.put("r", (double) 2751 / 41223);
        letterScore.put("o", (double) 2626 / 41223);
        letterScore.put("i", (double) 2509 / 41223);
        letterScore.put("l", (double) 2231 / 41223);
        letterScore.put("t", (double) 2137 / 41223);
        letterScore.put("n", (double) 1912 / 41223);
        letterScore.put("u", (double) 1655 / 41223);
        letterScore.put("d", (double) 1615 / 41223);
        letterScore.put("c", (double) 1403 / 41223);
        letterScore.put("y", (double) 1371 / 41223);
        letterScore.put("p", (double) 1301 / 41223);
        letterScore.put("m", (double) 1267 / 41223);
        letterScore.put("h", (double) 1185 / 41223);
        letterScore.put("g", (double) 1050 / 41223);
        letterScore.put("b", (double) 1023 / 41223);
        letterScore.put("k", (double) 913 / 41223);
        letterScore.put("f", (double) 707 / 41223);
        letterScore.put("w", (double) 686 / 41223);
        letterScore.put("v", (double) 465 / 41223);
        letterScore.put("z", (double) 227 / 41223);
        letterScore.put("x", (double) 212 / 41223);
        letterScore.put("j", (double) 184 / 41223);
        letterScore.put("q", (double) 79 / 41223);
    }
}