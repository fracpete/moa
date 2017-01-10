/*
 *    MLOzaBag.java
 *    Copyright (C) 2012 University of Waikato, Hamilton, New Zealand
 *    @author Jesse Read (jesse@tsc.uc3m.es)
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *    
 */
package moa.classifiers.multilabel.meta;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.MultiLabelPrediction;
import com.yahoo.labs.samoa.instances.Prediction;
import com.yahoo.labs.samoa.instances.StructuredInstance;

import moa.classifiers.Classifier;
import moa.classifiers.MultiLabelLearner;
import moa.classifiers.MultiTargetRegressor;
import moa.classifiers.meta.OzaBag;
import moa.core.DoubleVector;
import moa.core.Example;
import moa.options.ClassOption;

/**
 * OzaBag for Multi-label data.
 * 
 * @author Jesse Read (jesse@tsc.uc3m.es)
 * @version $Revision: 1 $
 */
public class MTOzaBag extends OzaBag implements MultiLabelLearner, MultiTargetRegressor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MTOzaBag() {
    	super();
    	this.baseLearnerOption = new ClassOption("baseLearner", 'l',
                "Classifier to train.", MultiLabelLearner.class, "multilabel.trees.ISOUPTree");
    	}

	@Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        MultiLabelLearner baseLearner = (MultiLabelLearner) getPreparedClassOption(this.baseLearnerOption);
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
            this.ensemble[i].resetLearning();
            this.ensemble[i].setRandomSeed(this.randomSeed + i * 100);
        }
    }

    
    @Override    
    public void modelContextSet() {
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i].setModelContext(this.getModelContext());;
        }
    }
    @Override // @note don't need this here
    public boolean isRandomizable() {
        return true;
    }

	public Prediction getPredictionForInstance(StructuredInstance inst) {
		int numTargets = getModelContext().numOutputAttributes();
		DoubleVector[][] predictions = new DoubleVector[this.ensemble.length][numTargets];
		for (int i = 0; i < this.ensemble.length; i++) {
			Prediction basePrediction = this.ensemble[i].getPredictionForInstance(inst);
			predictions[i] = ((MultiLabelPrediction) basePrediction).getPrediction();
		}
		DoubleVector[] prediction = new DoubleVector[numTargets];
		for (int j = 0; j < numTargets; j++) {
			prediction[j] = new DoubleVector();
			for (int i = 0; i < this.ensemble.length; i++) {
				prediction[j].addValues(predictions[i][j]);
			}
			prediction[j].scaleValues(1.0 / this.ensemble.length);
		}
		return new MultiLabelPrediction(prediction);
	}
	
	public Prediction getPredictionForInstanceUsingN(StructuredInstance inst, int n) {
		int actual = Math.max(1, Math.min(n, this.ensemble.length));
		DoubleVector[][] predictions = new DoubleVector[actual][getModelContext().numOutputAttributes()];
		for (int i = 0; i < actual; i++) {
			Prediction basePrediction = this.ensemble[i].getPredictionForInstance(inst);
			predictions[i] = ((MultiLabelPrediction) basePrediction).getPrediction();
		}
		DoubleVector[] prediction = new DoubleVector[getModelContext().numOutputAttributes()];
		for (int j = 0; j < getModelContext().numOutputAttributes(); j++) {
			prediction[j] = new DoubleVector();
			for (int i = 0; i < actual; i++) {
				prediction[j].addValues(predictions[i][j]);
			}
			prediction[j].scaleValues(1.0 / actual);
		}
		return new MultiLabelPrediction(prediction);
	}

    @Override
    public void trainOnInstanceImpl(StructuredInstance instance) {
        trainOnInstanceImpl((Instance) instance);
    }
    
    @Override
	public Prediction getPredictionForInstance(Example<Instance> example) {
		return getPredictionForInstance((StructuredInstance)example.getData());
	}
    
}
