import java.util.LinkedList;
import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:17:02 <br/>
 */
public class GiniGainFunction implements GainFunction {
	
	private double proportion(List<Boolean> labelSet){
		if (labelSet.isEmpty())
            return 0;
        int p=0;
		for (boolean b:labelSet){
			if(b) p++;
		}
		
		return p/labelSet.size();
		
	}
	
	private double gini(double p ){
		return (1-Math.pow(p, 2) - Math.pow(1-p, 2));
	}
	
    public double gain(List<Enum[]> samples, List<Boolean> labels, int featureIndex) {
    	
    	Enum[] domain = (Enum[]) samples.get(0)[featureIndex].getDeclaringClass().getEnumConstants();
		List<List<Boolean>> values = new LinkedList<List<Boolean>>();
		int[] proportions = new int[domain.length];
		for (int k=0;k<domain.length;k++)
			values.add(new LinkedList<Boolean>());
		for (int i=0;i<samples.size();i++) {
			Enum[] arr = samples.get(i);
			for (int j=0;j<domain.length;j++){
				if (arr[featureIndex].equals(domain[j])){
					proportions[j]++;
					values.get(j).add(labels.get(i));
					break;
				}
			}
		}
		double g = gini(proportion(labels));
		int size = labels.size();
		for (int l=0;l<domain.length;l++){
			g -= (proportions[l]/size)*gini(proportion(values.get(l)));
		}
    	
    	return g;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    public String toString(){
		return "Gini";
    	
    }
}
