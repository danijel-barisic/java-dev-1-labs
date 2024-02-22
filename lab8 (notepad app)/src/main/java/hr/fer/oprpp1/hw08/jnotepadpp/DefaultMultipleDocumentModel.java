package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private List<SingleDocumentModel> documents;
    private SingleDocumentModel currentDocument = null;
    private List<MultipleDocumentListener> listeners;

    private final ImageIcon redDisk;
    private final ImageIcon greenDisk;

    public DefaultMultipleDocumentModel() {
        this.documents = new ArrayList<>();
        this.listeners = new ArrayList<>();

        InputStream isRed = this.getClass().getResourceAsStream("./icons/red.png");
        InputStream isGreen = this.getClass().getResourceAsStream("./icons/green.png");

        if (isRed == null || isGreen == null) throw new NotepadException("Error while loading icons.");

        byte[] bytesRed = new byte[0];
        byte[] bytesGreen = new byte[0];

        try {
            bytesRed = isRed.readAllBytes();
            isRed.close();
            bytesGreen = isGreen.readAllBytes();
            isGreen.close();
        } catch (IOException exc) {
            System.err.println(exc.getMessage());
            exit(1);
        }

        this.redDisk = new ImageIcon(bytesRed);
        this.greenDisk = new ImageIcon(bytesGreen);
//        return new ImageIcon(bytes);

        this.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int index = DefaultMultipleDocumentModel.this.getSelectedIndex();
                if (index == -1) return;
//                System.out.println("Tab: " + index);
                SingleDocumentModel sdm = DefaultMultipleDocumentModel.this.getDocument(index);
                currentDocument = sdm;
//                Path path = sdm.getFilePath();
//                DefaultMultipleDocumentModel.this.setTitleAt(index, path == null ?
//                        "(untitled)" : path.getFileName().toString());

                Path path = sdm.getFilePath();
                String newTabTitle = path == null ? "(untitled)" : path.getFileName().toString();
                String newWindowTitle = null;
                try {
                    newWindowTitle = path == null ? "(untitled)" : path.toFile().getCanonicalPath();
                } catch (IOException ex) {
                    System.err.println("Couldn't get canonical path.");
                    exit(1);
                }

                DefaultMultipleDocumentModel.this.setTitleAt(index, newTabTitle);
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(DefaultMultipleDocumentModel.this);
                topFrame.setTitle(newWindowTitle + " - JNotepad++");
                SingleDocumentModel prevDocument = documents.size() > 0 ? currentDocument : null;
                fireDocumentChanged(prevDocument);

            }
        });
    }

    private JPanel wrapTextArea(JTextArea textArea, String name) {

        JPanel p = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(textArea);
        p.add(sp, BorderLayout.CENTER);
        p.setName(name == null ? "(untitled)" : name);
        return p;
    }

    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel prevDocument = documents.size() > 0 ? currentDocument : null;
        SingleDocumentModel sdm = new DefaultSingleDocumentModel(null, null);

        documents.add(sdm);
