package spell;

public class Trie implements ITrie{

    private int wordCount;
    private int nodeCount = 1;
    private INode root = new Node();

    @Override
    public void add(String word) {
        INode currNode = root;
        INode[] currChildren = root.getChildren();
        int index;
        for(int i = 0; i < word.length(); ++i) {
            char letter = word.charAt(i);
            index = letter - 'a';
            if (currChildren[index] == null) {
                currChildren[index] = new Node();
                ++nodeCount;
            }
            currNode = currChildren[index];
            currChildren = currNode.getChildren();
            if(i == word.length() - 1) {
                currNode.incrementValue();
                if(currNode.getValue() == 1) {
                    ++wordCount;
                }
            }
        }
    }

    @Override
    public INode find(String word) {
        INode currNode = root;
        INode[] currChildren = root.getChildren();
        int index;
        for(int i = 0; i < word.length(); ++i) {
            char letter = word.charAt(i);
            index = letter - 'a';
            if (currChildren[index] == null) {
                return null;
            }
            currNode = currChildren[index];
            currChildren = currNode.getChildren();
            if(i == word.length() - 1 && currNode.getValue() > 0) {
                return currNode;
            }
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        int hash = wordCount * nodeCount;
        INode[] children = root.getChildren();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] != null) {
                hash = hash * i;
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Trie t = (Trie)obj;
        if (t.wordCount != this.wordCount || t.nodeCount != this.nodeCount) {
            return false;
        }
        return equalsHelper(this.root, t.root);
    }

    private boolean equalsHelper(INode n1, INode n2) {
        if(n1.getValue() != n2.getValue()) {
            return false;
        }
        INode[] children1 = n1.getChildren();
        INode[] children2 = n2.getChildren();
        for (int i = 0; i < children1.length; ++i) {
            if(children1[i] == null && children2[i] != null ||
            children1[i] != null && children2[i] == null) {
                return false;
            }
        }
        boolean output = true;
        for (int i = 0; i < children1.length; ++i) {
            if(children1[i] != null) {
                output = equalsHelper(children1[i], children2[i]);
            }
        }
        return output;
    }

    @Override
    public String toString() {
        StringBuilder currWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringHelper(root, currWord, output);
        return output.toString();
    }

    private void toStringHelper(INode n, StringBuilder currWord, StringBuilder output) {
        if (n.getValue() > 0) {
            output.append(currWord.toString());
            output.append('\n');
        }
        for (int i = 0; i < root.getChildren().length; i++) {
            INode child = n.getChildren()[i];
            if (child != null) {
                char childLetter = (char)('a' + i);
                currWord.append(childLetter);
                toStringHelper(child, currWord, output);
                currWord.deleteCharAt(currWord.length() - 1);
            }
        }
    }
}
