/*
 *    LearnModelRegression.java
 *    Copyright (C) 2007 University of Waikato, Hamilton, New Zealand
 *    @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
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
package moa.tasks.regression;

import moa.capabilities.CapabilitiesHandler;
import moa.capabilities.Capability;
import moa.capabilities.ImmutableCapabilities;
import moa.streams.clustering.ClusterEvent;

import moa.learners.Regressor;
import moa.options.ClassOption;
import moa.tasks.AbstractLearnModel;

/**
 * Task for learning a model without any evaluation.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class LearnModel extends AbstractLearnModel<Regressor> implements RegressionMainTask, CapabilitiesHandler {

    @Override
    public String getPurposeString() {
        return "Learns a regression model from a stream.";
    }

    private static final long serialVersionUID = 1L;

    public LearnModel() {
		this.learnerOption = new ClassOption("learner", 'l', "Learner to train.", Regressor.class, "moa.classifiers.trees.FIMTDD");
    }
    
    public ImmutableCapabilities defineImmutableCapabilities() {
        // We are restricting tasks based on view mode
        return new ImmutableCapabilities(Capability.VIEW_STANDARD);
    }
    
}
