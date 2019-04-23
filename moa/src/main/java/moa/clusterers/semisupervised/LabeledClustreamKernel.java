package moa.clusterers.semisupervised;

import com.yahoo.labs.samoa.instances.Instance;
import moa.cluster.CFCluster;
import moa.clusterers.clustream.ClustreamKernel;
import moa.core.DoubleVector;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * An extension of <code>ClustreamKernel</code>. Apart from the common statistics (linear sum, squared sum, etc.),
 * it also keeps track of the label counts of the instances classified in this cluster
 */
public class LabeledClustreamKernel extends ClustreamKernel {

    /** Keeps track of the count of the class labels */
    private Map<Double, Integer> labelCount;

    /** Counts the number of times this cluster has been used to give prediction */
    private int timesGivingPrediction;
    private boolean hasGivenPrediction;
    private static int numID = 0;

    /**
     * Creates a new labeled clustream kernel. Initializes the label count of instance's label to 1
     * @param instance an instance point
     * @param dimensions instance dimension
     * @param timestamp timestamp
     * @param t
     * @param m
     */
    public LabeledClustreamKernel(Instance instance, int dimensions, long timestamp, double t, int m) {
        super(instance, dimensions, timestamp, t, m);

        // initializes the label count
        labelCount = new HashMap<>();
        try {
            if (!instance.classIsMissing()) {
                incrementLabelCount(instance.classValue(), 1);
            }
        } catch (NullPointerException e) { /* shhh say nothing... */ }

        // set the id
        this.setId(numID++);

        // some side information
        this.timesGivingPrediction = 0;
        this.hasGivenPrediction = false;
    }

    public LabeledClustreamKernel(ClustreamKernel cluster, double t, int m) {
        super(cluster, t, m);

        // copy the label count + time giving prediction
        if (cluster instanceof LabeledClustreamKernel) {
            this.labelCount = ((LabeledClustreamKernel) cluster).labelCount; // or a copy?
            this.timesGivingPrediction = ((LabeledClustreamKernel) cluster).getTimesGivingPrediction();
            this.hasGivenPrediction = ((LabeledClustreamKernel) cluster).hasGivenPrediction();
        }

        // set the ID
        this.setId(numID++);
    }

    @Override
    public void insert(Instance instance, long timestamp) {
        super.insert(instance, timestamp);

        // update the label count
        if (!instance.classIsMissing()) incrementLabelCount(instance.classValue(), 1);
    }

    @Override
    public void add(CFCluster other) {
        super.add(other);

        // accumulate the label count & times giving prediction
        if (other instanceof LabeledClustreamKernel) {
            LabeledClustreamKernel lc = (LabeledClustreamKernel) other;
            for (Map.Entry<Double, Integer> entry : lc.labelCount.entrySet()) {
                this.incrementLabelCount(entry.getKey(), entry.getValue());
            }
            this.timesGivingPrediction += lc.getTimesGivingPrediction();
            this.hasGivenPrediction = lc.hasGivenPrediction();
        }
    }

    /**
     * Increments the count of a given label in this cluster
     * @param label the class label
     * @param amount the amount to increment
     */
    @Override
    public void incrementLabelCount(Double label, int amount) {
        if (labelCount.containsKey(label)) {
            labelCount.put(label, labelCount.get(label) + amount);
        } else {
            labelCount.put(label, amount);
        }
    }

    /**
     * Decrements the count of a given label in this cluster
     * @param label the class label
     * @param amount the amount to increment
     */
    public void decrementLabelCount(Double label, int amount) {
        if (labelCount.containsKey(label)) {
            labelCount.put(label, labelCount.get(label) - amount);
        } else {
            labelCount.put(label, amount);
        }
    }

    @Override
    public double getInclusionProbability(Instance instance) {
        double distance = ClustreamSSL.distance(instance.toDoubleArray(), this.getCenter());
        // problem: radius of a CFCluster is 0.0 (not explicitly declared)
        // return (distance < this.radiusFactor ? 1.0 : 0.0);
        return (distance < 0.2 ? 1.0 : 0.0);
    }

    /**
     * Gets the votes in terms of probability of each label memorized in this cluster
     * @return an array of probabilities of size #labels
     */
    public double[] getLabelVotes() {
        DoubleVector votes = new DoubleVector();
        for (Map.Entry<Double, Integer> entry : this.labelCount.entrySet()) {
            votes.addToValue(entry.getKey().intValue(), entry.getValue());
        }
        votes.normalize();
        return votes.getArrayRef();
    }

    /**
     * Gets the label that takes the majority vote in this cluster
     * @return the majority label
     */
    public double getMajorityLabel() {
        double label = 0;
        int maxCount = 0;
        for (Map.Entry<Double, Integer> entry : this.labelCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                label = entry.getKey();
            }
        }
        return label;
    }

    public Map<Double, Integer> getLabelCount() { return this.labelCount; }

    public void incrementTimesGivingPrediction(int amount) {
        this.timesGivingPrediction += amount;
    }

    public int getTimesGivingPrediction() { return this.timesGivingPrediction; }

    public void setTimesGivingPrediction(int amount) { this.timesGivingPrediction = amount; }

    public void setHasGivenPrediction() { this.hasGivenPrediction = true; }

    public void unsetHasGivenPrediction() { this.hasGivenPrediction = false; }

    public boolean hasGivenPrediction() { return this.hasGivenPrediction; }
}