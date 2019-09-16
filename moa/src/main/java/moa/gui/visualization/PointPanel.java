/**
 * PointPanel.java
 *
 * @author Timm Jansen (moa@cs.rwth-aachen.de)
 * @editor Yunsu Kim
 *
 * Last edited: 2013/06/02
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

package moa.gui.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

public class PointPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	static final int POINTSIZE = 4;
	DataPoint point;

	protected int x_dim = 0;
	protected int y_dim = 1;
	protected Color col;
	protected Color default_color = Color.BLACK;

	protected int panel_size;
	protected int window_size;
	protected boolean highligted = false;

	protected double decayRate;
	protected double decayThreshold;

	protected int type;
	protected final int TYPE_PLAIN = 0;
	protected final int TYPE_CLUSTERED = 1;

	protected StreamPanel sp;

	/**
	 * Type 1: Possibly be decayed, colored by class label.
	 * 
	 * @param point
	 * @param streamPanel
	 * @param decayRate
	 * @param decayThreshold
	 */
	public PointPanel(DataPoint point, StreamPanel streamPanel, double decayRate, double decayThreshold) {
		this.point = point;
		this.panel_size = POINTSIZE;
		this.decayRate = decayRate;
		this.decayThreshold = decayThreshold;
		this.col = default_color;
		this.sp = streamPanel;
		this.type = TYPE_PLAIN;

		setVisible(true);
		setOpaque(false);
		setSize(new Dimension(1, 1));
		setLocation(0, 0);
		initComponents();
	}

	/**
	 * Type 2: Never be decayed, single color.
	 * 
	 * @param point
	 * @param streamPanel
	 * @param color
	 */
	public PointPanel(DataPoint point, StreamPanel streamPanel, Color color) {
		this.point = point;
		this.panel_size = POINTSIZE;
		this.decayRate = 0; // Never be decayed
		this.decayThreshold = 0;
		this.col = color;
		this.sp = streamPanel;
		this.type = TYPE_CLUSTERED;

		setVisible(true);
		setOpaque(false);
		setSize(new Dimension(1, 1));
		setLocation(0, 0);
		initComponents();
	}

	public void updateLocation() {
		window_size = Math.min(sp.getWidth(), sp.getHeight());

		x_dim = sp.getActiveXDim();
		y_dim = sp.getActiveYDim();

		setSize(new Dimension(panel_size + 1, panel_size + 1));
		setLocation((int) (point.value(x_dim) * window_size - (panel_size / 2)),
				(int) (point.value(y_dim) * window_size - (panel_size / 2)));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 296, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 266, Short.MAX_VALUE));
	}// </editor-fold>//GEN-END:initComponents

	@Override
	protected void paintComponent(Graphics g) {
		if (type == TYPE_PLAIN) {
			point.updateWeight(RunVisualizer.getCurrentTimestamp(), decayRate);
			if (point.weight() < decayThreshold) {
				getParent().remove(this);
				return;
			}
		}

		Color color = getColor();
		// Color errcolor = getErrorColor();
		// if (errcolor == null) {
		// errcolor = color;
		panel_size = POINTSIZE;
		// } else {
		// panel_size = POINTSIZE + 2;
		// }

		updateLocation();

		/*
		 * g.setColor(errcolor); g.drawOval(0, 0, panel_size, panel_size);
		 * g.setColor(color); g.fillOval(0, 0, panel_size, panel_size);
		 */

		if (type == TYPE_PLAIN) {
			g.setColor(color);
			if (point.isNoise()) {
				g.setFont(g.getFont().deriveFont(9.0f));
				g.drawChars(new char[] { 'x' }, 0, 1, 0, panel_size);
			} else {
				g.drawOval(0, 0, panel_size, panel_size);
				g.setColor(color);
				g.fillOval(0, 0, panel_size, panel_size);
			}
		} else if (type == TYPE_CLUSTERED) {
			g.setColor(color);
			g.drawOval(0, 0, panel_size, panel_size);
		}

		setToolTipText(point.getInfo(x_dim, y_dim));
	}

	private Color getErrorColor() {
		String cmdvalue = point.getMeasureValue("CMM");
		Color color = null;
		if (!cmdvalue.equals("")) {
			double err = Double.parseDouble(cmdvalue);
			if (err > 0.00001) {
				if (err > 0.7)
					err = 1;
				int alpha = (int) (100 + 155 * err);
				color = new Color(255, 0, 0, alpha);
			}
			if (err == 0.00001) {
				color = new Color(255, 0, 0, 100);
			}
		}
		return color;
	}

	private Color getColor() {
		Color color = null;

		if (type == TYPE_PLAIN) {
			ClusterPanel cp = sp.getHighlightedClusterPanel();

			if (cp != null) {
				if (cp.getClusterLabel() == point.classValue()) {
					color = Color.BLUE;
				}
			}

			if (color == null) {
				int alpha = (int) (point.weight() * 200 + 55);
				float numCl = 10;

				Color c;
				if (point.isNoise()) {
					c = Color.GRAY;
				} else {
					c = getPointColorbyClass(point, numCl);
				}

				color = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
			}
		} else if (type == TYPE_CLUSTERED) {
			color = this.col;
		}

		return color;
	}

	public static Color getPointColorbyClass(DataPoint point, float numClasses) {
		Color c;
		int classValue = (int) point.classValue();

		if (classValue != point.noiseLabel) {
			c = new Color(Color.HSBtoRGB((classValue + 1) / numClasses, 1f, 240f / 240));
		} else {
			c = Color.GRAY;
		}
		return c;
	}

	public void highlight(boolean enabled) {
		highligted = enabled;
		repaint();
	}

	public String getObjectInfo() {
		return point.getInfo(x_dim, y_dim);
	}

	@Override
	public String getToolTipText() {
		return super.getToolTipText();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

	public String getSVGString(int width) {
		StringBuffer out = new StringBuffer();

		int x = (int) (point.value(x_dim) * window_size);
		int y = (int) (point.value(y_dim) * window_size);
		int radius = panel_size / 2;
		// radius = 1;

		Color c = getColor();

		String color = "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
		double trans = c.getAlpha() / 255.0;

		out.append("<circle ");
		out.append("cx='" + x + "' cy='" + y + "' r='" + radius + "'");
		out.append(" stroke='" + color + "' stroke-width='0' fill='" + color + "' fill-opacity='" + trans
				+ "' stroke-opacity='" + trans + "'/>");
		out.append("\n");
		return out.toString();
	}

	public void drawOnCanvas(Graphics2D imageGraphics) {
		Point location = getLocation();

		if (type == TYPE_PLAIN) {
			imageGraphics.drawOval(location.x, location.y, panel_size, panel_size);
			imageGraphics.fillOval(location.x, location.y, panel_size, panel_size);
		} else if (type == TYPE_CLUSTERED) {
			imageGraphics.drawOval(location.x, location.y, panel_size, panel_size);
		}
	}
}
