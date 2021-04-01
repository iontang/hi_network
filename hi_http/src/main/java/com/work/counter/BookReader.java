package com.work.counter;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 *
 * @author cindym
 */
public class BookReader {

    public static void main(String[] args) {

        // TODO code application logic here

        String book = "";
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> map7letter = new HashMap<>();
        HashMap<String, Integer> mapScrablescores = new HashMap<>();

        String frequentWord = "";
        String longWord7Chareter = "";
        String scrable = "";

        int frequentWordCount = 0;
        int l7word = 0;
        int scrableWordScore = 0;

        Scanner input = new Scanner(System.in);
        String FILENAME;

        System.out.println("Please enter a .txt file path to start: ");
        FILENAME = input.nextLine();

        try {

            //read the book line by line
            Stream<String> stream = Files.lines(Paths.get(FILENAME));
            String[] stringArray = stream.toArray(size -> new String[size]);
            for (String d : stringArray) {
                book += d;
            }


            String[] stringArraySplit = book.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");

            for (String word : stringArraySplit) {

                Integer n = map.get(word);
                n = (n == null) ? 1 : ++n;
                map.put(word, n);

                if  (word.length() == 7 )
                {
                    map7letter.put(word, n);
                }

                // calculate and store the scores
                if (scrabbleScore(word) > 0)
                {
                    mapScrablescores.put(word, scrabbleScore(word));

                }

            }

            Object[] objWords = map.entrySet().toArray();
            Arrays.sort(objWords, new Comparator() {
                public int compare(Object o1, Object o2) {

                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });

            for (Object e : objWords) {

                if (((Map.Entry<String, Integer>) e).getValue() > frequentWordCount) {

                    frequentWordCount = ((Map.Entry<String, Integer>) e).getValue();
                    frequentWord = ((Map.Entry<String, Integer>) e).getKey();

                }
            }


            Object[] objTo7letter= objWords;
            for (Object word7letter : objTo7letter)
            {
                String temp = ((Map.Entry<String, Integer>) word7letter).getKey();

                if (temp.length() == 7){

                    Integer p = map7letter.get(temp);
                    p = (p == null) ? 1 : ++p;
                    map7letter.put(temp, p);

                }

            }

            Object[] sevenLetterSort = map7letter.entrySet().toArray();
            Arrays.sort(sevenLetterSort, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });
            for (Object sevelLetter : sevenLetterSort) {
                if (((Map.Entry<String, Integer>) sevelLetter).getValue() > l7word) {
                    l7word = ((Map.Entry<String, Integer>) sevelLetter).getValue();
                    longWord7Chareter = ((Map.Entry<String, Integer>) sevelLetter).getKey();
                }
            }

            /// sort scrable scores and get the highest scoring word
            Object[] scrableScoreSort = mapScrablescores.entrySet().toArray();
            Arrays.sort(scrableScoreSort, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });

            for (Object sword : scrableScoreSort) {

                if (((Map.Entry<String, Integer>) sword).getValue() > scrableWordScore) {
                    scrableWordScore = ((Map.Entry<String, Integer>) sword).getValue();
                    scrable = ((Map.Entry<String, Integer>) sword).getKey();
                }

            }


            System.out.println("Most frequent word: "+frequentWord + " occured " + frequentWordCount + " times");

            System.out.println("Most frequent 7-character word: "+longWord7Chareter + " occured " + l7word + " times");

            System.out.println("Highest scoring word (s) : " +scrable + " with a score of "+scrableWordScore);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static int scrabbleScore(String scrabbleWord) {
        int score = 0;
        for (int i = 0; i < scrabbleWord.length(); i++) {
            char calculatedLetter = scrabbleWord.toUpperCase().charAt(i);
            switch (calculatedLetter) {
                case 'A':
                case 'E':
                case 'I':
                case 'L':
                case 'N':
                case 'O':
                case 'R':
                case 'S':
                case 'T':
                case 'U': //Jesus this is fugly
                    score += 1;
                    break;
                case 'D':
                case 'G':
                    score += 2;
                    break;
                case 'B':
                case 'C':
                case 'M':
                case 'P':
                    score += 3;
                    break;
                case 'F':
                case 'H':
                case 'V':
                case 'W':
                case 'Y':
                    score += 4;
                    break;
                case 'K':
                    score += 5;
                    break;
                case 'J':
                case 'X':
                    score += 8;
                    break;
                case 'Q':
                case 'Z':
                    score += 10;
                    break;
                default:
                    break;
            }
        }
        return score;
    }

}