//
//        JPanel p = new JPanel(new BorderLayout());
//        Border lineBorder = BorderFactory.createLineBorder(new Color(0xAD85FF), 5);
//        JScrollPane sp = new JScrollPane(sdm.getTextComponent());
//        sp.setBorder(lineBorder);
//        p.add(sp, BorderLayout.CENTER);
//        p.setName("(untitled test)");
//        this.add(wrapTextArea(sdm.getTextComponent(), null));
        this.addTab("test title", redDisk, wrapTextArea(sdm.getTextComponent(), null), "(untitled)");

        this.setSelectedIndex(this.getTabCount() - 1);
        currentDocument = sdm;
        this.setIconAt(this.getTabCount() - 1, redDisk);
        sdm.setModified(true);
        currentDocument.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                DefaultMultipleDocumentModel.this.setIconAt(DefaultMultipleDocumentModel.this.getSelectedIndex(),
                        model.isModified() ? redDisk : greenDisk);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                DefaultMultipleDocumentModel.this.fireStateChanged();
            }
        });
        fireDocumentAdded(currentDocument);
        fireDocumentChanged(prevDocument);

        return sdm;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        SingleDocumentModel prevDocument = documents.size() > 0 ? currentDocument : null;

        if (path == null) {
            throw new NullPointerException("Path must not be null.");
        }

        int index;

        //if document exists
        for (SingleDocumentModel s : documents) {
            if (s.getFilePath() != null && s.getFilePath().equals(path)) {
                index = this.getIndexOfDocument(s);
                this.setSelectedIndex(index);
//                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
//                JOptionPane.showOptionDialog(this.getParent(), "Do you want to save first?",
//                        "Save", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
//                        null, null, null);
                currentDocument = s;
                currentDocument.setModified(false);
                return null;
            }
        }

        /*
        // if unmodified
        if (sdm != null && sdm.isModified()) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    int choice = JOptionPane.showOptionDialog(this.getParent(), "Do you want to save first?",
                            "Save", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, null, null);
                    if (choice != JOptionPane.CLOSED_OPTION) {
                        if (choice == JOptionPane.YES_OPTION) {
                            saveDocument(s, path);
                            currentDocument = s;
                        } else {
                            currentDocument = s;
                        }
                    }
                }
         */

        byte[] okteti;
        try {
            okteti = Files.readAllBytes(path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this.getParent(),
                    "Pogreška prilikom čitanja datoteke " + path + ".",
                    "Pogreška",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String text = new String(okteti, StandardCharsets.UTF_8);

        SingleDocumentModel sdm = new DefaultSingleDocumentModel(path, text);
        documents.add(sdm);
        try {
            this.addTab("test title", greenDisk, wrapTextArea(sdm.getTextComponent(), null),
                    path.toFile().getCanonicalPath());
        } catch (IOException ex) {
            System.err.println("Couldn't get canonical path.");
            exit(1);
        }
        this.setSelectedIndex(this.getTabCount() - 1);

        currentDocument = sdm;

        currentDocument.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                DefaultMultipleDocumentModel.this.setIconAt(DefaultMultipleDocumentModel.this.getSelectedIndex(),
                        model.isModified() ? redDisk : greenDisk);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                DefaultMultipleDocumentModel.this.fireStateChanged();
            }
        });

        fireDocumentAdded(currentDocument);
        fireDocumentChanged(prevDocument);

        return sdm;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {

        if (newPath == null) {
            newPath = model.getFilePath();
            if (newPath == null) {
                throw new NotepadException("Invalid path.");
            }
        } else if (findForPath(newPath) != null) {
            throw new NotepadException("Model with the associated path already exists." +
                    "Try closing it first.");
        }

        byte[] podatci = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(newPath, podatci);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                    this.getParent(),
                    "Error whilst writing to file " + newPath.toFile().getAbsolutePath(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(
                this.getParent(),
                "File is saved.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);

        model.setFilePath(newPath);
        model.setModified(false);
        try {
            this.setToolTipTextAt(this.getSelectedIndex(), newPath.toFile().getCanonicalPath());
        } catch (IOException ex) {
            System.err.println("Couldn't get canonical path.");
            exit(1);
        }
        this.fireStateChanged();

    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = this.documents.indexOf(model);
        documents.remove(model);
        this.removeTabAt((index == -1) ? this.getSelectedIndex() : index);

        if (documents.size() == 0) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(DefaultMultipleDocumentModel.this);
            topFrame.setTitle("JNotepad++");
        }

        fireDocumentRemoved(model);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        for (SingleDocumentModel s : documents) {
            if (s.getFilePath() != null && s.getFilePath().equals(path)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        int index = 0;
        for (SingleDocumentModel s : documents) {
            if (s.getFilePath() != null && s.getFilePath().equals(doc.getFilePath())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    private void fireDocumentChanged(SingleDocumentModel oldDocument) {

        if (oldDocument == null && currentDocument == null) {
            throw new NullPointerException("Old document and new document cannot both be null.");
        }

        for (MultipleDocumentListener l : listeners) {
            l.currentDocumentChanged(oldDocument, currentDocument);
        }
    }

    private void fireDocumentAdded(SingleDocumentModel sdm) {
        for (MultipleDocumentListener l : listeners) {
            l.documentAdded(sdm);
        }
    }

    private void fireDocumentRemoved(SingleDocumentModel sdm) {
        for (MultipleDocumentListener l : listeners) {
            l.documentRemoved(sdm);
        }
    }
}
