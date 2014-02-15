/*
 * This file is part of the QuickServer library 
 * Copyright (C) QuickServer.org
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.util.logging.*;

import org.quickserver.util.TextFile;
import org.quickserver.swing.JFrameUtilities;
import org.quickserver.net.qsadmin.gui.*;


/**
 * StatsPanel for
 * QuickServer Admin GUI - QSAdminGUI
 * @author Akshathkumar Shetty
 */
public class StatsPanel extends JPanel implements PluginPanel {
	
	private QSAdminMain qsAdminMain;
	private MeterChart meterPanel;
	private JPanel controlPanel;
	private JPanel otherPanel;
	private JPanel footerPanel;

	private ThermometerChart memoryTherm;
	private ThermometerChart threadPoolTherm;
	private ThermometerChart dataPoolTherm;
	private ThermometerChart byteBufferPoolTherm;

	private String target = "server";

	private GridBagConstraints gbc;

	private boolean showStats = false;

	//--start of PluginPanel
	public void setQSAdminMain(final QSAdminMain qsAdminMain) {
		this.qsAdminMain = qsAdminMain;
	}

	public void init() {
		new GetStats(StatsPanel.this, qsAdminMain);
	}

	public void activated() {
		showStats = true;
	}

	public void deactivated() {
		showStats = false;
	}

	public boolean isActivated() {
		return showStats;
	}
	//--end of PluginPanel

