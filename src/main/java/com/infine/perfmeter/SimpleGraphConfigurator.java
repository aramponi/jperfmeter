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
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 * @author ARAMPONI
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleGraphConfigurator extends JPanel implements Configurator {

    static final long serialVersionUID = -2265360255850381468L;

    private JPanel jPanel10 = null;
    private JLabel jLabelColor1 = null;
    private JButton jButtonColor1 = null;
    private JLabel jLabelColor2 = null;
    private JButton jButtonColor2 = null;
    private JLabel jLabelColor3 = null;
    private JButton jButtonColor3 = null;
    private JLabel jLabelBackground = null;
    private JButton jButtonBackground = null;
    private JLabel jLabelText = null;
    private JButton jButtonGrid = null;
    private JLabel jLabeGrid = null;
    private JButton jButtonText = null;
    private JLabel jLabelTimeout = null;
    private JButton jButtonTimeout = null;
    private JCheckBox jCheckBoxShareScale = null;
    private JButton jButtonRestoreDefaults = null;
    private JPanel jPanel1 = null;

    /**
     * This is the default constructor
     */
    public SimpleGraphConfigurator() {
        super();
        initialize();
    }

    static Colors defaults = new Colors();
    Colors colors = defaults;
    boolean vertical = true;
    boolean shareScale = false;

    private JPanel jPanel2 = null;

    private JRadioButton jRadioButtonHorizontal = null;

    private JRadioButton jRadioButtonVertical = null;

    static public class Colors implements Serializable {
        static final long serialVersionUID = -7695885325902202147L;
        public Color color1 = new Color(187, 32, 32);
        public Color color2 = new Color(38, 164, 69);
        public Color color3 = new Color(255, 204, 0);
        public Color color4 = new Color(51, 153, 51);
        public Color background = new Color(0, 0, 0);
        ;
        public Color text = new Color(232, 232, 232);
        public Color grid = new Color(80, 80, 80);
        public Color timeout = new Color(232, 232, 232);
        //public Color graph = new Color(80, 80, 80);

        transient public Color[][] colors = null;

        public Color[][] getColorsAsArray() {
            if (colors == null) {
                colors = new Color[][]{new Color[]{},
                        new Color[]{color1},
                        new Color[]{color1, color2},
                        new Color[]{color1, color2, color3},
                        new Color[]{color1, color2, color3, color4}};
            }
            return colors;
        }
    }

    public Colors getColors() {
        return colors;
    }

    public JPanel getPanel() {
        return this;
    }

    public String getName() {
        return "SimpleGraph";
    }

    public String getDescription() {
        return "Simple Graph";
    }

    public void actionOk() {
        colors.color1 = getJButtonColor1().getBackground();
        colors.color2 = getJButtonColor2().getBackground();
        colors.color3 = getJButtonColor3().getBackground();
        //colors.color4 = getJButtonColor4().getBackground();
        colors.background = getJButtonBackground().getBackground();
        colors.text = getJButtonText().getBackground();
        colors.grid = getJButtonGrid().getBackground();
        colors.timeout = getJButtonTimeout().getBackground();
        vertical = jRadioButtonVertical.isSelected();
        shareScale = jCheckBoxShareScale.isSelected();
    }

    public void actionCancel() {

    }

    public void actionApply() {
        actionOk();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(300, 306);
        //this.add(getJPanel(), null);
        this.setMaximumSize(new java.awt.Dimension(32767, 306));
        this.add(getJPanel2(), null);
        this.add(getJPanel10(), null);
        this.add(getJPanel1(), null);
    }

    /**
     * This method initializes jPanel10
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel10() {
        if (jPanel10 == null) {
            jLabelText = new JLabel();
            jLabelBackground = new JLabel();
            jLabelColor3 = new JLabel();
            jLabelColor2 = new JLabel();
            jLabelColor1 = new JLabel();
            jLabeGrid = new JLabel();
            jLabelTimeout = new JLabel();
            GridLayout gridLayout1 = new GridLayout();
            jPanel10 = new JPanel();
            jPanel10.setLayout(gridLayout1);
            jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Colors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jLabelColor1.setText("Color1");
            gridLayout1.setRows(7);
            gridLayout1.setColumns(2);
            jLabelColor2.setText("Color2");
            jLabelColor3.setText("Color3");
            jLabelBackground.setText("Background");
            jLabelText.setText("Text");
            jLabelTimeout.setText("Timeout");
            jPanel10.setMaximumSize(new java.awt.Dimension(32767, 160));
            jPanel10.setMinimumSize(new java.awt.Dimension(146, 160));
            jPanel10.setPreferredSize(new java.awt.Dimension(146, 160));
            jLabeGrid.setText("Grid");
            jPanel10.add(jLabelColor1, null);
            jPanel10.add(getJButtonColor1(), null);
            jPanel10.add(jLabelColor2, null);
            jPanel10.add(getJButtonColor2(), null);
            jPanel10.add(jLabelColor3, null);
            jPanel10.add(getJButtonColor3(), null);
            jPanel10.add(jLabelBackground, null);
            jPanel10.add(getJButtonBackground(), null);
            jPanel10.add(jLabeGrid, null);
            jPanel10.add(getJButtonGrid(), null);
            jPanel10.add(jLabelText, null);
            jPanel10.add(getJButtonText(), null);
            jPanel10.add(jLabelTimeout, null);
            jPanel10.add(getJButtonTimeout(), null);
        }
        return jPanel10;
    }

    /**
     * This method initializes jButtonColor1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonColor1() {
        if (jButtonColor1 == null) {
            jButtonColor1 = new JButton();
            jButtonColor1.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor1.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor1.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor1);
                }
            });

        }
        return jButtonColor1;
    }

    /**
     * This method initializes jButtonColor2
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonColor2() {
        if (jButtonColor2 == null) {
            jButtonColor2 = new JButton();
            jButtonColor2.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor2.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor2.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor2);
                }
            });
        }
        return jButtonColor2;
    }

    /**
     * This method initializes jButtonColor3
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonColor3() {
        if (jButtonColor3 == null) {
            jButtonColor3 = new JButton();
            jButtonColor3.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor3.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor3.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor3);
                }
            });
        }
        return jButtonColor3;
    }

    /**
     * This method initializes jButtonBackground
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonBackground() {
        if (jButtonBackground == null) {
            jButtonBackground = new JButton();
            jButtonBackground.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonBackground.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonBackground.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonBackground.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonBackground);
                }
            });
        }
        return jButtonBackground;
    }

    /**
     * This method initializes jButtonTimeout
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonTimeout() {
        if (jButtonTimeout == null) {
            jButtonTimeout = new JButton();
            jButtonTimeout.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonTimeout.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonTimeout.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonTimeout.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonTimeout);
                }
            });
        }
        return jButtonTimeout;
    }

    /**
     * This method initializes jButtonGrid
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonGrid() {
        if (jButtonGrid == null) {
            jButtonGrid = new JButton();
            jButtonGrid.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonGrid.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonGrid.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonGrid.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonGrid);
                }
            });
        }
        return jButtonGrid;
    }

    /**
     * This method initializes jButtonText
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonText() {
        if (jButtonText == null) {
            jButtonText = new JButton();
            jButtonText.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonText.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonText.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonText.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonText);
                }
            });
        }
        return jButtonText;
    }

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private void setButtonColor(JButton button) {
        Color newColor = JColorChooser.showDialog(SimpleGraphConfigurator.this, "Colors", button.getBackground());
        if (newColor != null)
            button.setBackground(newColor);
    }

    /**
     * This method initializes jButtonRestoreDefaults
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonRestoreDefaults() {
        if (jButtonRestoreDefaults == null) {
            jButtonRestoreDefaults = new JButton("Restore Default Colors");
            jButtonRestoreDefaults.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mapColorsToButtons(defaults);
                }
            });
        }
        return jButtonRestoreDefaults;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            jPanel1 = new JPanel();
            jPanel1.setLayout(flowLayout);
            jPanel1.add(getJButtonRestoreDefaults(), null);
        }
        return jPanel1;
    }

    private void mapColorsToButtons(Colors colors) {
        getJButtonColor1().setBackground(colors.color1);
        getJButtonColor2().setBackground(colors.color2);
        getJButtonColor3().setBackground(colors.color3);
        getJButtonBackground().setBackground(colors.background);
        getJButtonGrid().setBackground(colors.grid);
        getJButtonText().setBackground(colors.text);
        getJButtonTimeout().setBackground(colors.timeout);
    }

    public void map() {
        mapColorsToButtons(colors);
        jRadioButtonHorizontal.setSelected(!vertical);
        jRadioButtonVertical.setSelected(vertical);
        jCheckBoxShareScale.setSelected(shareScale);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        colors = (Colors) in.readObject();
        vertical = in.readBoolean();
        shareScale = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeObject(colors);
        out.writeBoolean(vertical);
        out.writeBoolean(shareScale);
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            JLabel jLabel = new JLabel();
            jLabel.setText("Direction");
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(3);
            gridLayout2.setColumns(2);
            jPanel2 = new JPanel();
            jPanel2.setMaximumSize(new java.awt.Dimension(32767, 110));
            jPanel2.setMinimumSize(new java.awt.Dimension(238, 110));
            jPanel2.setPreferredSize(new java.awt.Dimension(238, 85));
            jPanel2.setLayout(gridLayout2);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.add(jLabel, null);
            jRadioButtonHorizontal = new JRadioButton();
            jRadioButtonHorizontal.setText("Horizontal");
            jPanel2.add(jRadioButtonHorizontal, null);
            jLabel = new JLabel();
            jPanel2.add(jLabel, null);
            jRadioButtonVertical = new JRadioButton();
            jRadioButtonVertical.setText("Vertical");
            jPanel2.add(jRadioButtonVertical, null);
            ButtonGroup group = new ButtonGroup();
            group.add(jRadioButtonHorizontal);
            group.add(jRadioButtonVertical);
            jLabel = new JLabel();
            jLabel.setText("Scale");
            jPanel2.add(jLabel, null);
            jCheckBoxShareScale = new JCheckBox();
            jCheckBoxShareScale.setMaximumSize(new java.awt.Dimension(21, 16));
            jCheckBoxShareScale.setPreferredSize(new java.awt.Dimension(21, 16));
            jCheckBoxShareScale.setText("Share the same Scale");
            jCheckBoxShareScale.setMinimumSize(new java.awt.Dimension(21, 16));
            jPanel2.add(jCheckBoxShareScale, null);

        }
        return jPanel2;
    }

    public boolean isShareScale() {
        return shareScale;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void initFromArgs(String args[]) {
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
