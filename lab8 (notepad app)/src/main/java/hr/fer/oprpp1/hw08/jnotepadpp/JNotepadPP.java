package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.System.exit;

public class JNotepadPP extends JFrame {

    private MultipleDocumentModel mdm;
    private JPanel statusBar;
    private JLabel lengthStatus;
    private JLabel lineStatus;
    private JLabel colStatus;
    private JLabel selStatus;


    public JNotepadPP() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (int i = 0; i < mdm.getNumberOfDocuments(); i++) {
                    if (mdm.getDocument(i).isModified()) {
                        int choice = JOptionPane.showOptionDialog(JNotepadPP.this,
                                "Do you want to save first?",
                                "Save", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, null, null);
                        if(choice==JOptionPane.NO_OPTION){
                            continue;
                        }
                        else if (!(choice == JOptionPane.ABORT || choice == JOptionPane.CANCEL_OPTION
                                || choice == JOptionPane.CLOSED_OPTION)) {
                            Path path = mdm.getDocument(i).getFilePath();
                            if (path == null) {
                                JFileChooser jfc = new JFileChooser();
                                jfc.showSaveDialog(JNotepadPP.this);
                                jfc.setDialogTitle("Save document");
                                path = jfc.getSelectedFile().toPath();
                                mdm.saveDocument(mdm.getDocument(i), path);
                            }
                        }
                        else{
                            return;
                        }
                    }
                }
                dispose();
                exit(0);
            }
        });

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setTitle("JNotepad++");

        setLocation(200, 10);

        setSize(1000, 800);

        initGUI();
    }

    protected void initGUI() {
        lengthStatus = new JLabel("0");
        lineStatus = new JLabel("1");
        colStatus = new JLabel("1");
        selStatus = new JLabel("0");

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        mdm = new DefaultMultipleDocumentModel();

        mdm.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                fireLengthUpdate();
                fireCaretUpdate(currentModel.getTextComponent().getCaretPosition(),
                        currentModel.getTextComponent().getCaret().getMark());
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });

        JPanel mdmWrap = new JPanel(new BorderLayout());
        mdmWrap.add(mdm.getVisualComponent(), BorderLayout.CENTER);

        statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftStatus.add(new JLabel("length :"));
        leftStatus.add(lengthStatus);
        leftStatus.add(new JLabel(" Ln :"));
        leftStatus.add(lineStatus);
        leftStatus.add(new JLabel(" Col :"));
        leftStatus.add(colStatus);
        leftStatus.add(new JLabel(" Sel :"));
        leftStatus.add(selStatus);
        statusBar.add(leftStatus);

        statusBar.add(new JSeparator(SwingConstants.VERTICAL));

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JComponent clock = new Clock();
        rightStatus.add(clock);

        statusBar.add(rightStatus);

        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        mdmWrap.add(statusBar, BorderLayout.PAGE_END);

        cp.add(mdmWrap, BorderLayout.CENTER);

        createActions();
        createMenus();
        createToolbars();
    }


    private static class Clock extends JLabel {

        volatile String vrijeme;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        public Clock() {
            updateTime();

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                    }
                    SwingUtilities.invokeLater(() -> {
                        updateTime();
                    });
                }
            });
            t.setDaemon(true);
            t.start();
        }

        private void updateTime() {
            vrijeme = formatter.format(LocalDateTime.now());
            setText(vrijeme);
        }

    }

    private void fireLengthUpdate() {
        lengthStatus.setText(Integer.toString(mdm.getCurrentDocument().getTextComponent().getText().length()));
    }

    private void fireCaretUpdate(int offset, int mark) {
        try {
            JTextArea textComp = mdm.getCurrentDocument().getTextComponent();
            int line = textComp.getLineOfOffset(offset);
            lineStatus.setText(Integer.toString(1 + line));
            colStatus.setText(Integer.toString(1 + offset -
                    textComp.getLineStartOffset(line)));
            selStatus.setText(Integer.toString(Math.abs(mark - offset)));
        } catch (BadLocationException ex) {
            throw new NotepadException("Bad offset provided.");
        }
    }

    private Action newDocumentAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            mdm.createNewDocument();
            mdm.getCurrentDocument().addSingleDocumentListener(new SingleDocumentListener() {
                @Override
                public void documentModifyStatusUpdated(SingleDocumentModel model) {
                    fireLengthUpdate();
                    fireCaretUpdate(0, 0);
                }

                @Override
                public void documentFilePathUpdated(SingleDocumentModel model) {
                    return;
                }
            });
            mdm.getCurrentDocument().getTextComponent().addCaretListener(c -> fireCaretUpdate(c.getDot(), c.getMark()));
        }
    };

    private Action openDocumentAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");
            if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();
            if (!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "File " + fileName.getAbsolutePath() + " doesn't exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mdm.loadDocument(filePath);
            mdm.getCurrentDocument().addSingleDocumentListener(new SingleDocumentListener() {
                @Override
                public void documentModifyStatusUpdated(SingleDocumentModel model) {
                    fireLengthUpdate();
                    fireCaretUpdate(0, 0);
                }

                @Override
                public void documentFilePathUpdated(SingleDocumentModel model) {
                    return;
                }
            });
            mdm.getCurrentDocument().getTextComponent().addCaretListener(c -> fireCaretUpdate(c.getDot(), c.getMark()));

        }

    };

    private Action saveDocumentAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Path newPath = null;
            SingleDocumentModel sdm = mdm.getCurrentDocument();
            if (sdm.getFilePath() == null) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Save document");
                if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(
                            JNotepadPP.this,
                            "Nothing is saved.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //actual saving. Remember to add path to document in savedocument
                newPath = jfc.getSelectedFile().toPath();
                if (mdm.findForPath(newPath) != null) {
                    JOptionPane.showMessageDialog(
                            JNotepadPP.this,
                            "Document with that path already exists. Try closing it first before saving.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    mdm.saveDocument(sdm, newPath);
                }
            } else {
                //saving of preexisting documents with path
                mdm.saveDocument(sdm, null);
            }

        }
    };

    private Action saveDocumentAsAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Path newPath = null;
            SingleDocumentModel sdm = mdm.getCurrentDocument();

            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Save document as");
            if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Nothing is saved.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            //actual saving. Remember to add path to document in savedocument
            newPath = jfc.getSelectedFile().toPath();