	public StatsPanel() {
		Container cp = this;
		GridBagConstraints gbc = new GridBagConstraints();
		
		meterPanel = new MeterChart("Client");
		controlPanel = new JPanel();
		otherPanel = new JPanel();
		footerPanel = new JPanel();

		//--set other
		memoryTherm = new ThermometerChart("Memory (in KB)");
		threadPoolTherm = new ThermometerChart("Threads");
		dataPoolTherm = new ThermometerChart("ClientData");
		byteBufferPoolTherm = new ThermometerChart("ByteBuffer");

		otherPanel.setLayout(new GridLayout(1,4,0,0));
		otherPanel.add(memoryTherm);
		otherPanel.add(threadPoolTherm);
		otherPanel.add(dataPoolTherm);
		otherPanel.add(byteBufferPoolTherm);
		

		//--main
		cp.setLayout(new GridBagLayout());		
		gbc.insets = new Insets( 0, 0, 0, 0 );
		gbc.weighty = 1.0;
		gbc.weightx = 0.6;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		cp.add(meterPanel, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.weightx = 0.4;
		gbc.anchor = GridBagConstraints.NORTHEAST; 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		cp.add(controlPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTHEAST; 
		gbc.fill = GridBagConstraints.BOTH;
		cp.add(otherPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 0.0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST; 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		cp.add(footerPanel, gbc);
	}	

	public void updateConnectionStatus(boolean connected) {
		if(connected==false) 
			deactivated();
		else {
			meterPanel.setValue(new Double(0.0));
		}
	}

	public void setClientCount(Double value) {
		meterPanel.setValue(value);
	}

	public void setMemoryInfo(float total, float used, float max) {
		memoryTherm.setRange(0, max);
		memoryTherm.setSubrangeInfo(0,         0,     total,    0, total+10);
        memoryTherm.setSubrangeInfo(1,     total,  max/0.75,  total-10, max/0.75+10);
		memoryTherm.setSubrangeInfo(2,  max/0.75,       max,   max/0.75-10, max);
		memoryTherm.setValue(new Double(used));
	}

	public void setClientThreadInfo(int active, int total) {
		if(total==0) total = 1;
		threadPoolTherm.setRange(0, total);
		threadPoolTherm.setSubrangeInfo(0, 0, total, 0, total);
		threadPoolTherm.setValue(new Double(active));
	}

	public void setDataPoolInfo(int active, int total) {
		if(total==0) total = 1;
		dataPoolTherm.setRange(0, total);
		dataPoolTherm.setSubrangeInfo(0, 0, total, 0, total);
		dataPoolTherm.setValue(new Double(active));
	}

	public void setByteBufferPoolInfo(int active, int total) {
		if(total==0) total = 1;
		byteBufferPoolTherm.setRange(0, total);
		byteBufferPoolTherm.setSubrangeInfo(0, 0, total, 0, total);
		byteBufferPoolTherm.setValue(new Double(active));
	}


}

class GetStats extends Thread {
	private static final Logger logger =  
		Logger.getLogger(GetStats.class.getName());

	private StatsPanel statsPanel;
	private QSAdminMain qsAdminMain;
	public GetStats(StatsPanel statsPanel, QSAdminMain qsAdminMain) {
		super("Stats-GUIAdmin");
		setDaemon(true);
		this.statsPanel = statsPanel;
		this.qsAdminMain = qsAdminMain;
		start();
	}

	public void run() {
		String count = "";
		String memInfo = "";
		String poolInfo = "";

		float usedMemory = 0;
        float totalMemory = 0;
		float maxMemory = 0;
		float kByte = 1024*8;

		long active = 0;
		long total = 0;

		while(true) {
			if(statsPanel.isActivated() && qsAdminMain.isLoggedIn()) {
				try	{
					count = qsAdminMain.sendCommunicationNoEcho("noclient server", false);
					if(count.startsWith("+OK")) {
						count = count.substring(4);
						statsPanel.setClientCount(new Double(count));	
					}
					memInfo = qsAdminMain.sendCommunicationNoEcho("memoryInfo", false);
					if(memInfo.startsWith("+OK")) {
						memInfo = memInfo.substring(4);
						StringTokenizer st = new StringTokenizer(memInfo, ":");
						if(st.countTokens()==3) {
							totalMemory = Float.parseFloat((String)st.nextToken()) / kByte;
							usedMemory = Float.parseFloat((String)st.nextToken()) / kByte;
							maxMemory = Float.parseFloat((String)st.nextToken()) / kByte;
							statsPanel.setMemoryInfo((int)totalMemory, (int)usedMemory, (int)maxMemory);
							/*
							logger.log(Level.FINE, "totalMemory : "+(int)totalMemory);
							logger.log(Level.FINE, "usedMemory : "+(int)usedMemory);
							logger.log(Level.FINE, "maxMemory : "+(int)maxMemory);
							*/
						} 					
					} else {
						logger.log(Level.WARNING, "Bad Response : "+memInfo);
					}
					poolInfo = qsAdminMain.sendCommunicationNoEcho("client-thread-pool-info server", false);
					if(poolInfo.startsWith("+OK")) {
						poolInfo = poolInfo.substring(4);
						StringTokenizer st = new StringTokenizer(poolInfo, ":");
						if(st.countTokens()==2) {
							active = Long.parseLong((String)st.nextToken());
							total = active + Long.parseLong((String)st.nextToken());
							statsPanel.setClientThreadInfo((int)active, (int)total);
						} 					
					} else {
						logger.log(Level.WARNING, "Bad Response : "+poolInfo);
						statsPanel.setClientThreadInfo(0,0);
					}
					poolInfo = qsAdminMain.sendCommunicationNoEcho("client-data-pool-info server", false);
					if(poolInfo.startsWith("+OK")) {
						poolInfo = poolInfo.substring(4);
						StringTokenizer st = new StringTokenizer(poolInfo, ":");
						if(st.countTokens()==2) {
							active = Long.parseLong((String)st.nextToken());
							total = active + Long.parseLong((String)st.nextToken());
							statsPanel.setDataPoolInfo((int)active, (int)total);
						} 					
					} else {
						logger.log(Level.FINEST, "Bad Response : "+poolInfo);
						statsPanel.setDataPoolInfo(0,0);
					}
					poolInfo = qsAdminMain.sendCommunicationNoEcho("byte-buffer-pool-info server", false);
					if(poolInfo.startsWith("+OK")) {
						poolInfo = poolInfo.substring(4);
						StringTokenizer st = new StringTokenizer(poolInfo, ":");
						if(st.countTokens()==2) {
							active = Long.parseLong((String)st.nextToken());
							total = active + Long.parseLong((String)st.nextToken());
							statsPanel.setByteBufferPoolInfo((int)active, (int)total);
						} 					
					} else {
						logger.log(Level.FINEST, "Bad Response : "+poolInfo);
						statsPanel.setByteBufferPoolInfo(0,0);
					}
				} catch(Exception e) {
					logger.log(Level.WARNING, "Exception : "+e);
					e.printStackTrace();
				}				
			}
			try {
				Thread.sleep(100);
			} catch(InterruptedException e){
				logger.log(Level.WARNING, "InterruptedException : "+e);
			} catch(Exception e){
				logger.log(Level.WARNING, "Exception : "+e);
			}
		}
	}
}
