package com.infine.perfmeter.csv;

/*
 * @(#)SimpleFormatter.java 1.14 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


import com.infine.perfmeter.GraphModelManager;
import com.infine.perfmeter.rstat.RstatGraphModel;
import com.infine.perfmeter.rstat.RstatTimeStampGraphModel;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * Print a brief summary of the LogRecord in a human readable format. The
 * summary will typically be 1 or 2 lines.
 *
 * @version 1.14, 12/19/03
 * @since 1.4
 */

public class CSVFormatter extends Formatter {

    Date dat = new Date();

    DecimalFormat dc = new DecimalFormat("#################.######");
    private boolean writeHeader;
    // Line separator string. This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String lineSeparator = (String) System.getProperty("line.separator");


    public CSVFormatter(boolean writeHeader) {
        this.writeHeader = writeHeader;
    }

    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();

        RstatTimeStampGraphModel ts = (RstatTimeStampGraphModel) record.getParameters()[0];
        StreamWriterConfigurator config = (StreamWriterConfigurator) record.getParameters()[1];
        Vector v = GraphModelManager.getInstance().getModels(ts.getHostName());

        if (v.size() == 0)
            return "";

        sb.append(ts.getHostName()).append(";");

        for (int i = 0; i < v.size() - 1; i++) {
            RstatGraphModel model = (RstatGraphModel) v.get(i);
            for (int j = 0; j < model.getSeries(); j++) {
                if (model.getSamples() == 0)
                    sb.append(";");
                else
                    sb.append(dc.format(model.getSerie(j)[model.getSamples() - 1])).append(";");
            }
        }
        if (ts.getSamples() > 0) {
            if (config.isExcelFormat()) {
                sb.append(dc.format((ts.getSerie(0)[ts.getSamples() - 1] / 1000.0 + 2209165200.0) / 86400.0)).append(";");
            } else {
                sb.append(dc.format(ts.getSerie(0)[ts.getSamples() - 1])).append(";");
            }
            sb.append(dc.format(ts.getSerie(1)[ts.getSamples() - 1])).append(lineSeparator);
        } else
            sb.append(";;").append(lineSeparator);
        return sb.toString();
    }

    // This method is called just after the handler using this
    // formatter is created
    public String getHead(Handler h) {
        StringBuffer sb = new StringBuffer();
        if (writeHeader) {
            Vector v = GraphModelManager.getInstance().getModels();
            if (v.size() > 0) {
                sb.append("host;");
                for (int i = 0; i < v.size(); i++) {
                    RstatGraphModel model = (RstatGraphModel) v.get(i);
                    if (model.getFooter() == null)
                        continue;
                    if (model.getFooter().length == 1) {
                        sb.append(model.getFooter()[0].trim());
                        sb.append(";");
                    }
                    for (int j = 1; j < model.getFooter().length; j++) {
                        String str = model.getFooter()[0].trim();
                        str += " ";
                        if (model.getFooter() != null && model.getFooter()[j] != null)
                            str += model.getFooter()[j].trim();
                        sb.append(str.trim());
                        if (i == v.size() - 1 && j == model.getFooter().length - 1)
                            sb.append("\n");
                        else
                            sb.append(";");
                    }
                }
            }
        }
        return sb.toString();
    }

    // This method is called just after the handler using this
    // formatter is closed
    public String getTail(Handler h) {
        return "";
    }
}
