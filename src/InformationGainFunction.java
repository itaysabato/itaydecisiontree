import java.util.LinkedList;
import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:17:54 <br/>
 */
public class InformationGainFunction implements GainFunction {
	
	double entropy(List<Boolean> labelSet){
		if (labelSet.isEmpty())
            return 0;

        int proportion = 0;
		for (Boolean lab:labelSet) {
			if (lab) 	proportion++;
		}
        if(proportion ==0 || proportion == labelSet.size()) return 0;
		double p1 = proportion/(double)labelSet.size();
		double p2 = (labelSet.size() - proportion)/(double)labelSet.size();
		return (-p1*Math.log(p1)/Math.log(2) -p2*Math.log(p2)/Math.log(2));
	}
	
	public double gain(List<Enum[]> samples, List<Boolean> labels, int featureIndex) {
		//TODO check if samples is empty??
    	double g = entropy(labels);
		
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
		int size = labels.size();
		for(int l=0;l<domain.length;l++)
			g -= (proportions[l]/size)*entropy(values.get(l));

        return g;
    }
    
    public String toString(){
		return "Information";
    	
    }
}
