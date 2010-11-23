import java.io.PrintStream;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 05:40:24 <br/>
 */
public interface DecisionTree {
    int size();
    int bifurcationsCount();
    boolean guessLabel(Enum[] X);
    void plot(PrintStream printTo);
}
