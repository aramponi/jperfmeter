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

import com.infine.perfmeter.csv.StreamWriter;
import com.infine.perfmeter.csv.StreamWriterConfigurator;
import com.infine.perfmeter.rstat.PerfmeterConfig;
import com.infine.perfmeter.rstat.RstatPlugin;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;


public class MainFrame extends JFrame {

    private static final String TITLE = "JPerfmeter";
    final Plugin plugin;
    DefaultGraphModel model = null;
    private JPanel graphPanel = null;
    ArrayList graphs = new ArrayList();
    ArrayList streams = new ArrayList();
    Window configWindow = null;
    int oldRows, oldColumns;
    final int defaultColumnSize = 130;
    final int defaultRowSize = 100;
    JFileChooser fc = null;
    static final String JPF_EXT = ".jpf";
    static int windows = 0;
    File lastFile = null;
    boolean modified = false;
    SimpleGraphConfigurator graphConfig = null;
    StreamWriterConfigurator streamConfig = null;
    boolean currentDirection;
    boolean showControls = true;
    ControlPanel controlPanel = null;
    JPanel mainPanel = null;
    JPopupMenu jPopupMenu = null;
    JMenuItem jMenuItemShowHide = null;

    class JPFFileFilter extends FileFilter {

        public boolean accept(File f) {
            return f.getName().endsWith(JPF_EXT) || f.isDirectory();
        }

        public String getDescription() {
            return "JPerfmeter Configuration";
        }
    }


