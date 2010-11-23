import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:10:25 <br/>
 */
public class DecisionTreeImpl implements DecisionTree {
    Node root = null;
    private int size = 0;

    DecisionTreeImpl(Node root) {
        this.root = root;
    }

    public  DecisionTreeImpl(DecisionTreeImpl tree) {
        root = new Node();
        copy(root,tree.root);
    }

    void copy(Node node1,Node node2) {
        node1.feature = node2.feature;
        if(node2.children==null) return;
        node1.children = new ArrayList<Node>();
        int i = 0;
        for(;i<node2.children.size();i++) {
            node1.children.add(new Node());
            copy(node1.children.get(i),node2.children.get(i));
        }
    }

    void refreshSize(Node node) {
        size++;
        if(node.children==null) return;
        for(Node e:node.children)  refreshSize(e);
    }

    public int size() {
        return size;
    }

    public int bifurcationsCount() {
        return bifHelp(root);
    }

    private int  bifHelp(Node node) {
        if(node.children==null) return 0;
        int counter = 0;
        for(Node e:node.children)  counter+= bifHelp(e);
        return counter+1;
    }

    public boolean guessLabel(Enum[] X) {
        Node current = root;
        while(!current.isLeaf()) {
            current = current.children.get(X[current.feature].ordinal());
        }
        if(current.feature==0) return false;
        else return true;
    }

    public void plot(PrintStream printTo) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    static class Node {
        int feature = 0;
        List<Node> children = null;

        boolean isLeaf() {
            return children==null;
        }
    }
}
