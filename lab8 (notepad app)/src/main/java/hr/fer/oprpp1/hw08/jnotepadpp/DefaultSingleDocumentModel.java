package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private JTextArea textComponent;
    private Path filePath;
    private boolean modified;
    private List<SingleDocumentListener> listeners;

    public DefaultSingleDocumentModel(Path filePath, String text) {
        this.textComponent = new JTextArea(text);
        this.filePath = filePath;
        this.listeners = new ArrayList<>();
        this.modified = false;
        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
                fireModifyStatusUpdated();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modified = true;
                fireModifyStatusUpdated();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modified = true;
                fireModifyStatusUpdated();
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textComponent;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        this.filePath = path;
        fireFilePathUpdated();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        fireModifyStatusUpdated();
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    private void fireFilePathUpdated() {
        for (SingleDocumentListener l : listeners) {
            l.documentFilePathUpdated(this);
        }
    }

    private void fireModifyStatusUpdated() {
        for (SingleDocumentListener l : listeners) {
            l.documentModifyStatusUpdated(this);
        }
    }
}
