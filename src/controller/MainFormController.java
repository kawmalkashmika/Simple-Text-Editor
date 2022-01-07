package controller;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
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

public class MainFormController {
    public TextArea txtText;
    public Path sourcePath;
    public String fileName;
    public String selectedText;
    public MenuButton btnFile;
    public MenuButton btnEdit;
    public MenuButton btnHelp;
    public AnchorPane anchorPane;

    public void initialize() {


    }

    private void saveTest() {


        //Choose directory

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select text file");
        File file = directoryChooser.showDialog(null);
        String destinationPath = file.getAbsolutePath();
        //copyPath = Paths.get(file.getAbsolutePath());
        //System.out.println(copyPath);

    }

    private void openTextFile() throws IOException {

        //select source path


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file here");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files only", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        String sourcePathString = file.getAbsolutePath();
        fileName = file.getName();
        sourcePath= Paths.get(sourcePathString);

        //Read file from Source path


        FileChannel fileChannel = FileChannel.open(sourcePath);
        ByteBuffer allocate = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(allocate);
        fileChannel.close();
        byte[] array = allocate.array();
        txtText.setText(new String(array));

    }


    private void saveTextFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
        System.out.println(selectedFile.getAbsolutePath());
        if (selectedFile != null) {
           // .display(selectedFile);
        }else {
            System.out.println("Pakaya");
        }
    }


    public void btnOpenOnAction(ActionEvent actionEvent) throws IOException {
      openTextFile();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        //save();
        saveTest();

    }

    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        /*DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Destination Folder");
        File file = directoryChooser.showDialog(null);
        String destinationPath = file.getAbsolutePath();
        path= Paths.get(file.getAbsolutePath());

        String fileContent=txtText.getText();
        byte[] bytes=fileContent.getBytes();
        OutputStream os=Files.newOutputStream(Paths.get(path + "/sample.txt"));
        os.write(bytes);
        os.close();*/
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

    private void save() throws IOException {
       /*File ops=new File(path+"/"+fileName);

        String content=txtText.getText();

        byte[] bytes=content.getBytes();

       FileOutputStream st=new FileOutputStream(ops);
        st.write(bytes);
        st.close();*/

    }

    private void open() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Source File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files only", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        String sourcePath = file.getAbsolutePath();
        //path= Paths.get(sourcePath);
        fileName = file.getName();

        InputStream is = Files.newInputStream(Paths.get(sourcePath));
        byte[] fileBytes = new byte[is.available()];
        is.read(fileBytes);
        String fileContent = new String(fileBytes);
        txtText.setText(fileContent);
    }


}
