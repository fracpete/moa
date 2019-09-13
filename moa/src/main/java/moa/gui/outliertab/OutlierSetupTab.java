/*
 *    OutlierSetupTab.java
 *    Copyright (C) 2013 Aristotle University of Thessaloniki, Greece
 *    @author D. Georgiadis, A. Gounaris, A. Papadopoulos, K. Tsichlas, Y. Manolopoulos
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

package moa.gui.outliertab;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import moa.clusterers.outliers.MyBaseOutlierDetector;
import moa.evaluation.MeasureCollection;
import moa.gui.FileExtensionFilter;
import moa.gui.TextViewerPanel;
import moa.streams.clustering.ClusteringStream;
import nz.ac.waikato.cms.gui.core.BaseFileChooser;

public class OutlierSetupTab extends javax.swing.JPanel {
    private OutlierTabPanel outlierTab;
    private String lastfile;

    /** Creates new form outlierSetupTab */
    public OutlierSetupTab() {
        initComponents();
        outlierAlgoPanel0.renderAlgoPanel();
    }

    public MyBaseOutlierDetector getOutlierer0(){
        return outlierAlgoPanel0.getClusterer0();
    }
    
    public MyBaseOutlierDetector getOutlierer1(){
        return outlierAlgoPanel0.getClusterer1();
    }

    public ClusteringStream getStream0(){
        return outlierAlgoPanel0.getStream();
    }

    public MeasureCollection[] getMeasures(){
        return outlierEvalPanel1.getSelectedMeasures();
    }

    public TextViewerPanel getLogPanel(){
        return logPanel;
    }

    public void addButtonActionListener(ActionListener l){
        buttonWeka.addActionListener(l);
        buttonWeka.setActionCommand("weka export");
        buttonExport.addActionListener(l);
        buttonExport.setActionCommand("csv export");
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        outlierAlgoPanel0 = new moa.gui.outliertab.OutlierAlgoPanel();
        outlierEvalPanel1 = new moa.gui.outliertab.OutlierEvalPanel();
        buttonStart = new javax.swing.JButton();
        buttonStop = new javax.swing.JButton();
        buttonExport = new javax.swing.JButton();
        buttonWeka = new javax.swing.JButton();
        buttonImportSettings = new javax.swing.JButton();
        buttonExportSettings = new javax.swing.JButton();
        logPanel = new moa.gui.TextViewerPanel();

        setLayout(new java.awt.GridBagLayout());

        outlierAlgoPanel0.setMinimumSize(new java.awt.Dimension(335, 150));
        outlierAlgoPanel0.setPanelTitle("Outlier Detection Algorithm Setup");
        outlierAlgoPanel0.setPreferredSize(new java.awt.Dimension(500, 150));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(outlierAlgoPanel0, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        // ### add(outlierEvalPanel1, gridBagConstraints);

        int iBtnHeight = 27;
        
        buttonStart.setText("Start");
        buttonStart.setMinimumSize(new java.awt.Dimension(80, iBtnHeight));
        buttonStart.setPreferredSize(new java.awt.Dimension(80, iBtnHeight));
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonStart, gridBagConstraints);

        buttonStop.setText("Stop");
        buttonStop.setMinimumSize(new java.awt.Dimension(80, iBtnHeight));
        buttonStop.setPreferredSize(new java.awt.Dimension(80, iBtnHeight));
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonStop, gridBagConstraints);

        buttonExport.setText("Export CSV");
        buttonExport.setMinimumSize(new java.awt.Dimension(120, iBtnHeight));
        buttonExport.setPreferredSize(new java.awt.Dimension(120, iBtnHeight));
        buttonExport.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonExport, gridBagConstraints);

        buttonWeka.setText("Weka Explorer");
        buttonWeka.setMinimumSize(new java.awt.Dimension(120, iBtnHeight));
        buttonWeka.setPreferredSize(new java.awt.Dimension(120, iBtnHeight));
        buttonWeka.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonWeka, gridBagConstraints);

        buttonImportSettings.setText("Import");
        buttonImportSettings.setMinimumSize(new java.awt.Dimension(80, iBtnHeight));
        buttonImportSettings.setPreferredSize(new java.awt.Dimension(80, iBtnHeight));
        buttonImportSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonImportSettingsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 75, 4, 4);
        add(buttonImportSettings, gridBagConstraints);

        buttonExportSettings.setText("Export");
        buttonExportSettings.setMinimumSize(new java.awt.Dimension(80, iBtnHeight));
        buttonExportSettings.setPreferredSize(new java.awt.Dimension(80, iBtnHeight));
        buttonExportSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportSettingsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonExportSettings, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(logPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonImportSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonImportSettingsActionPerformed

        BaseFileChooser fileChooser = new BaseFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.addChoosableFileFilter(new FileExtensionFilter("txt"));
        if(lastfile!=null)
            fileChooser.setSelectedFile(new File(lastfile));
        if (fileChooser.showOpenDialog(this.buttonImportSettings) == BaseFileChooser.APPROVE_OPTION) {
            lastfile = fileChooser.getSelectedFile().getPath();
            loadOptionsFromFile(fileChooser.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_buttonImportSettingsActionPerformed

    private void buttonExportSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportSettingsActionPerformed
        StringBuffer sb = new StringBuffer();
        sb.append(outlierAlgoPanel0.getStreamValueAsCLIString()+"\n");
        sb.append(outlierAlgoPanel0.getAlgorithm0ValueAsCLIString()+"\n");
        
        System.out.println(sb);
        logPanel.addText(sb.toString());
        
    }//GEN-LAST:event_buttonExportSettingsActionPerformed

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        toggle(true);
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
        stop(true);
    }//GEN-LAST:event_buttonStopActionPerformed


    private void loadOptionsFromFile(String filepath){
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));

            String stream0 = in.readLine();
            outlierAlgoPanel0.setStreamValueAsCLIString(stream0);

            String algo0 = in.readLine();
            outlierAlgoPanel0.setAlgorithm0ValueAsCLIString(algo0);

            System.out.println("Loading settings from "+filepath);
            logPanel.addText("Loading settings from "+filepath);

        } catch (Exception e) {
            System.out.println("Bad option file:"+e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonExport;
    private javax.swing.JButton buttonExportSettings;
    private javax.swing.JButton buttonImportSettings;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JButton buttonWeka;
    private moa.gui.outliertab.OutlierAlgoPanel outlierAlgoPanel0;
    private moa.gui.outliertab.OutlierEvalPanel outlierEvalPanel1;
    private moa.gui.TextViewerPanel logPanel;
    // End of variables declaration//GEN-END:variables

    void setOutlierTab(OutlierTabPanel outlierTab) {
        this.outlierTab = outlierTab;
    }

    public void toggleRunMode(){
        toggle(false);
    }

    public void stopRun(){
        stop(false);
    }

    private void toggle(boolean internal) {
        setStateConfigButtons(false);
        if(buttonStart.getText().equals("Pause")){
            buttonStart.setText("Resume");
            buttonWeka.setEnabled(true);
            buttonExport.setEnabled(true);
        }
        else{
            buttonStart.setText("Pause");
            buttonWeka.setEnabled(false);
            buttonExport.setEnabled(false);
        }

        //push event forward to the cluster tab
        if(internal)
            outlierTab.toggle();
    }


    private void stop(boolean internal) {
        buttonStart.setEnabled(true);
        buttonStart.setText("Start");
        buttonStop.setEnabled(false);
        buttonWeka.setEnabled(false);
        buttonExport.setEnabled(false);
        setStateConfigButtons(true);

        //push event forward to the cluster tab
        if(internal)
            outlierTab.stop();
    }

    private void setStateConfigButtons(boolean state){
        buttonStop.setEnabled(!state);
        buttonExportSettings.setEnabled(state);
        buttonImportSettings.setEnabled(state);
    }


}
