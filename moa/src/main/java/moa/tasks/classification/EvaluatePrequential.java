package moa.tasks.classification;

import java.io.PrintStream;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.predictions.Prediction;

import moa.core.Utils;
import moa.evaluation.LearningPerformanceEvaluator;
import moa.learners.Classifier;
import moa.options.ClassOption;
import moa.tasks.AbstractEvaluatePrequential;

public class EvaluatePrequential extends AbstractEvaluatePrequential<Classifier> implements ClassificationMainTask {

	private static final long serialVersionUID = 1L;
	
	public EvaluatePrequential() {
		this.learnerOption = new ClassOption("learner", 'l', "Learner to train.", Classifier.class, "moa.classifiers.trees.HoeffdingTree");
		this.evaluatorOption = new ClassOption("evaluator", 'e',
					"Classification performance evaluation method.",
					LearningPerformanceEvaluator.class,
					"WindowClassificationPerformanceEvaluator");
	}

	@Override
    public String getPurposeString() {
        return "Evaluates a classifier on a stream by testing then training with each example in sequence.";
    }
	@Override
	public void printPrediction(PrintStream print, Instance inst, Prediction prediction) {
		int trueClass = (int) inst.classValue();
		print.println(Utils.maxIndex(prediction.asDoubleArray()) + "," + (inst.classIsMissing() ? " ? " : trueClass));

	}

}
