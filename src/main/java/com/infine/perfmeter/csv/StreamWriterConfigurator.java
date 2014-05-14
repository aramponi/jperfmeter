package com.infine.perfmeter.csv;

import com.infine.perfmeter.Configurator;
import com.infine.perfmeter.Util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class StreamWriterConfigurator extends JPanel implements Configurator {


    private static final long serialVersionUID = 2096344289625597221L;

    protected static final String LOG_EXT = ".log";

    private JPanel jPanelFile = null;

    private JRadioButton jRadioButtonRotatingFile = null;

    private JTextField jTextFieldPattern = null;

    private JRadioButton jRadioButtonFileName = null;

    private JTextField jTextFieldFileName = null;

    private JRadioButton jRadioButtonNone = null;

    private JCheckBox jCheckBoxAppend = null;

    private JPanel jPanel = null;

    private JButton jButton = null;

    private JFileChooser fc = null;

    //private JLabel jLabel = null;

    String filename = null;

    boolean append = true;

    String pattern = null;

    boolean dataChanged = false;

    boolean excelFormat = false;

    int size = 1024; // in kb

    int history = 7;

    char type = SIZETYPE;

    public static char TIMETYPE = 'T' /* time */;

    public static char SIZETYPE = 'S' /* size */;

    public static String periods[] = {"Month", "Day", "Hour"};

    //

    private JPanel jPanel2 = null;

    private JCheckBox jCheckBoxExcelDataFormat = null;

    private JPanel jPanel3 = null;

    private JTextField jTextFieldSizeLimit = null;

    private JLabel jLabelKo = null;

    private JPanel jPanel1 = null;

    private JLabel jLabelHistory = null;

    private JTextField jTextFieldHistory = null;

    private JLabel jLabelSize = null;

    private JLabel jLabelFileName = null;

    private JPanel jPanel4 = null;

    private JLabel jLabelFilePattern = null;


    class LOGFileFilter extends FileFilter {

        public boolean accept(File f) {
            return f.getName().endsWith(LOG_EXT) || f.isDirectory();
        }

        public String getDescription() {
            return "JPerfmeter Configuration";
        }
    }

    /**
     * This method initializes
     */
    public StreamWriterConfigurator() {
        super();
        initialize();

    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(new java.awt.Dimension(271, 264));
        this.setMaximumSize(new java.awt.Dimension(32767, 200));
        this.add(getJPanelParameters(), null);
        this.add(getJPanel2(), null);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(getJRadioButtonRotatingFile());
        group1.add(getJRadioButtonFileName());
        group1.add(getJRadioButtonNone());
        //ButtonGroup group2 = new ButtonGroup();
        map();
        //toggleFields();
    }

    public void actionApply() {
        actionOk();

    }

    public void actionCancel() {
        // TODO Auto-generated method stub

    }

    public boolean hasChanged() {
        return dataChanged;
    }

    public void clearChanged() {
        dataChanged = false;
    }

    public void actionOk() {
        String oldFilename = filename;
        String oldPattern = pattern;
        boolean oldAppend = append;
        int oldSize = size;
        int oldHistory = history;
        if (jRadioButtonFileName.isSelected()) {
            filename = jTextFieldFileName.getText();
            append = jCheckBoxAppend.isSelected();
            pattern = null;
        } else if (jRadioButtonRotatingFile.isSelected()) {
            pattern = jTextFieldPattern.getText();
            size = Integer.parseInt(jTextFieldSizeLimit.getText());
            history = Integer.parseInt(jTextFieldHistory.getText());
            filename = null;
        } else {
            pattern = null;
            filename = null;
        }

        excelFormat = jCheckBoxExcelDataFormat.isSelected();

        if (pattern == null || (oldPattern == null && pattern != oldPattern) || oldSize != size || oldHistory != history)
            dataChanged = true;
        else if (filename == null || oldFilename == null && filename != oldFilename)
            dataChanged = true;
        else if (!filename.equals(oldFilename) || !pattern.equals(oldPattern))
            dataChanged = true;
        else if (append != oldAppend)
            dataChanged = false;
    }

    public String getDescription() {
        return "File Output";
    }

    public String getName() {
        return "StreamWriter";
    }

    public JPanel getPanel() {
        return this;
    }

    public void initFromArgs(String[] args) {
        // TODO Auto-generated method stub

    }

    public void map() {
        if (filename != null) {
            jRadioButtonFileName.setSelected(true);
            jTextFieldFileName.setText(filename);
            jTextFieldPattern.setText("");
            jCheckBoxAppend.setSelected(append);
            jCheckBoxExcelDataFormat.setSelected(excelFormat);
        } else if (pattern != null) {
            jRadioButtonRotatingFile.setSelected(true);
            jTextFieldFileName.setText("");
            jTextFieldPattern.setText(pattern);
            jCheckBoxExcelDataFormat.setSelected(excelFormat);
        } else {
            jRadioButtonNone.setSelected(true);
            jTextFieldFileName.setText("");
            jTextFieldPattern.setText("");
        }
        jTextFieldHistory.setText(history + "");
        jTextFieldSizeLimit.setText(size + "");
        toggleFields();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readInt();
        if (in.readBoolean()) {
            filename = in.readUTF();
            append = in.readBoolean();
        }
        if (in.readBoolean())
            pattern = in.readUTF();
        excelFormat = in.readBoolean();
        size = in.readInt();
        history = in.readInt();
        type = in.readChar();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeBoolean(filename != null);
        if (filename != null) {
            out.writeUTF(filename);
            out.writeBoolean(append);
        }
        out.writeBoolean(pattern != null);
        if (pattern != null)
            out.writeUTF(pattern);
        out.writeBoolean(excelFormat);
        out.writeInt(size);
        out.writeInt(history);
        out.writeChar(type);
    }

    /**
     * This method initializes jPanelParameters
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelParameters() {
        if (jPanelFile == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(8);
            gridLayout.setColumns(2);
            jPanelFile = new JPanel();
            jPanelFile.setMaximumSize(new java.awt.Dimension(32767, 200));
            jPanelFile.setLayout(gridLayout);
            jPanelFile.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "File", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanelFile.add(getJRadioButtonNone(), null);
            jPanelFile.add(getJRadioButtonFileName(), null);
            jPanelFile.add(getJPanel(), null);
            jPanelFile.add(getJRadioButtonRotatingFile(), null);
            jPanelFile.add(getJPanel4(), null);
            jPanelFile.add(getJPanel3(), null);
            jPanelFile.add(getJPanel1(), null);
            // getJRadioButtonRotatingFile().setEnabled(false);
            // jLabel3.setEnabled(false);
            // getJPanel3().setEnabled(false);
            // jLabel4.setEnabled(false);
            // getJPanel4().setEnabled(false);
            // jLabel5.setEnabled(false);
            // getJPanel1().setEnabled(false);
            // jLabel6.setEnabled(false);
            // getJCheckBoxCompress().setEnabled(false);
            // getJRadioButtonTime().setEnabled(false);
            // getJComboBoxPeriod().setEnabled(false);
            // jLabel2.setEnabled(false);
            // getJRadioButtonSizeLimit().setEnabled(false);
            // getJTextFieldSizeLimit().setEnabled(false);
            // jLabel7.setEnabled(false);
            // getJTextFieldHistory().setEnabled(false);
        }
        return jPanelFile;
    }

    /**
     * This method initializes jRadioButton
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getJRadioButtonRotatingFile() {
        if (jRadioButtonRotatingFile == null) {
            jRadioButtonRotatingFile = new JRadioButton();
            jRadioButtonRotatingFile.setText("Rotating File");
            jRadioButtonRotatingFile.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    toggleFields();
                }
            });
        }
        return jRadioButtonRotatingFile;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldPattern() {
        if (jTextFieldPattern == null) {
            jTextFieldPattern = new JTextField();
            jTextFieldPattern.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        }
        return jTextFieldPattern;
    }

    /**
     * This method initializes jRadioButton1
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getJRadioButtonFileName() {
        if (jRadioButtonFileName == null) {
            jRadioButtonFileName = new JRadioButton();
            jRadioButtonFileName.setText("File");
            jRadioButtonFileName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    toggleFields();
                }
            });
        }
        return jRadioButtonFileName;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldFileName() {
        if (jTextFieldFileName == null) {
            jTextFieldFileName = new JTextField();
            jTextFieldFileName.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        }
        return jTextFieldFileName;
    }

    /**
     * This method initializes jRadioButton2
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getJRadioButtonNone() {
        if (jRadioButtonNone == null) {
            jRadioButtonNone = new JRadioButton();
            jRadioButtonNone.setText("None");
            jRadioButtonNone.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    toggleFields();
                }
            });
        }
        return jRadioButtonNone;
    }

    public boolean isActive() {
        return !(filename == null && pattern == null);
    }

    /**
     * This method initializes jCheckBoxAppend
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBoxAppend() {
        if (jCheckBoxAppend == null) {
            jCheckBoxAppend = new JCheckBox();
            jCheckBoxAppend.setText("Append");
        }
        return jCheckBoxAppend;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jLabelFileName = new JLabel();
            jLabelFileName.setText("File Name  ");
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.X_AXIS));
            jPanel.add(jLabelFileName, null);
            jPanel.add(getJTextFieldFileName(), null);
            jPanel.add(getJButton(), null);
        }
        return jPanel;
    }

    private JFileChooser getFileChooser() {
        if (fc == null) {
            File dir = Util.getPreferenceDir();
            fc = new JFileChooser(dir);
            fc.setFileFilter(new LOGFileFilter());
        }
        return fc;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("...");
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int returnVal = getFileChooser().showSaveDialog(StreamWriterConfigurator.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = getFileChooser().getSelectedFile();
                        if (!file.getName().endsWith(LOG_EXT))
                            file = new File(file.toString() + LOG_EXT);
                        getJTextFieldFileName().setText(file.toString());
                    }

                }
            });
        }
        return jButton;
    }

    public void toggleFields() {
        if (getJRadioButtonNone().isSelected()) {
            getJTextFieldFileName().setEnabled(false);
            getJButton().setEnabled(false);
            getJCheckBoxAppend().setEnabled(false);
            getJTextFieldPattern().setEnabled(false);
            getJTextFieldHistory().setEnabled(false);
            getJTextFieldSizeLimit().setEnabled(false);
            getJCheckBoxExcelDataFormat().setEnabled(false);
            jLabelFileName.setEnabled(false);
            jLabelFilePattern.setEnabled(false);
            jLabelHistory.setEnabled(false);
            jLabelKo.setEnabled(false);
            jLabelSize.setEnabled(false);
        } else if (getJRadioButtonFileName().isSelected()) {
            getJTextFieldFileName().setEnabled(true);
            getJButton().setEnabled(true);
            getJCheckBoxAppend().setEnabled(true);
            getJTextFieldPattern().setEnabled(false);
            getJTextFieldHistory().setEnabled(false);
            getJTextFieldSizeLimit().setEnabled(false);
            getJCheckBoxExcelDataFormat().setEnabled(true);
            jLabelFileName.setEnabled(true);
            jLabelFilePattern.setEnabled(false);
            jLabelHistory.setEnabled(false);
            jLabelKo.setEnabled(false);
            jLabelSize.setEnabled(false);
        } else {
            getJTextFieldFileName().setEnabled(false);
            getJButton().setEnabled(false);
            getJCheckBoxAppend().setEnabled(true);
            getJTextFieldPattern().setEnabled(true);
            getJTextFieldHistory().setEnabled(true);
            getJTextFieldSizeLimit().setEnabled(true);
            getJCheckBoxExcelDataFormat().setEnabled(true);
            jLabelFileName.setEnabled(false);
            jLabelFilePattern.setEnabled(true);
            jLabelHistory.setEnabled(true);
            jLabelKo.setEnabled(true);
            jLabelSize.setEnabled(true);
        }
    }

    public String getFilename() {
        return filename;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isAppend() {
        return append;
    }

    public boolean isExcelFormat() {
        return excelFormat;
    }

    public int getLogSize() {
        return size;
    }

    public int getLogHistory() {
        return history;
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new FlowLayout());
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.add(getJCheckBoxExcelDataFormat(), null);
            jPanel2.add(getJCheckBoxAppend(), null);
        }
        return jPanel2;
    }

    /**
     * This method initializes jCheckBoxExcelDataFormat
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBoxExcelDataFormat() {
        if (jCheckBoxExcelDataFormat == null) {
            jCheckBoxExcelDataFormat = new JCheckBox();
            jCheckBoxExcelDataFormat.setText("Timestamp as Excel date format");
        }
        return jCheckBoxExcelDataFormat;
    }

    /**
     * This method initializes jPanel3
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            jLabelSize = new JLabel();
            jLabelSize.setText("Size");
            jLabelKo = new JLabel();
            jLabelKo.setText(" Ko");
            jPanel3 = new JPanel();
            jPanel3.setLayout(flowLayout);
            jPanel3.setPreferredSize(new java.awt.Dimension(71, 24));
            jPanel3.setMinimumSize(new java.awt.Dimension(71, 24));
            jPanel3.add(jLabelSize, null);
            jPanel3.add(getJTextFieldSizeLimit(), null);
            jPanel3.add(jLabelKo, null);
        }
        return jPanel3;
    }

    /**
     * This method initializes jTextFieldSizeLimit
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldSizeLimit() {
        if (jTextFieldSizeLimit == null) {
            jTextFieldSizeLimit = new JTextField();
            jTextFieldSizeLimit.setMinimumSize(new Dimension(60, 20));
            jTextFieldSizeLimit.setPreferredSize(new Dimension(60, 20));
        }
        return jTextFieldSizeLimit;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(FlowLayout.RIGHT);
            jLabelHistory = new JLabel();
            jLabelHistory.setText("File History  ");
            jPanel1 = new JPanel();
            jPanel1.setLayout(flowLayout1);
            jPanel1.add(jLabelHistory, null);
            jPanel1.add(getJTextFieldHistory(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jTextFieldHistory
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldHistory() {
        if (jTextFieldHistory == null) {
            jTextFieldHistory = new JTextField();
            jTextFieldHistory.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            jTextFieldHistory.setPreferredSize(new Dimension(60, 20));
            jTextFieldHistory.setMinimumSize(new Dimension(60, 20));
        }
        return jTextFieldHistory;
    }

    /**
     * This method initializes jPanel4
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            jLabelFilePattern = new JLabel();
            jLabelFilePattern.setText("File Pattern  ");
            jPanel4 = new JPanel();
            jPanel4.setLayout(new BoxLayout(getJPanel4(), BoxLayout.X_AXIS));
            jPanel4.add(jLabelFilePattern, null);
            jPanel4.add(getJTextFieldPattern(), null);
        }
        return jPanel4;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
