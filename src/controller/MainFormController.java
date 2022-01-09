package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.SubString;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public MenuBar mnbar;
    public TextField txtFind;
    public Button btnFind;
    public TextField txtReplace;
    public Button btnReplace;
    public RadioButton radioRegx;
    public RadioButton radioUppercase;
    public Button btnNext;
    public Matcher matcher;
    public Button btnPrevious;
    public Label txtWordCount;
    ArrayList<SubString> indexes=new ArrayList<>();
    public static int counter=-1;


    public void initialize() {

    menuItemlistners();
    findReplaceUIController(false);

    btnReplace.setOnAction(event -> {
        replace();
    });
    btnPrevious.setOnAction(event -> {
        if(counter>=0){
            counter--;
            txtText.selectRange(indexes.get(counter).getStartIndex(),indexes.get(counter).getEndIndex());
        }else {
            counter=indexes.size()-2;
        }
        txtWordCount.setText(String.valueOf(counter));
    });
    btnNext.setOnAction(event -> {
       if(counter<indexes.size()-1){
           counter++;
           txtText.selectRange(indexes.get(counter).getStartIndex(),indexes.get(counter).getEndIndex());
       }else {
           counter=-1;
       }
       txtWordCount.setText(String.valueOf(counter));
    });

    txtFind.textProperty().addListener(observable -> {
        find();
    });
    txtText.textProperty().addListener(observable -> {
        find();
    });

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
    private void findReplaceUIController(boolean visibility){
        txtFind.setVisible(visibility);
        btnNext.setVisible(visibility);
        btnPrevious.setVisible(visibility);
        btnReplace.setVisible(visibility);
        txtReplace.setVisible(visibility);
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


    //Button on actions
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
        cut();
    }

    public void btnCopy(ActionEvent actionEvent) {
        copy();
    }

    public void btnPaste(ActionEvent actionEvent) {
        paste();
    }



    public void cut() {
        Clipboard cp = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);

    }

    public void copy() {
        Clipboard cp = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);

    }

    public void paste() {
        Clipboard cp = Clipboard.getSystemClipboard();
        String text = cp.getString();
        int index = txtText.getCaretPosition();
        txtText.insertText(index, text);

    }

    private void replace(){
       String s=Pattern.compile(txtFind.getText()).matcher(txtText.getText()).replaceAll(txtReplace.getText());
       txtText.setText(s);
    }

    private void find(){
        indexes.clear();
        int startingIndex=0;
        int endingIndex=0;
        int matchingCount=0;
        matcher= Pattern.compile(txtFind.getText()).matcher(txtText.getText());
        while (matcher.find()){
            startingIndex=matcher.start();
            endingIndex=matcher.end();
            indexes.add(new SubString(startingIndex,endingIndex));
            matchingCount++;

        }
        txtWordCount.setText(String.valueOf(matchingCount));

    }


    public void findButtonOnAction(ActionEvent actionEvent) {
        findReplaceUIController(true);
    }
}
