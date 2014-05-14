/*
 * Copyright (c) 2004, Antoine Ramponi
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution. 
 * Neither the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY 
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.infine.perfmeter;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

/**
 * @author tony
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AboutFrame extends JFrame implements HyperlinkListener {

    private javax.swing.JPanel jContentPane = null;

    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JPanel jPanel2 = null;
    private JButton jButton = null;
    private JPanel jPanel3 = null;
    private JEditorPane htmlPane = null;

    /**
     * This is the default constructor
     */
    public AboutFrame() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(419, 267);
        this.setContentPane(getJContentPane());
        this.setTitle("About JPerfmeter");
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            BorderLayout borderLayout2 = new BorderLayout();
            JLabel jLabel = new JLabel();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(borderLayout2);
            jLabel.setIcon(new ImageIcon(getClass().getResource("/icon32x32.png")));
            borderLayout2.setHgap(10);
            borderLayout2.setVgap(10);
            jContentPane.add(jLabel, java.awt.BorderLayout.WEST);
            jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJPanel3(), java.awt.BorderLayout.EAST);
            jContentPane.add(getJPanel2(), java.awt.BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.add(getJButton(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridLayout gridLayout1 = new GridLayout();
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout1);
            StringBuffer sb = new StringBuffer("<html>");
            sb.append("JPerfmeter version <b>");
            sb.append(Util.getVersion());
            sb.append("</b><br><br>");
            sb.append(Util.getBuildInfo());
            sb.append(" <a href=\"http://remotetea.sourceforge.net/\">remotetea</a><br><br>");
            sb.append("Copyright (c) 2007 - <a href=\"http://jperfmeter.sourceforge.net\">in fine consulting</a><br><br>");
            sb.append("Author <a href=\"mailto:jperfmeter@infineconsulting.com\">Antoine Ramponi</a><br><br>");
            sb.append("</html>");
            htmlPane = new JEditorPane("text/html", sb.toString());

            htmlPane.setEditable(false);
            htmlPane.addHyperlinkListener(this);
            JScrollPane scrollPane = new JScrollPane(htmlPane);
            jPanel1.add(scrollPane, BorderLayout.CENTER);

            gridLayout1.setRows(1);
            gridLayout1.setHgap(10);
            gridLayout1.setVgap(10);
            gridLayout1.setColumns(1);
        }
        return jPanel1;
    }


    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            Util.displayURL(event.getURL());
        }
    }


    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
        }
        return jPanel2;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Ok");
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AboutFrame.this.dispose();
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes jPanel3
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            jPanel3 = new JPanel();
        }
        return jPanel3;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
