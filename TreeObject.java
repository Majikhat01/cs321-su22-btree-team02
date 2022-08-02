import com.sun.source.tree.Tree;

public class TreeObject {

    //Substring of DNA
    private long DNA;

    //Frequency of character substring that appears in DNA
    private int frequency;

    public void incrementFrequency() {
        frequency ++;
    }

    public int getFrequency() {
        return frequency;
    }

    public int setFrequency(int newFrequency) {
        return frequency = newFrequency;
    }

    public int compareTo(TreeObject compObject) {
        if (DNA > compObject.getDNA()) {
            return 1;
        }
        if (DNA < compObject.getDNA()) {
            return -1;
        }
        return 0;
    }

    public TreeObject(long startDNA, int newFrequency) {
        DNA = startDNA;
        frequency = newFrequency;
    }

    public long getDNA() {
        return DNA;
    }

    public String toString() {
        String stringDNA = Long.toString(DNA);
        StringBuilder returnDNA = new StringBuilder();
        for (int i = 0; i < stringDNA.length(); i += 2) {
            String singleDNA = stringDNA.substring(i, i+1);
            switch (singleDNA) {
                case "00": returnDNA.append("A");
                case "11": returnDNA.append("T");
                case "01": returnDNA.append("C");
                case "10": returnDNA.append("G");
            }
        }
        return  returnDNA.toString() + ": " + frequency;
    }

}