    /**
     * This is the default constructor
     */
    public MainFrame(Plugin plugin, SimpleGraphConfigurator graphConfig, StreamWriterConfigurator streamConfig) {
        super();
        setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon32x32.png")));
        this.plugin = plugin;
        this.graphConfig = graphConfig;
        this.streamConfig = streamConfig;
        if (graphConfig != null)
            currentDirection = graphConfig.isVertical();
        initialize();
    }

//    public MainFrame(Plugin plugin, File file) throws IOException, ClassNotFoundException {
//        super();
//        setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon32x32.png")));
//        this.plugin = plugin;
//        loadConfiguration(file);
//        initialize();
//    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        setContentPane(getMainPanel());
        setSize(300, 200);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) && e.getKeyCode() == 78)
                    newWindow();
                else if (((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) && e.getKeyCode() == 79)
                    loadConfiguration();
                else if (modified && ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) && e.getKeyCode() == 83)
                    saveConfiguration(lastFile);
                else if (((e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) && e.getKeyCode() == 67)
                    openCongifurator();
                super.keyPressed(e);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (graphs.size() > 0)
                    controlPanel.setGraphWidth(((Component) graphs.get(0)).getWidth());
            }
        });

        setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (isUserOkToLeave()) {
                    MainFrame.this.dispose();
                    if (--windows <= 0)
                        System.exit(1);
                    else
                        plugin.stop();
                }
            }
        });
        windows++;
        setModified(null, false);
        draw();
    }

    void draw() {
        PluginConfigurator configurator = plugin.getPluginConfigurator();
        graphPanel.removeAll();
        for (int i = 0; i < graphs.size(); i++)
            ((SimpleGraph) graphs.get(i)).stop();
        int columnSize;
        int rowSize;

        if (oldColumns != 0 && oldRows != 0) {
            columnSize = getSize().width / oldColumns;
            rowSize = getSize().height / oldRows;
            if (!currentDirection) {
                columnSize = getSize().height / oldColumns;
                rowSize = getSize().width / oldRows;
            }

        } else {
            columnSize = defaultColumnSize;
            rowSize = defaultRowSize;
        }

        if (showControls) {
            controlPanel.setConfig(graphConfig, configurator);
            controlPanel.setGraphWidth(columnSize);
            controlPanel.setVisible(true);
            controlPanel.validate();
        } else {
            controlPanel.setVisible(false);
        }


        graphs.clear();
        int rows = configurator.rows();
        int columns = configurator.columns();
        oldRows = rows;
        oldColumns = columns;
        currentDirection = graphConfig.isVertical();
        SimpleGraph graph = null;

        if (currentDirection) {
            ((GridLayout) graphPanel.getLayout()).setRows(rows);
            ((GridLayout) graphPanel.getLayout()).setColumns(columns);
            setSize(columnSize * columns, rowSize * rows);
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < columns; i++) {
                    graph = new SimpleGraph(configurator.getModel(j, i), configurator.getTimeStamp(i), graphConfig);
                    graphs.add(graph);
                    graphPanel.add(graph);
                }

            }
        } else {
            ((GridLayout) graphPanel.getLayout()).setRows(columns);
            ((GridLayout) graphPanel.getLayout()).setColumns(rows);
            setSize(rowSize * rows, columnSize * columns);
            //setSize(columnSize * columns, rowSize * rows);
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    graph = new SimpleGraph(configurator.getModel(j, i), configurator.getTimeStamp(i), graphConfig);
                    graphs.add(graph);
                    graphPanel.add(graph);
                }
            }
        }
        mainPanel.setBackground(graphConfig.getColors().background);
        graphPanel.setBackground(graphConfig.getColors().background);
        validate();
        // reconfigure streams
        for (int i = 0; i < streams.size(); i++)
            ((StreamWriter) streams.get(i)).stop();
        streams.clear();
        if (streamConfig.isActive())
            for (int i = 0; i < columns; i++)
                streams.add(StreamWriter.getInstance(configurator.getTimeStamp(i), streamConfig)); // pluged on timestamp

    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getGraphPanel() {
        if (graphPanel == null) {
            graphPanel = new JPanel();
            GridLayout layGridLayout1 = new GridLayout();
            layGridLayout1.setRows(1);
            layGridLayout1.setColumns(1);
            graphPanel.setLayout(layGridLayout1);
            graphPanel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }

                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        getJPopupMenu().show(e.getComponent(),
                                e.getX(), e.getY());
                    }
                }

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        openCongifurator();
                    }
                }
            });
        }
        return graphPanel;
    }

    private ControlPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new ControlPanel(this);
        }
        return controlPanel;
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(getControlPanel());
            mainPanel.add(getGraphPanel());
        }
        return mainPanel;
    }

    // return true 
    private boolean isUserOkToLeave() {
        boolean res = true;
        if (modified) {
            int n = JOptionPane.showOptionDialog(this,
                    "Save changes " + (lastFile != null ? "to" + lastFile : ""),
                    "JPerfmeter",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            switch (n) {
                case JOptionPane.CANCEL_OPTION:
                    res = false;
                    break;
                case JOptionPane.YES_OPTION:
                    if (lastFile == null)
                        saveConfiguration();
                    else
                        saveConfiguration(lastFile);
                case JOptionPane.NO_OPTION:
                    res = true;
            }
        }
        return res;
    }

    public void openCongifurator() {
        PluginConfigurator configurator = plugin.getPluginConfigurator();
        JDialog dialog = new JDialog(MainFrame.this, "JPerfmeter Preferences", true);
//        dialog.setContentPane(configurator.getPanel());
        ArrayList plugins = new ArrayList();
        plugins.add(configurator);
        plugins.add(graphConfig);
        plugins.add(streamConfig);
        ConfiguratorPanel conf = new ConfiguratorPanel((Configurator[]) plugins.toArray(new Configurator[plugins.size()]));

        dialog.setContentPane(conf);
        conf.removePropertyChangeListener("configuration", applyChanges);
        conf.addPropertyChangeListener("configuration", applyChanges);
        //configurator.refresh();
        dialog.pack();
        configWindow = dialog;
        dialog.setLocationRelativeTo(MainFrame.this);
        dialog.setVisible(true);
        configWindow = null;
    }

    private JFileChooser getFileChooser() {
        if (fc == null) {
            File dir = Util.getPreferenceDir();
            fc = new JFileChooser(dir);
            fc.setFileFilter(new JPFFileFilter());
        }
        return fc;
    }

    private void saveConfiguration(File file) {
        FileOutputStream fos = null;
        try {
            //This is where a real application would open the file.
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(plugin.getPluginConfigurator());
            oos.writeObject(graphConfig);
            oos.writeObject(streamConfig);
            oos.writeObject(getBounds());
            setModified(file, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e1) {
            }
        }
    }

    private void saveConfiguration() {
        int returnVal = getFileChooser().showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = getFileChooser().getSelectedFile();
            if (!file.getName().endsWith(JPF_EXT))
                file = new File(file.toString() + JPF_EXT);
            saveConfiguration(file);
        }
    }

    private String buildTitle() {
        return (lastFile != null ? lastFile.getName() : "New ") + (modified ? "*" : "") + " - " + TITLE;
    }

    private void setModified(File lastFile, boolean modified) {
        this.lastFile = lastFile;
        this.modified = modified;
        setTitle(buildTitle());
    }

    private void loadConfiguration() {
        int returnVal = getFileChooser().showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = getFileChooser().getSelectedFile();
            try {
                loadConfiguration(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadConfiguration(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        ObjectInputStream oos = new ObjectInputStream(fis);
        PluginConfigurator pc = (PluginConfigurator) oos.readObject();
        graphConfig = (SimpleGraphConfigurator) oos.readObject();
        streamConfig = (StreamWriterConfigurator) oos.readObject();
        Rectangle rect = (Rectangle) oos.readObject();
        plugin.setPluginConfigurator(pc);
        draw();
        // yes, I know ...
        setBounds(rect);
        draw();
        setModified(file, false);
    }

    private void newWindow() {
        PluginConfigurator pc = (PluginConfigurator) Util.newInstance(PerfmeterConfig.class);
        SimpleGraphConfigurator graphConfig = (SimpleGraphConfigurator) Util.newInstance(SimpleGraphConfigurator.class);
        StreamWriterConfigurator streamConfig = (StreamWriterConfigurator) Util.newInstance(StreamWriterConfigurator.class);
        RstatPlugin plugin = new RstatPlugin();
        plugin.setPluginConfigurator(pc);
        MainFrame frame = new MainFrame(plugin, graphConfig, streamConfig);
        frame.setLocation(getLocation().x + 40, getLocation().y + 40);
        frame.setVisible(true);
        plugin.start();
    }

    private PropertyChangeListener applyChanges = new PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent e) {
            if (e.getNewValue() != null) {
                draw();
                setModified(lastFile, true);
            }
            //PluginConfigurator configurator = plugin.getPluginConfigurator();
            if (e.getOldValue() != null && configWindow != null)
                configWindow.dispose();

        }
    };

    /**
     * This method initializes jPopupMenu
     *
     * @return javax.swing.JPopupMenu
     */
    private JPopupMenu getJPopupMenu() {
        if (jPopupMenu == null) {
            jPopupMenu = new JPopupMenu();
            jPopupMenu.setName("properties");
            JMenuItem tmp = new JMenuItem("New Window", KeyEvent.VK_N);
            tmp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
            tmp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    newWindow();
                }
            });
            jPopupMenu.add(tmp);
            tmp = new JMenuItem("Open ...", KeyEvent.VK_O);
            tmp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
            tmp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    loadConfiguration();
                }
            });
            jPopupMenu.add(tmp);
            tmp = new JMenuItem("Save", KeyEvent.VK_S);
            //tmp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
            tmp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveConfiguration(lastFile);
                }
            });
            tmp.setEnabled(lastFile != null && modified);
            jPopupMenu.add(tmp);
            tmp = new JMenuItem("Save As ...", KeyEvent.VK_A);
            tmp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveConfiguration();
                }
            });
            jPopupMenu.add(tmp);
            tmp = new JMenuItem("Configure ...", KeyEvent.VK_C);
            tmp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
            tmp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    openCongifurator();
                }
            });
            jPopupMenu.add(tmp);
            jPopupMenu.addSeparator();
            jMenuItemShowHide = new JMenuItem();
            jMenuItemShowHide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
            jMenuItemShowHide.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showControls = !showControls;
                    draw();
                }
            });

            jPopupMenu.add(jMenuItemShowHide);
            jPopupMenu.addSeparator();
            tmp = new JMenuItem("About ...");
            tmp.addActionListener(new java.awt.event.ActionListener() {
                JFrame frame = new AboutFrame();

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    frame.setLocationRelativeTo(MainFrame.this);
                    frame.setVisible(true);
                }
            });
            jPopupMenu.add(tmp);
        }
        jMenuItemShowHide.setText(!showControls ? "Show Controls" : "Hide Controls");
        return jPopupMenu;
    }


}
