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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;


public class ConfiguratorPanel extends JPanel {
    static class NodeInfo {
        final String title;

        final JPanel panel;

        NodeInfo(String title, JPanel panel) {
            this.title = title;
            this.panel = panel;
        }

        public String toString() {
            return title;
        }
    }

    private JList jList = null;

    private JPanel jSouthPanel = null;

    private JPanel jWestPanel = null;

    private JPanel jCenterPanel = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    private JButton jButtonApply = null;

    protected boolean mustClose;

    final Configurator configurators[];

    private JLabel jTitleLabel = null;
    JPanel lastPanel = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JButton jButton = null;

    private JOptionPane jOptionPane = null;  //  @jve:decl-index=0:visual-constraint="571,195"

    /**
     * This is the default constructor
     */
    public ConfiguratorPanel(Configurator configurators[]) {
        super();
        this.configurators = configurators;
        initialize();
        map();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(5);
        borderLayout.setVgap(5);
        this.setLayout(borderLayout);
        this.setSize(new java.awt.Dimension(357, 234));

        this.add(new JLabel("Category"), BorderLayout.NORTH);

        jWestPanel = new JPanel();
        jWestPanel.setLayout(new BorderLayout());
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < configurators.length; i++)
            listModel.addElement(new NodeInfo(configurators[i].getDescription(), configurators[i].getPanel()));
        jList = new JList(listModel);
        jList.setSelectedIndex(0);
        jList.setBorder(BorderFactory.createLineBorder(java.awt.SystemColor.controlShadow, 1));
        jList.setPreferredSize(new java.awt.Dimension(110, 200));
        jWestPanel.add(jList, java.awt.BorderLayout.CENTER);
        jList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    selectListItemAction();
            }
        });
        this.add(jWestPanel, BorderLayout.WEST);
        jSouthPanel = new JPanel();
        jSouthPanel.setLayout(new BorderLayout());
        this.add(jSouthPanel, BorderLayout.SOUTH);
        jCenterPanel = new JPanel();
        jCenterPanel.setLayout(new BorderLayout());
        jTitleLabel = new JLabel();
        jTitleLabel.setText("JLabel");
        jTitleLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
        jTitleLabel.setForeground(java.awt.SystemColor.activeCaptionText);
        jTitleLabel.setOpaque(true);
        jTitleLabel.setBackground(new java.awt.Color(153, 153, 153));
        jCenterPanel.add(jTitleLabel, java.awt.BorderLayout.NORTH);
        this.add(jCenterPanel, java.awt.BorderLayout.CENTER);
        jSouthPanel.add(getJPanel(), java.awt.BorderLayout.WEST);
        jSouthPanel.add(getJPanel1(), java.awt.BorderLayout.EAST);
        selectListItemAction();
    }

    /**
     * This method initializes jList
     *
     * @return javax.swing.JList
     */
    private void actionOk() {
        for (int i = 0; i < configurators.length; i++)
            configurators[i].actionOk();
        firePropertyChange("configuration", configurators, this);
    }

    private void actionCancel() {
        for (int i = 0; i < configurators.length; i++)
            configurators[i].actionCancel();
        firePropertyChange("configuration", this, null);
    }

    private void actionApply() {
        for (int i = 0; i < configurators.length; i++)
            configurators[i].actionApply();
        firePropertyChange("configuration", null, this);

    }

    private void selectListItemAction() {
        int i = jList.getSelectedIndex();
        jTitleLabel.setText(configurators[i].getDescription());
        if (lastPanel != null) {
            jCenterPanel.remove(lastPanel);
            lastPanel.setVisible(false);
        }
        jCenterPanel.add(configurators[i].getPanel(), java.awt.BorderLayout.CENTER, 1);
        lastPanel = configurators[i].getPanel();
        configurators[i].getPanel().setVisible(true);
        jCenterPanel.revalidate();
        jCenterPanel.repaint();
    }

    private void map() {
        for (int i = 0; i < configurators.length; i++)
            configurators[i].map();
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
            jPanel = new JPanel();
            jPanel.setLayout(flowLayout1);
            jButton = new JButton();
            jButton.setText("Save as Defaults");
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        for (int i = 0; i < configurators.length; i++)
                            Util.saveAsDefaults(configurators[i]);
                    } catch (Exception exception) {
                        jOptionPane = new JOptionPane(exception.getMessage());
                        jOptionPane.setMessageType(javax.swing.JOptionPane.ERROR_MESSAGE);
                        jOptionPane.setVisible(true);
                    }

                }
            });

            jPanel.add(jButton, null);
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
            FlowLayout flowLayout2 = new FlowLayout();
            flowLayout2.setAlignment(java.awt.FlowLayout.RIGHT);
            jPanel1 = new JPanel();
            jButtonOk = new JButton();
            jButtonOk.setText("Ok");
            jButtonOk.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    actionOk();
                }
            });
            jButtonCancel = new javax.swing.JButton();
            jButtonCancel.setText("Cancel");
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    actionCancel();
                }
            });
            jButtonApply = new JButton();
            jButtonApply.setText("Apply");
            jPanel1.setLayout(flowLayout2);
            jPanel1.add(jButtonOk, null);
            jPanel1.add(jButtonCancel, null);
            jPanel1.add(jButtonApply, null);
            jButtonApply.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    actionApply();
                }
            });
        }
        return jPanel1;
    }


}  //  @jve:decl-index=0:visual-constraint="10,2"
