package pl.olszak.michal.detector.utils;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.reactivex.Single;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.LinkedList;

/**
 * @author molszak
 *         created on 02.05.2017.
 */
public class TextAreaAppender extends AppenderBase<ILoggingEvent> {

    private static final String EOL = System.getProperty("line.separator");

    private TextArea textArea;
    private static final int MAX_LINES = 200;
    private final LinkedList<Integer> length = new LinkedList<>();

    private PatternLayoutEncoder encoder;


    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named [" + name + "].");
            return;
        }

        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Platform.runLater(() -> {
            if (textArea != null) {
                byte[] array = encoder.encode(eventObject);
                logMessage(new String(array));
            }
        });
    }

    private void logMessage(String message) {
        Single.just(message)
                .subscribe(string -> {
                    if (string.endsWith(EOL)) {
                        if (length.size() > MAX_LINES) {
                            textArea.replaceText(0, length.removeFirst() -1, "");
                        }
                        length.addLast(string.length());
                    }
                    textArea.appendText(message);
                });
    }

    public void setTextArea(javafx.scene.control.TextArea textArea) {
        this.textArea = textArea;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
}
