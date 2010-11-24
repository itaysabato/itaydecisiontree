import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 05:20:08 <br/>
 */
public class Driver {
    private static enum Answer {
        YES, NO,UNANSWERED;
    }

    public static void main(String[] arguments) {
        File file = new File("ex2db.txt");
        try {
            Scanner scanner = new Scanner(file);

            List<Enum[]> testSet = new ArrayList<Enum[]>();
            List<Boolean> testSetLabels = new ArrayList<Boolean>();
            for(int i = 0; scanner.hasNextLine() && i < 100; i++){
                parseLine(scanner.nextLine(), testSet, testSetLabels);
            }

            List<Enum[]> trainSet = new ArrayList<Enum[]>();
            List<Boolean> trainSetLabels = new ArrayList<Boolean>();
            while(scanner.hasNextLine()){
                parseLine(scanner.nextLine(), trainSet, trainSetLabels);
            }

            makeTree(trainSet, trainSetLabels, testSet, testSetLabels, new InformationGainFunction());
            makeTree(trainSet, trainSetLabels, testSet, testSetLabels, new TrainingGainFunction());
            makeTree(trainSet, trainSetLabels, testSet, testSetLabels, new GiniGainFunction());

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void makeTree(List<Enum[]> trainSet, List<Boolean> trainSetLabels, List<Enum[]> testSet, List<Boolean> testSetLabels, GainFunction gainFunction) {
        TreeBuilder treeBuilder = new TreeBuilder();
        ValidationErrorFunction errorFunction = new  ValidationErrorFunction();
        double totalError = 0;
        double testError = 0;

        DecisionTreeImpl tree =  treeBuilder.buildID3Tree(trainSet, trainSetLabels, gainFunction);
        System.out.println("Original tree using "+gainFunction+" gain function:");
        tree.plot(System.out);
        System.out.println("Bifurcations: "+tree.bifurcationsCount());
        System.out.println("Train Error: "+errorFunction.error(trainSet, trainSetLabels, tree));
        System.out.println("Test Error: "+errorFunction.error(testSet, testSetLabels, tree));

        DecisionTreeImpl validationTree = treeBuilder.pruneTree(tree, testSet, testSetLabels, new ValidationErrorFunction());
        System.out.println("Pruned tree using "+gainFunction+" gain function and Validation Error:");
        validationTree.plot(System.out);

        testError =  errorFunction.error(testSet, testSetLabels, validationTree);
        System.out.println("Test Error: "+testError);

        totalError = testError*testSet.size() +  errorFunction.error(trainSet, trainSetLabels, tree)*trainSet.size();
        totalError /= testSet.size() + trainSet.size();
        System.out.println("Total Error (Cross Validation): "+totalError);

        DecisionTreeImpl MDLTree = treeBuilder.pruneTree(tree, testSet, testSetLabels, new MDLErrorFunction());
        System.out.println("Pruned tree using "+gainFunction+" gain function and MDL Error:");
        MDLTree.plot(System.out);

        testError =  errorFunction.error(testSet, testSetLabels, MDLTree);
        System.out.println("Test Error: "+testError);

        totalError = testError*testSet.size() +  errorFunction.error(trainSet, trainSetLabels, tree)*trainSet.size();
        totalError /= testSet.size() + trainSet.size();
        System.out.println("Total Error (Cross Validation): "+totalError);
    }

    private static void parseLine(String line, List<Enum[]> samples, List<Boolean> labels) {
        String[] values = line.split(" ");
        samples.add(toEnumArr(values, values.length - 1));
        labels.add(toLabel(values[values.length - 1]));
    }

    private static Boolean toLabel(String value) {
        return value.charAt(0) == 'r';
    }

    private static Enum[] toEnumArr(String[] values, int numFeatures) {
        Enum[] array = new Enum[numFeatures];

        for(int i = 0; i < numFeatures; i++){
            if(values[i].equals("y")){
                array[i] = Answer.YES;
            }
            else if(values[i].equals("n")){
                array[i] = Answer.NO;
            }
            else {
                array[i] = Answer.UNANSWERED;
            }
        }
        return array;
    }
}
