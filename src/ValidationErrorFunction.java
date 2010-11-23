import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:13:52 <br/>
 */
public class ValidationErrorFunction implements GeneralizationErrorFunction {
    public double error(List<Enum[]> samples, List<Boolean> labels, DecisionTree tree) {
    	int err =0;
    	for (int i=0;i<samples.size();i++){
    		if (tree.guessLabel(samples.get(i)) != labels.get(i))
    			err++;
    	}
    	
        return err/labels.size();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
