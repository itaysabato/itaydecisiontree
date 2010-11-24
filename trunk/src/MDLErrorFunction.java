import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:15:52 <br/>
 */
public class MDLErrorFunction implements GeneralizationErrorFunction {
	
	private static final double DELTA = 0.05;

    public double error(List<Enum[]> samples, List<Boolean> labels, DecisionTree tree) {
    	 double error = new ValidationErrorFunction().error(samples, labels, tree);
    	int d  = samples.get(0).length;
    	int n = tree.size();
    	int m = labels.size();
    	return error + Math.sqrt(((n * (Math.log(d) + 1)) + Math.log(2 / DELTA))
                        / (double) (2 * m));
    }
}
