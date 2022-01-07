package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class MainFormController {
    public TextArea txtText;
    public AnchorPane anchorPane;
    public Path sourcePath;
    public Path destinationPath;
    public String fileName;
    public MenuItem mnitmNew;
    public MenuItem mnitmOpen;
    public MenuItem mnitmSave;
    public MenuItem mnitmPrint;
    public MenuItem mnitmExit;

    public void initialize() {
    menuItemlistners();

    }


    private void openTextFile() throws IOException {

        //select source path


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file here");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files only", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        String sourcePathString = file.getAbsolutePath();
        fileName = file.getName();
        sourcePath = Paths.get(sourcePathString);

        //Read file from Source path


        FileChannel fileChannel = FileChannel.open(sourcePath);
        ByteBuffer allocate = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(allocate);
        fileChannel.close();
        byte[] array = allocate.array();
        txtText.setText(new String(array));

    }


    private void saveTextFile() {

        //Select a destination path


        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select destination here");
        File file = directoryChooser.showDialog(null);
        String destinationPathString = file.getAbsolutePath();
        destinationPath = Paths.get(file.getAbsolutePath() + setFileName());
        TextInputDialog dilogBox = new TextInputDialog();
        dilogBox.setTitle("input file Name");
        System.out.println(destinationPath);

        //Convert text values into bytes


        byte[] bytes = txtText.getText().getBytes();

        //Copy file into destination


        try {
            Files.createFile(destinationPath);
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "This file already exist").showAndWait();
        }
        FileChannel fc = null;
        try {
            fc = FileChannel.open(destinationPath, StandardOpenOption.WRITE);
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "This file missing right now").showAndWait();
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            fc.write(buffer);
            fc.close();
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "Please check the programme files").showAndWait();
        }

    }

    private String setFileName() {
        TextInputDialog dialogBox = new TextInputDialog();
        dialogBox.setTitle("input file Name");
        dialogBox.showAndWait();
        TextField in = dialogBox.getEditor();
        String fileName = in.getText();
        return "/" + fileName;

    }

    private void menuItemlistners(){
        mnitmNew.setOnAction(event -> {
            txtText.clear();
        });

         mnitmOpen.setOnAction(event -> {
             try {
                 openTextFile();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         });

         mnitmSave.setOnAction(event -> {
             saveTextFile();
         });

         mnitmExit.setOnAction(event -> {
             System.exit(0);
         });



    }


    public void btnOpenOnAction(ActionEvent actionEvent) throws IOException {
        openTextFile();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        saveTextFile();
    }

    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        txtText.clear();
    }

    public void btnCut(ActionEvent actionEvent) {
        Clipboard cp = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);

        txtText.setText(txtText.getSelectedText().replace("", ""));


    }

    public void btnCopy(ActionEvent actionEvent) {
        Clipboard cp = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);
    }

    public void btnPaste(ActionEvent actionEvent) {
        Clipboard cp = Clipboard.getSystemClipboard();
        String text = cp.getString();
        int index = txtText.getCaretPosition();
        txtText.insertText(index, text);

    }


}
