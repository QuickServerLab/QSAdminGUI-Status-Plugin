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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.DefaultValueDataset;
import org.jfree.data.Range;
import org.jfree.chart.TextTitle;

/**
 * MeterPanel for displaying client count for 
 * QuickServer Admin GUI - QSAdminGUI
 * @author Akshathkumar Shetty
 * @since 1.3.2
 */
public class MeterChart extends JPanel {

	private DefaultValueDataset data;
	private MeterPlot meterplot;
	private Range range; 

	private JFreeChart meterchart;
    private ChartPanel panelMeter;
	private Color bgColor;

	public MeterChart(String title) {
		data = new DefaultValueDataset(0.0);
		meterplot = new MeterPlot(data);
		range = new Range(0,20000);
		bgColor = new Color(238,238,230,255);

		meterplot.setRange(range);
		meterplot.setNormalRange(new Range(0,4000));
		meterplot.setWarningRange(new Range(4000,9000));
		meterplot.setCriticalRange(new Range(9000,20000));

		meterplot.setUnits("");
		meterplot.setDrawBorder(false);
		meterplot.setInsets(new Insets( 2, 2, 2, 2 ));
		
		meterchart = new JFreeChart(title+" Meter", 
			JFreeChart.DEFAULT_TITLE_FONT, meterplot, false);
		meterchart.setBackgroundPaint(bgColor);
		panelMeter = new ChartPanel(meterchart);
		setLayout(new BorderLayout());
		add(panelMeter, BorderLayout.CENTER);
	}

	public void setValue(Double  value) {
		data.setValue(value);
	}
	public Double getValue() {
		return (Double)data.getValue();
	}
	
	public void setUnits(String units) {
		meterplot.setUnits(units);
	}
	public String getUnits() {
		return meterplot.getUnits();
	}

	public void setTitle(String title) {
		meterchart.setTitle(
			new TextTitle(title,JFreeChart.DEFAULT_TITLE_FONT) );
	}
	public String getTitle() {
		return meterchart.getTitle().getText();
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		meterchart.setBackgroundPaint(bgColor);
	}
}