//
            if (mdm.findForPath(newPath) != null) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Document with that path already exists. Try closing it first before saving.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                mdm.saveDocument(sdm, newPath);
            }
        }
    };

    private Action closeDocumentAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {

            SingleDocumentModel sdm = mdm.getCurrentDocument();
            mdm.closeDocument(sdm);
        }

    };

//    private Action deleteSelectedPartAction = new AbstractAction() {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
////            Document doc = editor.getDocument();
////            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
////            if (len == 0) return;
////            int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
////            try {
////                doc.remove(offset, len);
////            } catch (BadLocationException e1) {
////                e1.printStackTrace();
////            }
//        }
//    };


    private Action invertCaseAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea text = mdm.getCurrentDocument().getTextComponent();
            String oldText = text.getText();
            text.setText(oldText.substring(0, text.getSelectionStart()) +
                    changeCase(oldText.substring(text.getSelectionStart(), text.getSelectionEnd()))
                    + oldText.substring(text.getSelectionEnd()));
        }

        private String changeCase(String text) {
            char[] znakovi = text.toCharArray();
            for (int i = 0; i < znakovi.length; i++) {
                char c = znakovi[i];
                if (Character.isLowerCase(c)) {
                    znakovi[i] = Character.toUpperCase(c);
                } else if (Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    private Action toUpperCaseAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea text = mdm.getCurrentDocument().getTextComponent();
            String oldText = text.getText();
            text.setText(oldText.substring(0, text.getSelectionStart()) + oldText.substring(text.getSelectionStart(),
                    text.getSelectionEnd()).toUpperCase() + oldText.substring(text.getSelectionEnd()));
        }
    };

    private Action toLowerCaseAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea text = mdm.getCurrentDocument().getTextComponent();
            String oldText = text.getText();
            text.setText(oldText.substring(0, text.getSelectionStart()) + oldText.substring(text.getSelectionStart(),
                    text.getSelectionEnd()).toLowerCase() + oldText.substring(text.getSelectionEnd()));
        }
    };

    private Action exitAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
