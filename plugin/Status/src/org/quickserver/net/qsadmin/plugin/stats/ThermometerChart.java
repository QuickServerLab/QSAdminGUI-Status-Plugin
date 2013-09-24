/*
 * This file is part of the QuickServer library 
 * Copyright (C) 2003-2005 QuickServer.org
 *
 * Use, modification, copying and distribution of this software is subject to
 * the terms and conditions of the GNU Lesser General Public License. 
 * You should have received a copy of the GNU LGP License along with this 
 * library; if not, you can download a copy from <http://www.quickserver.org/>.
 *
 * For questions, suggestions, bug-reports, enhancement-requests etc.
 * visit http://www.quickserver.org
 *
 */

package org.quickserver.net.qsadmin.plugin.stats;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;

import org.jfree.chart.plot.JThermometer;


/**
 * ThermometerChart Panel for displaying client count for 
 * QuickServer Admin GUI - QSAdminGUI
 * @author Akshathkumar Shetty
 * @since 1.3.2
 */
public class ThermometerChart extends JPanel {
	 private JThermometer thermo1 = new JThermometer();
	 private Double value = new Double(0);
	 private Color bgColor;

	 public ThermometerChart(String title) {
		bgColor = new Color(238,238,230,255);
		thermo1.setValue(value);
		thermo1.setBackground(bgColor);
		thermo1.setOutlinePaint(null);
		thermo1.setUnits(0);
		thermo1.setShowValueLines(true);
        thermo1.setFollowDataInSubranges(true);
		//thermo1.setForeground(Color.blue);
		thermo1.setValuePaint(Color.BLACK);

		setRange(0, 300);
		/*
        setSubrangeInfo(0,   0,  50,    0, 60);
        setSubrangeInfo(1,  50,  100,  40, 110);
        setSubrangeInfo(2, 110,  250, 100, 300);
		*/

		thermo1.addSubtitle(title, new Font("Dialog", Font.BOLD, 15));
		thermo1.setToolTipText(title);
		setToolTipText(title);

		setLayout(new BorderLayout());
		add(thermo1, BorderLayout.CENTER);
	 }

	public void setValue(Double  value) {
		this.value = value;
		thermo1.setValue(value);
	}
	public Double getValue() {
		return value;
	}

	public void setTitle(String title) {
		thermo1.addSubtitle(title);
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		thermo1.setBackground(bgColor);
	}

	public void setRange(double lower, double upper) {
		thermo1.setRange(lower, upper);
	}

	public void setSubrangeInfo(int range, double rangeLow, double rangeHigh, 
			double displayLow, double displayHigh) {
		thermo1.setSubrangeInfo(range, rangeLow, rangeHigh, displayLow, displayHigh);
	}
}
