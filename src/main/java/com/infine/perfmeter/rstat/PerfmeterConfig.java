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
package com.infine.perfmeter.rstat;

import com.infine.perfmeter.GraphModel;
import com.infine.perfmeter.PluginConfigurator;
import com.infine.perfmeter.TimeStampGraphModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

public class PerfmeterConfig extends JPanel implements PluginConfigurator {

    static final long serialVersionUID = 6141892085620792882L;

    private int sampleTime = 2;
    private int sampleHistory = 1000;
    private boolean mustClose = false;
    DefaultListModel hosts = new DefaultListModel();
    RstatPlugin plugin = null;
    boolean checkBoxesStates[] = null;
    private JPanel jPanel = null;
    private JPanel jPanel2 = null;
    private JCheckBox jCheckBoxCPU = null;
    private JCheckBox jCheckBoxLoad = null;
    private JCheckBox jCheckBoxDisk = null;
    private JCheckBox jCheckBoxPage = null;
    private JCheckBox jCheckBoxContext = null;
    private JCheckBox jCheckBoxSwap = null;
    private JCheckBox jCheckBoxInterrupts = null;
    private JCheckBox jCheckBoxPackets = null;
    private JCheckBox jCheckBoxCollisions = null;
    private JCheckBox jCheckBoxErrors = null;
    private JCheckBox jCheckBoxes[] = null;
    private JPanel jPanel4 = null;
    private JPanel jPanel5 = null;
    private JPanel jPanel6 = null;
    private JLabel jLabel = null;
    private JComboBox jComboBoxHost = null;
    private JButton jButtonAdd = null;
    private JPanel jPanel7 = null;
    private JList jListHost = null;
    private JPanel jPanel8 = null;
    private JButton jButtonUp = null;
    private JButton jButtonRemove = null;
    private JButton jButtonDown = null;
    private JScrollPane jScrollPane = null;
    private JPanel jPanel9 = null;
    private JLabel jLabel1 = null;
    private JTextField jTextField = null;
    private JLabel jLabel2 = null;
    private JTextField jTextFieldSampleHistory = null;

    /**
     * This is the default constructor for readExternal
     */
    public PerfmeterConfig() {
        super();
        initialize();
    }

    public PerfmeterConfig(RstatPlugin plugin) {
        super();
        this.plugin = plugin;
        initialize();
        map();

    }

    public void initFromArgs(String args[]) {
        hosts.clear();
        for (int i = 0; i < args.length; i++)
            hosts.addElement(args[i]);
    }

//    public void map(PerfmeterConfig pc) {
//        hosts.clear();
//        String tmp[] = pc.getHosts();
//        for (int i = 0; i < tmp.length; i++)
//            hosts.addElement(tmp[i]);
//        plugin.configurationChange();
//    }

    public void setPlugin(RstatPlugin plugin) {
        this.plugin = plugin;
        plugin.configurationChange();
    }

    public void map() {
        hosts.clear();
        Iterator it = plugin.getHosts().iterator();
        while (it.hasNext())
            hosts.addElement(it.next());

        checkBoxesStates = new boolean[jCheckBoxes.length];
        for (int i = 0; i < jCheckBoxes.length; i++)
            checkBoxesStates[i] = jCheckBoxes[i].isSelected();
        getJTextFieldSampleTime().setText(Integer.toString(sampleTime));
        getJTextFieldSampleHistory().setText(Integer.toString(sampleHistory));
    }

