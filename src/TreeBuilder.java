import java.util.ArrayList;
import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 05:51:12 <br/>
 */
public class TreeBuilder {

    public DecisionTreeImpl buildID3Tree(List<Enum[]> samples, List<Boolean> labels, GainFunction function) {
        DecisionTreeImpl.Node root = new DecisionTreeImpl.Node();
        DecisionTreeImpl tree = new DecisionTreeImpl(root);
        //build the indexes list:
        ArrayList<Integer> indexes = new  ArrayList<Integer>();
        int i = 0;
        for(;i<samples.get(0).length; i++) indexes.add(i);
         //calls the algorithm:
        ID3(root,indexes, samples, labels,  function);
        tree.refreshSize(root);
        return tree;
    }

    private void ID3(DecisionTreeImpl.Node node, List<Integer> indexes ,List<Enum[]> samples, List<Boolean> labels,
                     GainFunction function) {
        //stop conditions:
        if(numLabels(labels,true)==labels.size() || numLabels(labels,false)==labels.size()) {
           if(labels.size()==0 || labels.get(0))  node.feature = 1;
           else node.feature = 0;
           return;
        }
        if(indexes.isEmpty()) {
            node.feature = majority(labels);
            return;
        }
        int idx = maxIdx(indexes , samples,labels,function);
        if(featureConstant(samples,idx)) {
            node.feature = majority(labels);
            return;
        }

        //building the children:
        node.feature = idx;
        node.children = new ArrayList<DecisionTreeImpl.Node>();
        DecisionTreeImpl.Node child = null;
        indexes.remove(new Integer(idx));
        List<Boolean> tempLabels = null;
        List<Enum[]> tempSamples = null;
        Enum[] answers = (Enum[]) samples.get(0)[0].getDeclaringClass().getEnumConstants();
        for(Enum e:answers)  {
            child = new DecisionTreeImpl.Node();
            node.children.add(child);
            //trims the sets:
            tempLabels = trimLabels(samples,labels,idx,e);
            tempSamples = trimSamples(samples,idx,e);
            ID3(child,indexes,samples,labels,function);
            samples.addAll(tempSamples);
            labels.addAll(tempLabels);
        }
        indexes.add(new Integer(idx));
    }

    private List<Enum[]>    trimSamples(List<Enum[]> samples, int idx, Enum e) {
        int i = 0;
        ArrayList<Enum[]> result = new ArrayList<Enum[]>();
        for(;i<samples.size();) {
            if(!samples.get(i)[idx].equals(e)) {
                result.add(samples.get(i));
                samples.remove(i);
            }
            else i++;
        }
        return result;
    }

        private List<Boolean>  trimLabels(List<Enum[]> samples, List<Boolean> labels, int idx, Enum e) {
        int i = 0;
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        int j = 0;
        for(;i<samples.size();i++) {
            if(!samples.get(i)[idx].equals(e)) {
                result.add(labels.get(j));
                labels.remove(j);
            }
            else  j++;
        }
            return result;
    }

    private boolean featureConstant(List<Enum[]> samples, int idx) {
        Enum featureValue = samples.get(0)[idx];
        for(Enum[] e:samples) {
            if(!e[idx].equals(featureValue)) return false;
        }
        return true;
    }

    private int numLabels(List<Boolean> labels,boolean bool) {
        int counter = 0;
        for(boolean e:labels) if(e==bool) counter++;
        return counter;
    }

    private int maxIdx(List<Integer> indexes ,List<Enum[]> samples, List<Boolean> labels,
                     GainFunction function) {
        int maxIdx = 0;
        double maxValue = 0;
        for(Integer i:indexes) {
            double current = function.gain(samples,labels,i) ;
            if(current>=maxValue) {
                maxIdx = i;
                maxValue = current;
            }
        }
         return maxIdx;
    }

     private int majority(List<Boolean> labels) {
        if(numLabels(labels,true)>numLabels(labels,false)) return 1;
        else return 0;
    }

    public DecisionTreeImpl pruneTree(DecisionTreeImpl tree, List<Enum[]> samples, List<Boolean> labels, GeneralizationErrorFunction function) {
        return new DecisionTreeImpl(tree).prune(samples, labels, function);
    }

    static  List<DecisionTreeImpl.Node> nodesArray(DecisionTreeImpl tree) {
        ArrayList<DecisionTreeImpl.Node> array = new ArrayList<DecisionTreeImpl.Node>();
        array.add(tree.root);
        int i = 0;
        System.out.println("size:"+tree.size());
        for(;i<tree.size();i++) {
            List<DecisionTreeImpl.Node> children = array.get(i).children;
            if(children==null) continue;
            array.addAll(children);
        }
        return array;
    }
}
