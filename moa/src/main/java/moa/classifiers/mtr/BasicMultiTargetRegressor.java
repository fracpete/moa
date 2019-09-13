/*
 *    BasicMultiTargetRegressor.java
 *    Copyright (C) 2017 University of Porto, Portugal
 *    @author J. Duarte, J. Gama
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

package moa.classifiers.mtr;

import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.instances.predictions.MultiTargetRegressionPrediction;
import com.yahoo.labs.samoa.instances.predictions.Prediction;

import moa.classifiers.AbstractInstanceLearner;
import moa.classifiers.AbstractMultiTargetRegressor;
import moa.core.DoubleVector;
import moa.core.FastVector;
import moa.core.Measurement;
import moa.core.StringUtils;
import moa.learners.MultiTargetRegressor;
import moa.learners.Regressor;
import moa.options.ClassOption;
import moa.streams.InstanceStream;


public class BasicMultiTargetRegressor extends AbstractMultiTargetRegressor implements MultiTargetRegressor {


	public BasicMultiTargetRegressor() {
		init();
	}

	protected void init() {
		baseLearnerOption = new ClassOption("baseLearner", 'l',
				"Classifier to train.", Regressor.class, "rules.AMRulesRegressor");
	}

	private static final long serialVersionUID = 1L;

	public ClassOption baseLearnerOption; //rules.AMRules"); 

	protected Regressor[] ensemble;

	protected boolean hasStarted = false;

	@Override
	public void resetLearningImpl() {
		this.hasStarted = false;
	}

	@Override
	public void trainOnInstanceImpl(Instance instance) {
		if (this.hasStarted == false){		
			this.ensemble = new Regressor[instance.numOutputAttributes()];
			Regressor baseLearner = (Regressor) getPreparedClassOption(this.baseLearnerOption);
			baseLearner.resetLearning();
			for (int i = 0; i < this.ensemble.length; i++) {
				this.ensemble[i] = (Regressor) baseLearner.copy();
			}
			this.hasStarted = true;
		}
		for (int i = 0; i < this.ensemble.length; i++) {
			Instance weightedInst = transformInstance(instance,i);
			this.ensemble[i].trainOnInstance(weightedInst); 
		}
	}

	protected InstancesHeader[] header;

	protected Instance transformInstance(Instance inst, int outputIndex) {
		if (header == null) {
			this.header = new InstancesHeader[this.ensemble.length];
		}
		if (header[outputIndex] == null) {
			//Create Header
			FastVector attributes = new FastVector();
			for (int attributeIndex = 0; attributeIndex < inst.numInputAttributes(); attributeIndex++) {
				attributes.addElement(inst.inputAttribute(attributeIndex));
			}
			//System.out.println("Number of attributes: "+this.numAttributes+ ","+inst.numAttributes());
			attributes.addElement(inst.outputAttribute(outputIndex));
			this.header[outputIndex] =  new InstancesHeader(new InstancesHeader(
					getCLICreationString(InstanceStream.class), attributes, 0));
			this.header[outputIndex].setClassIndex(attributes.size()-1);
			this.ensemble[outputIndex].setModelContext(this.header[outputIndex]);
		}
		//Instance instance = new DenseInstance(this.numAttributes+1);
		//instance.setDataset(dataset[classifierIndex]);
		int numAttributes = this.header[outputIndex].numInputAttributes();
		double[] attVals = new double[numAttributes+1]; //JD - +1 for class
		for (int attributeIndex = 0; attributeIndex < numAttributes; attributeIndex++) {
			attVals[attributeIndex] = inst.valueInputAttribute(attributeIndex);
		}
		Instance instance = new DenseInstance(1.0, attVals);
		instance.setDataset(header[outputIndex]);
		instance.setClassValue(inst.valueOutputAttribute(outputIndex));
		// System.out.println(inst.toString());
		// System.out.println(instance.toString());
		// System.out.println("============");
		return instance;
	}


	public boolean isRandomizable() {
		return true;
	}


	@Override
	protected Measurement[] getModelMeasurementsImpl() {
		if(ensemble.length>0)
			return ensemble[0].getModelMeasurements(); 
		//TODO: JD - get measurements for all outputs
		else 
			return null;
	}


	@Override
	public void getModelDescription(StringBuilder out, int indent) {
		if(ensemble.length>0 && ensemble[0] instanceof AbstractInstanceLearner)
		{
			for (int i=0; i<ensemble.length;i++){
				StringUtils.appendIndented(out,indent+1,"Model output attribute #" + i + "\n");
				((AbstractInstanceLearner)ensemble[i]).getModelDescription(out, indent+1);
			}
		}
	}
	

	@Override
	public Prediction getPredictionForInstance(Instance instance) {
		DoubleVector combinedVote = new DoubleVector();
		if (this.hasStarted){ 
			
			for (int i = 0; i < this.ensemble.length; i++) {
				double vote = this.ensemble[i].getPredictionForInstance(transformInstance(instance,i)).asDoubleArray()[0];
				combinedVote.setValue(i, vote);
			}
		}
		return new MultiTargetRegressionPrediction(combinedVote);
	}
}