    public String[] getHosts() {
        String ret[] = new String[hosts.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = (String) hosts.get(i);
        return ret;
    }

    public void refresh() {
        map();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        int hostsSize = in.readInt();
        hosts.clear();
        for (int i = 0; i < hostsSize; i++)
            hosts.addElement(in.readObject());
        checkBoxesStates = (boolean[]) in.readObject();
        for (int i = 0; i < checkBoxesStates.length; i++)
            jCheckBoxes[i].setSelected(checkBoxesStates[i]);
        sampleTime = in.readInt();
        sampleHistory = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        //  current stream object version
        out.writeInt(1);
        out.writeInt(hosts.getSize());
        for (int i = 0; i < hosts.getSize(); i++)
            out.writeObject(hosts.get(i));
        out.writeObject(checkBoxesStates);
        out.writeInt(sampleTime);
        out.writeInt(sampleHistory);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getJPanel(), java.awt.BorderLayout.NORTH);
        //this.add(getJPanel1(), java.awt.BorderLayout.SOUTH);
        this.add(getJPanel4(), java.awt.BorderLayout.CENTER);
        this.setSize(300, 319);
    }

    /**
     * This method initializes jPanel
     *
     * @return JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel2
     *
     * @return JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            java.awt.GridLayout layGridLayout2 = new java.awt.GridLayout();
            layGridLayout2.setRows(3);
            layGridLayout2.setColumns(4);
            jPanel2.setLayout(layGridLayout2);
            jPanel2.add(getJCheckBoxCPU(), null);
            jPanel2.add(getJCheckBoxLoad(), null);
            jPanel2.add(getJCheckBoxDisk(), null);
            jPanel2.add(getJCheckBoxPage(), null);
            jPanel2.add(getJCheckBoxContext(), null);
            jPanel2.add(getJCheckBoxSwap(), null);
            jPanel2.add(getJCheckBoxInterrupts(), null);
            jPanel2.add(getJCheckBoxPackets(), null);
            jPanel2.add(getJCheckBoxCollisions(), null);
            jPanel2.add(getJCheckBoxErrors(), null);
            jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Graph", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.setPreferredSize(new java.awt.Dimension(334, 100));
            jPanel2.setMaximumSize(new java.awt.Dimension(32767, 100));
            jPanel2.setMinimumSize(new java.awt.Dimension(334, 100));
            jCheckBoxes =
                    new JCheckBox[]{
                            getJCheckBoxCPU(),
                            getJCheckBoxLoad(),
                            getJCheckBoxDisk(),
                            getJCheckBoxPage(),
                            getJCheckBoxContext(),
                            getJCheckBoxSwap(),
                            getJCheckBoxInterrupts(),
                            getJCheckBoxPackets(),
                            getJCheckBoxCollisions(),
                            getJCheckBoxErrors()};

        }
        return jPanel2;
    }

    /**
     * This method initializes jCheckBoxCPU
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxCPU() {
        if (jCheckBoxCPU == null) {
            jCheckBoxCPU = new JCheckBox();
            jCheckBoxCPU.setText("CPU");
            jCheckBoxCPU.setSelected(true);
        }
        return jCheckBoxCPU;
    }

    /**
     * This method initializes jCheckBoxLoad
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxLoad() {
        if (jCheckBoxLoad == null) {
            jCheckBoxLoad = new JCheckBox();
            jCheckBoxLoad.setText("Load");
            jCheckBoxLoad.setSelected(true);
        }
        return jCheckBoxLoad;
    }

    /**
     * This method initializes jCheckBoxDisk
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxDisk() {
        if (jCheckBoxDisk == null) {
            jCheckBoxDisk = new JCheckBox();
            jCheckBoxDisk.setText("Disk");
            jCheckBoxDisk.setSelected(true);
        }
        return jCheckBoxDisk;
    }

    /**
     * This method initializes jCheckBoxPage
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxPage() {
        if (jCheckBoxPage == null) {
            jCheckBoxPage = new JCheckBox();
            jCheckBoxPage.setText("Page");
            jCheckBoxPage.setSelected(true);
        }
        return jCheckBoxPage;
    }

    /**
     * This method initializes jCheckBoxContext
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxContext() {
        if (jCheckBoxContext == null) {
            jCheckBoxContext = new JCheckBox();
            jCheckBoxContext.setText("Context");
            jCheckBoxContext.setSelected(true);
        }
        return jCheckBoxContext;
    }

    /**
     * This method initializes jCheckBoxSwap
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxSwap() {
        if (jCheckBoxSwap == null) {
            jCheckBoxSwap = new JCheckBox();
            jCheckBoxSwap.setText("Swap");
        }
        return jCheckBoxSwap;
    }

    /**
     * This method initializes jCheckBoxInterrupts
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxInterrupts() {
        if (jCheckBoxInterrupts == null) {
            jCheckBoxInterrupts = new JCheckBox();
            jCheckBoxInterrupts.setText("Interrupts");
            jCheckBoxInterrupts.setSelected(true);
        }
        return jCheckBoxInterrupts;
    }

    /**
     * This method initializes jCheckBoxPackets
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxPackets() {
        if (jCheckBoxPackets == null) {
            jCheckBoxPackets = new JCheckBox();
            jCheckBoxPackets.setText("Packets");
            jCheckBoxPackets.setSelected(true);
        }
        return jCheckBoxPackets;
    }

    /**
     * This method initializes jCheckBoxCollisions
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxCollisions() {
        if (jCheckBoxCollisions == null) {
            jCheckBoxCollisions = new JCheckBox();
            jCheckBoxCollisions.setText("Collisions");
        }
        return jCheckBoxCollisions;
    }

    /**
     * This method initializes jCheckBoxErrors
     *
     * @return JCheckBox
     */
    private JCheckBox getJCheckBoxErrors() {
        if (jCheckBoxErrors == null) {
            jCheckBoxErrors = new JCheckBox();
            jCheckBoxErrors.setText("Errors");
        }
        return jCheckBoxErrors;
    }

