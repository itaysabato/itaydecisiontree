import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 06:00:20 <br/>
 */
public interface GeneralizationErrorFunction {
    double error(List<Enum[]> samples, List<Boolean> labels, DecisionTree tree);    
}
