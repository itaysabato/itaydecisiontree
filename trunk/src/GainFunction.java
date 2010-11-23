import java.util.List;

/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus <br/>
 * IDs: 036910008, 300618592 <br/>
 * Date: 23/11/2010 <br/>
 * Time: 05:53:03 <br/>
 */
public interface GainFunction {
    double gain(List<Enum[]> samples, List<Boolean> labels, int featureIndex);
}
