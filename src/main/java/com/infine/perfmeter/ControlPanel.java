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

public class ControlPanel extends JPanel {

    TimeControl timeControl = null;

    /**
     * This is the default constructor
     */
    public ControlPanel(MainFrame mainFrame) {
        super();
        initialize(mainFrame);
    }

    void setConfig(SimpleGraphConfigurator graphConfig, PluginConfigurator pluginConfig) {
        timeControl.setConfig(graphConfig, pluginConfig);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize(MainFrame mainFrame) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(350, 25);
        this.setPreferredSize(new java.awt.Dimension(350, 25));
        this.setMaximumSize(new java.awt.Dimension(32767, 25));
        timeControl = new TimeControl(mainFrame, getHeight());
        this.add(timeControl, null);
    }

    public void setGraphWidth(int graphWidth) {
        timeControl.setGraphWidth(graphWidth);
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
