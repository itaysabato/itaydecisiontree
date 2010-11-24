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
        size = tree.size;
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

    private int degree(int index,int[] degrees,List<Node> nodes){
        if (nodes.get(index).isLeaf()) {
            return 0;
        }
        else {
            int i = nodes.indexOf(nodes.get(index).children.get(0));
            return 1+degrees[i];
        }
    }

    public void plot(PrintStream printTo) {
        List<Node> nodes = TreeBuilder.nodesArray(this);
        if(nodes.isEmpty()) return;
        int[] depths = new int[nodes.size()];
        checkDepth(depths,nodes,root,0) ;
        int i = 0;
        int prev = 0;
        for(;i<depths.length;i++)  printTo.print(depths[i]);
        printTo.println();
        i = 0;
        for(;i<nodes.size();i++) {
            if(prev!=depths[i]) {
                printTo.println();
                prev = depths[i] ;
            }
            if(nodes.get(i).isLeaf())   printTo.print("<"+nodes.get(i).feature+"> \t");
            else printTo.print(nodes.get(i).feature+"? \t");
        }                                                          
        printTo.println();
    }

    public void print() {
        printHelp(root,0);
    }

    private void printHelp(Node node,int i) {
        if(node.isLeaf()) {
            System.out.print("<"+node.feature+">("+i+"), ");
        }
        if(i==3)  {
            System.out.print(node.feature+"?("+i+"), ");
        }
        for(Node child:node.children) {
         printHelp(child,i+1);
        }
         System.out.print(node.feature+"?("+i+"), ");
    }

    public DecisionTreeImpl prune(List<Enum[]> samples, List<Boolean> labels, GeneralizationErrorFunction function) {
        if(root.isLeaf()) return this;

        int i = 0;
        for(Node child: root.children){
            prune(root, i, child, samples, labels, function);
            i++;
        }
        int best = -1;
        double minError = function.error(samples,labels,this);
        double result = 0;
        i = 0;
        Node temp = root;
        for(Node child: temp.children){
            root = child;
            result = function.error(samples,labels,this);
            if(result <= minError){
                best = i;
                minError = result;
            }
            i++;
        }
        root = temp;
        List<Node> childrenTemp =  root.children;
        int featureTemp  = root.feature;

        root.children = null;
        root.feature = 0;
        result = function.error(samples,labels,this);
        if(result <= minError){
            best = -2;
            minError = result;
        }

                root.feature = 1;
        result = function.error(samples,labels,this);
        if(result <= minError){
            best = -3;
        }

        if(best >= 0){
            root = childrenTemp.get(best);
        }
        else if(best == -1){
            root.children = childrenTemp;
            root.feature = featureTemp;
        }
        else if(best == -2){
            root.feature = 0;
        }
        else {
            root.feature = 1;
        }
         return this;
    }

    private void prune(Node parent, int i, Node current, List<Enum[]> samples, List<Boolean> labels, GeneralizationErrorFunction function) {
        if(current.isLeaf()) return;

        int j = 0;
        for(Node child: current.children){
            prune(current, j, child, samples, labels, function);
            j++;
        }

        double minError = function.error(samples,labels,this);
        int best = -1;

        List<Node> childrenTemp =  current.children;
        int featureTemp  = current.feature;
        current.children = null;
        double result = 0;

        current.feature = 0;
        result =  function.error(samples,labels,this);
        if(result <= minError){
            minError = result;
            best = -2;
        }

        current.feature = 1;
        result =  function.error(samples,labels,this);
        if(result <= minError){
            minError = result;
            best = -3;
        }

        j = 0;
        for(Node child: current.children){
            parent.children.remove(i);
            parent.children.add(i,child);
            result =  function.error(samples,labels,this);
            if(result <= minError){
                minError = result;
                best = j;
            }
            j++;
        }
        parent.children.remove(i);

        if(best >= 0) {
            parent.children.add(i,childrenTemp.get(best));
        }
        else {
            parent.children.add(i,current);
            if(best == -1){
                current.children = childrenTemp;
                current.feature = featureTemp;
            }
            else if(best == -2){
                current.feature = 0;
            }
            else {
                current.feature = 1;
            }
        }
    }

    
    
        private void checkDepth(int[] depths, List<Node> nodes, Node node,int i) {
        if(node.isLeaf()) {
            depths[nodes.indexOf(node)] = i;
            return;
        }
         depths[nodes.indexOf(node)] = i;
        for(Node child:node.children)
            checkDepth(depths,nodes,child,i+1); 
    }
    
    static class Node {
        int feature = 0;
        List<Node> children = null;

        boolean isLeaf() {
            return children==null;
        }
    }
}