//            System.exit(0);
        }
    };

    private Action cutAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action copyAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action pasteAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action showStatisticsAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private void createActions() {

        newDocumentAction.putValue(
                Action.NAME,
                "New");
        newDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control N"));
        newDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_N);
        newDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to create new document in editor.");

        openDocumentAction.putValue(
                Action.NAME,
                "Open");
        openDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O);
        openDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to open existing file from disk.");

        saveDocumentAction.putValue(
                Action.NAME,
                "Save");
        saveDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S);
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk.");

        saveDocumentAsAction.putValue(
                Action.NAME,
                "Save as");
        saveDocumentAsAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control shift S"));
        saveDocumentAsAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_A);
        saveDocumentAsAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file as new file.");

        closeDocumentAction.putValue(
                Action.NAME,
                "Close document");
        closeDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control shift X"));
        closeDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_L);
        closeDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to close current document tab.");

        showStatisticsAction.putValue(
                Action.NAME,
                "Show statistics");
        showStatisticsAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control T"));
        showStatisticsAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_T);
        showStatisticsAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to show informational message about the current document.");

        cutAction.putValue(
                Action.NAME,
                "Cut");
        cutAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_U);
        cutAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to cut selected characters into clipboard.");

        copyAction.putValue(
                Action.NAME,
                "Copy");
        copyAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_C);
        copyAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to copy selected characters into clipboard.");

        pasteAction.putValue(
                Action.NAME,
                "Paste");
        pasteAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control P"));
        pasteAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_P);
        pasteAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to paste selected characters from clipboard into editor.");
//
//        deleteSelectedPartAction.putValue(
//                Action.NAME,
//                "Delete selected text");
//        deleteSelectedPartAction.putValue(
//                Action.ACCELERATOR_KEY,
//                KeyStroke.getKeyStroke("F2"));
//        deleteSelectedPartAction.putValue(
//                Action.MNEMONIC_KEY,
//                KeyEvent.VK_D);
//        deleteSelectedPartAction.putValue(
//                Action.SHORT_DESCRIPTION,
//                "Used to delete the selected part of text.");

        invertCaseAction.putValue(
                Action.NAME,
                "Toggle case");
        invertCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control F3"));
        invertCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O);
        invertCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to toggle character case in selected part of text or in entire document.");

        toUpperCaseAction.putValue(
                Action.NAME,
                "To upper case");
        toUpperCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control U"));
        toUpperCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_U);
        toUpperCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to up the character case in selected part of text or in entire document.");

        toLowerCaseAction.putValue(
                Action.NAME,
                "To lower case");
        toLowerCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control L"));
        toLowerCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_L);
        toLowerCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to lower the character case in selected part of text or in entire document.");

        exitAction.putValue(
                Action.NAME,
                "Exit");
        exitAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("ESCAPE"));
        exitAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_X);
        exitAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Exit application.");

    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAsAction));
        fileMenu.add(new JMenuItem(closeDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(showStatisticsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));
//        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(invertCaseAction));

        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(editMenu);
        editMenu.add(new JMenuItem(toUpperCaseAction));
        editMenu.add(new JMenuItem(toUpperCaseAction));
        editMenu.add(new JMenuItem(invertCaseAction));

        this.setJMenuBar(menuBar);
    }

    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Alati");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(newDocumentAction));
        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.add(new JButton(saveDocumentAsAction));
        toolBar.add(new JButton(closeDocumentAction));
        toolBar.add(new JButton(showStatisticsAction));
        toolBar.add(new JButton(exitAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(cutAction));
        toolBar.add(new JButton(copyAction));
        toolBar.add(new JButton(pasteAction));
//        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(toUpperCaseAction));
        toolBar.add(new JButton(toLowerCaseAction));
        toolBar.add(new JButton(invertCaseAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new JNotepadPP().setVisible(true));
    }
}