    /**
     * This method initializes jButtonOk
     *
     * @return JButton
     */
    public void actionOk() {
        applyChanges();
    }

    private void applyChanges() {
        try {
            sampleTime = Integer.parseInt(getJTextFieldSampleTime().getText());
            sampleTime = Math.max(sampleTime, 1);
            sampleHistory = Integer.parseInt(getJTextFieldSampleHistory().getText());
            sampleHistory = Math.max(sampleHistory, 100);
        } catch (Exception ex) {
        }
        plugin.configurationChange();
        map();
        //if (closeWindow) mustClose = true;
        //PerfmeterConfig.this.firePropertyChange("configuration", null, plugin);
        //if (closeWindow) mustClose = false;

    }

    public void actionApply() {
        applyChanges();
    }

    /**
     * This method initializes jButtonCancel
     *
     * @return JButton
     */
    public void actionCancel() {
        mustClose = true;
        PerfmeterConfig.this.firePropertyChange("configuration", null, null);
        // restore checkBoxes state
        for (int i = 0; i < jCheckBoxes.length; i++)
            jCheckBoxes[i].setSelected(checkBoxesStates[i]);

        mustClose = false;
    }

    /**
     * This method initializes jPanel4
     *
     * @return JPanel
     */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            jPanel4 = new JPanel();
            jPanel4.setLayout(new BoxLayout(getJPanel4(), BoxLayout.Y_AXIS));
            jPanel4.add(getJPanel9(), null);
            jPanel4.add(getJPanel2(), null);
            jPanel4.add(getJPanel5(), null);
        }
        return jPanel4;
    }

    /**
     * This method initializes jPanel5
     *
     * @return JPanel
     */
    private JPanel getJPanel5() {
        if (jPanel5 == null) {
            jPanel5 = new JPanel();
            jPanel5.setLayout(new java.awt.BorderLayout());
            jPanel5.add(getJPanel6(), java.awt.BorderLayout.NORTH);
            jPanel5.add(getJPanel7(), java.awt.BorderLayout.CENTER);
            jPanel5.setBorder(BorderFactory.createTitledBorder(null, "Hosts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        }
        return jPanel5;
    }

    /**
     * This method initializes jPanel6
     *
     * @return JPanel
     */
    private JPanel getJPanel6() {
        if (jPanel6 == null) {
            jPanel6 = new JPanel();
            jPanel6.setLayout(new java.awt.BorderLayout());
            jPanel6.add(getJLabel(), java.awt.BorderLayout.WEST);
            jPanel6.add(getJComboBoxHost(), java.awt.BorderLayout.CENTER);
            jPanel6.add(getJButtonAdd(), java.awt.BorderLayout.EAST);
            jPanel6.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        }
        return jPanel6;
    }

    /**
     * This method initializes jLabel
     *
     * @return JLabel
     */
    private JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new JLabel();
            jLabel.setText("Host");
        }
        return jLabel;
    }

    /**
     * This method initializes jComboBoxHost
     *
     * @return JComboBox
     */
    private JComboBox getJComboBoxHost() {
        if (jComboBoxHost == null) {
            jComboBoxHost = new JComboBox();
            jComboBoxHost.setEditable(true);
//            jComboBoxHost.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent e) {
//                    					addHost();
//                }
//            });
            jComboBoxHost.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    addHost();
                }
            });
        }
        return jComboBoxHost;
    }

    private void addHost() {
        if (!"".equals(getJComboBoxHost().getSelectedItem())) {
            String host = (String) getJComboBoxHost().getSelectedItem();
            if (!hosts.contains(host)) {
                hosts.addElement(host);
                getJListHost().ensureIndexIsVisible(hosts.getSize() - 1);
                getJComboBoxHost().getEditor().selectAll();
            }
            boolean found = false;
            for (int i = 0; i < getJComboBoxHost().getItemCount(); i++) {
                if (host.equals(getJComboBoxHost().getItemAt(i)))
                    found = true;
            }
            if (!found)
                getJComboBoxHost().addItem(host);
        }

    }

    /**
     * This method initializes jButtonAdd
     *
     * @return JButton
     */
    private JButton getJButtonAdd() {
        if (jButtonAdd == null) {
            jButtonAdd = new JButton();
            jButtonAdd.setText("Add");
            jButtonAdd.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonAdd.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonAdd.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addHost();
                }

            });
        }
        return jButtonAdd;
    }

    /**
     * This method initializes jPanel7
     *
     * @return JPanel
     */
    private JPanel getJPanel7() {
        if (jPanel7 == null) {
            jPanel7 = new JPanel();
            jPanel7.setLayout(new java.awt.BorderLayout());
            jPanel7.add(getJPanel8(), java.awt.BorderLayout.EAST);
            jPanel7.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jPanel7;
    }

    /**
     * This method initializes jListHost
     *
     * @return JList
     */
    private JList getJListHost() {
        if (jListHost == null) {
            jListHost = new JList();
            jListHost.setVisibleRowCount(5);
            jListHost.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jListHost.setModel(hosts);
            jListHost.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListHost.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == 127)
                        remove();

                }
            });
        }
        return jListHost;
    }

    /**
     * This method initializes jPanel8
     *
     * @return JPanel
     */
    private JPanel getJPanel8() {
        if (jPanel8 == null) {
            jPanel8 = new JPanel();
            jPanel8.setLayout(new BoxLayout(jPanel8, BoxLayout.Y_AXIS));
            jPanel8.add(getJButtonRemove(), null);
            jPanel8.add(getJButtonUp(), null);
            jPanel8.add(getJButtonDown(), null);
            jPanel8.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        }
        return jPanel8;
    }

    /**
     * This method initializes jButtonUp
     *
     * @return JButton
     */
    private JButton getJButtonUp() {
        if (jButtonUp == null) {
            jButtonUp = new JButton();
            jButtonUp.setText("Up");
            jButtonUp.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonUp.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonUp.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonUp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int idx = getJListHost().getSelectedIndex();
                    if (idx > 0) {
                        String tmp = (String) hosts.getElementAt(idx);
                        hosts.setElementAt(hosts.getElementAt(idx - 1), idx);
                        hosts.setElementAt(tmp, idx - 1);
                        getJListHost().setSelectedIndex(idx - 1);
                    }
                }
            });
        }
        return jButtonUp;
    }

    /**
     * This method initializes jButtonRemove
     *
     * @return JButton
     */
    private JButton getJButtonRemove() {
        if (jButtonRemove == null) {
            jButtonRemove = new JButton();
            jButtonRemove.setText("Remove");
            jButtonRemove.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonRemove.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonRemove.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    remove();
                }
            });
        }
        return jButtonRemove;
    }

    /**
     * This method initializes jButtonDown
     *
     * @return JButton
     */
    private JButton getJButtonDown() {
        if (jButtonDown == null) {
            jButtonDown = new JButton();
            jButtonDown.setText("Down");
            jButtonDown.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonDown.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonDown.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonDown.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int idx = getJListHost().getSelectedIndex();
                    if (idx < hosts.getSize() - 1) {
                        String tmp = (String) hosts.getElementAt(idx);
                        hosts.setElementAt(hosts.getElementAt(idx + 1), idx);
                        hosts.setElementAt(tmp, idx + 1);
                        getJListHost().setSelectedIndex(idx + 1);
                    }
                }
            });
        }
        return jButtonDown;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJListHost());
        }
        return jScrollPane;
    }

    public JPanel getPanel() {
        return this;
    }

    public int columns() {
        return hosts.size();
    }

    public GraphModel getModel(int row, int column) {
        int rows = rows();
        if (rows == 0)
            throw new ArrayIndexOutOfBoundsException(rows + "==" + 0);
        int i = 0;
        for (int computedRow = 0; i < jCheckBoxes.length; i++)
            if (jCheckBoxes[i].isSelected()) {
                if (computedRow == row)
                    break;
                computedRow++;
            }

        return plugin.getModel(column, i);

    }

    public TimeStampGraphModel getTimeStamp(int row) {
        return plugin.getTimeStamp(row);
    }

    public int rows() {
        int res = 0;
        for (int i = 0; i < jCheckBoxes.length; i++)
            res += jCheckBoxes[i].isSelected() ? 1 : 0;
        return res;
    }

    public boolean mustClose() {
        return mustClose;
    }

    public int getSampleTime() {
        return sampleTime;
    }

    public int getSampleHistory() {
        return sampleHistory;
    }

    public String getName() {
        return "RStat";
    }

    public String getDescription() {
        return "RStat Plugin";
    }

    private void remove() {
        int idx = getJListHost().getSelectedIndex();
        if (idx >= 0)
            hosts.remove(idx);
        getJListHost().setSelectedIndex(idx >= hosts.getSize() - 1 ? hosts.getSize() - 1 : idx);
    }

    /**
     * This method initializes jPanel9
     *
     * @return JPanel
     */
    private JPanel getJPanel9() {
        if (jPanel9 == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("Sample History");
            jLabel1 = new JLabel();
            jLabel1.setText("Sample Time (second)");
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            gridLayout.setHgap(0);
            gridLayout.setVgap(0);
            gridLayout.setColumns(2);
            jPanel9 = new JPanel();
            jPanel9.setMaximumSize(new Dimension(32767, 66));
            jPanel9.setPreferredSize(new java.awt.Dimension(264, 66));
            jPanel9.setBorder(BorderFactory.createTitledBorder(null, "Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            jPanel9.setLayout(gridLayout);
            jPanel9.add(jLabel1, null);
            jPanel9.add(getJTextFieldSampleTime(), null);
            jPanel9.add(jLabel2, null);
            jPanel9.add(getJTextFieldSampleHistory(), null);
        }
        return jPanel9;
    }

    /**
     * This method initializes jTextField
     *
     * @return JTextField
     */
    private JTextField getJTextFieldSampleTime() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setMinimumSize(new java.awt.Dimension(4, 16));
            jTextField.setPreferredSize(new java.awt.Dimension(4, 16));
        }
        return jTextField;
    }

    /**
     * This method initializes jTextField1
     *
     * @return JTextField
     */
    private JTextField getJTextFieldSampleHistory() {
        if (jTextFieldSampleHistory == null) {
            jTextFieldSampleHistory = new JTextField();
        }
        return jTextFieldSampleHistory;
    }


} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
