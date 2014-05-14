package com.infine.perfmeter.csv;

import com.infine.perfmeter.GraphModelEvent;
import com.infine.perfmeter.GraphModelListener;
import com.infine.perfmeter.TimeStampGraphModel;
import com.infine.perfmeter.rstat.RstatTimeStampGraphModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.*;


public class StreamWriter implements GraphModelListener {

    private static final String DEFAULT_FILE_NAME = "perfmeter.log";

    private static HashMap streamWriters = new HashMap();

    private StreamWriterConfigurator config = null;

    private TimeStampGraphModel timestamp = null;

    // PrintWriter pw = null;
    Logger logger = Logger.getLogger("StreamWriter");

    boolean configured = false;

    volatile boolean stopped = false;

    DecimalFormat dc = new DecimalFormat("#################.######");

    public static synchronized StreamWriter getInstance(TimeStampGraphModel timestamp, StreamWriterConfigurator config) {
        StreamWriter streamWriter = (StreamWriter) streamWriters.get(config);
        if (streamWriter == null) {
            streamWriter = new StreamWriter(timestamp, config);
            streamWriters.put(config, streamWriter);
        }
        timestamp.addTableModelListener(streamWriter);
        return streamWriter;
    }

    private StreamWriter(TimeStampGraphModel timestamp, StreamWriterConfigurator config) {
        this.config = config;
        this.timestamp = timestamp;
    }

    Object[] objects = new Object[2];

    public synchronized void graphChanged(GraphModelEvent e) {
        if (!config.isActive() || stopped)
            return;
        if (config.hasChanged())
            init((RstatTimeStampGraphModel) e.getSource());
        // RstatTimeStampGraphModel ts = (RstatTimeStampGraphModel) e.getSource();
        // Vector v = GraphModelManager.getInstance().getModels(ts.getHostName());
        LogRecord record = new LogRecord(Level.INFO, "");

        objects[0] = e.getSource();
        objects[1] = config;
        record.setParameters(objects);
        logger.log(record);
    }

    private void init(RstatTimeStampGraphModel ts) {
        // FileOutputStream of = null;
        // File file = null;
        try {
            FileHandler fh = null;
            // if (pw != null)
            // pw.close();
            Handler[] handlers = logger.getHandlers();
            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] instanceof FileHandler)
                    ((FileHandler) handlers[i]).close();
                logger.removeHandler(handlers[i]);
            }
            boolean isFileEmpty = false;
            if (config.getFilename() != null) {
                isFileEmpty = isFileEmpty(config.getFilename());
                fh = new FileHandler(config.getFilename(), config.isAppend());
            } else if (config.getPattern() != null) {
                fh = new FileHandler(config.getPattern(), config.getLogSize() * 1024, config.getLogHistory(), config.isAppend());
            } else {
                isFileEmpty = isFileEmpty(DEFAULT_FILE_NAME);
                fh = new FileHandler(DEFAULT_FILE_NAME, true);
            }
            fh.setFormatter(new CSVFormatter(config.isAppend() ? isFileEmpty : true));
            logger.addHandler(fh);
            configured = true;
            config.clearChanged();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    boolean isFileEmpty(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.length() > 0)
                return false;
        }
        return true;
    }

    public synchronized void stop() {
        timestamp.removeTableModelListener(this);
        streamWriters.remove(config);
        stopped = true;
    }

}
