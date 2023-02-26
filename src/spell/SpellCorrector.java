package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    private final Trie dictionary = new Trie();
    private final Set<String> allPoss = new TreeSet<>();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner input = new Scanner(file);
        while (input.hasNext()) {
            String line = input.next();
            dictionary.add(line);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase(Locale.ROOT);
        INode suggestion = dictionary.find(inputWord);
        if (suggestion != null && suggestion.getValue() > 0) {
            return inputWord;
        }
        Set<String> options = editDistance1(inputWord);
        if (options.size() == 0) {
            for (String disOne : allPoss) {
                options.addAll(editDistance2(disOne));
            }
        }
        return theChosenOne(options);
    }

    private Set<String> editDistance1(String input) {
        StringBuilder user = new StringBuilder(input);
        Set<String> accepted = new TreeSet<>();
        for (int i = 0; i < input.length(); ++i) {
            user.deleteCharAt(i);
            String temp = user.toString();
            allPoss.add(temp);
            INode possibility = dictionary.find(temp);
            if (possibility != null && possibility.getValue() > 0) {
                accepted.add(temp);
            }
            user.delete(0,user.length());
            user.append(input);
            for (int j = 0; j < input.length(); ++j) {
                user.setCharAt(i, input.charAt(j));
                user.setCharAt(j, input.charAt(i));
                temp = user.toString();
                allPoss.add(temp);
                possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
            for (int j = 0; j < 26; ++j) {
                user.deleteCharAt(i);
                user.insert(i, (char)('a' + j));
                temp = user.toString();
                allPoss.add(temp);
                possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
        }
        for (int i = 0; i < input.length() + 1; ++i) {
            for (int j = 0; j < 26; ++j) {
                user.insert(i, (char)('a' + j));
                String temp = user.toString();
                allPoss.add(temp);
                INode possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
        }
        return accepted;
    }

    private Set<String> editDistance2(String input) {
        StringBuilder user = new StringBuilder(input);
        Set<String> accepted = new TreeSet<>();
        for (int i = 0; i < input.length(); ++i) {
            user.deleteCharAt(i);
            String temp = user.toString();
            INode possibility = dictionary.find(temp);
            if (possibility != null && possibility.getValue() > 0) {
                accepted.add(temp);
            }
            user.delete(0,user.length());
            user.append(input);
            for (int j = 0; j < input.length(); ++j) {
                user.setCharAt(i, input.charAt(j));
                user.setCharAt(j, input.charAt(i));
                temp = user.toString();
                possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
            for (int j = 0; j < 26; ++j) {
                user.deleteCharAt(i);
                user.insert(i, (char)('a' + j));
                temp = user.toString();
                possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
        }
        for (int i = 0; i < input.length() + 1; ++i) {
            for (int j = 0; j < 26; ++j) {
                user.insert(i, (char)('a' + j));
                String temp = user.toString();
                INode possibility = dictionary.find(temp);
                if (possibility != null && possibility.getValue() > 0) {
                    accepted.add(temp);
                }
                user.delete(0, user.length());
                user.append(input);
            }
        }
        return accepted;
    }

    private String theChosenOne(Set<String> options) {
        int currMax = 0;
        String theOne = null;
        for (String option : options) {
            INode poss = dictionary.find(option);
            if (poss.getValue() > currMax) {
                currMax = poss.getValue();
                theOne = option;
            }
        }
        return theOne;
    }
}
