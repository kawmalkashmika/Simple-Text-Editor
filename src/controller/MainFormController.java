package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFormController {
    public TextArea txtText;
    public Path path;
    public String fileName;
    public String selectedText;
    public MenuButton btnFile;
    public MenuButton btnEdit;
    public MenuButton btnHelp;

    public void initialize(){
        setMenus();
        System.out.println();

    }

    public void btnOpenOnAction(ActionEvent actionEvent) throws IOException {
        open();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        save();

    }

    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Destination Folder");
        File file = directoryChooser.showDialog(null);
        String destinationPath = file.getAbsolutePath();
        path= Paths.get(file.getAbsolutePath());

        String fileContent=txtText.getText();
        byte[] bytes=fileContent.getBytes();
        OutputStream os=Files.newOutputStream(Paths.get(path + "/sample.txt"));
        os.write(bytes);
        os.close();
    }

    public void btnCut(ActionEvent actionEvent) {
        Clipboard cp=Clipboard.getSystemClipboard();
        ClipboardContent content=new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);

        txtText.setText(txtText.getSelectedText().replace("",""));






    }

    public void btnCopy(ActionEvent actionEvent) {
        Clipboard cp=Clipboard.getSystemClipboard();
        ClipboardContent content=new ClipboardContent();
        content.putString(txtText.getSelectedText());
        cp.setContent(content);
    }

    public void btnPaste(ActionEvent actionEvent) {
        Clipboard cp=Clipboard.getSystemClipboard();
        String text=cp.getString();
        int index=txtText.getCaretPosition();
        txtText.insertText(index,text);

    }

    private void save() throws IOException {
        File ops=new File(path+"/"+fileName);

        String content=txtText.getText();

        byte[] bytes=content.getBytes();

        FileOutputStream st=new FileOutputStream(ops);
        st.write(bytes);
        st.close();

    }

    private void open() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Source File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files only", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        String sourcePath = file.getAbsolutePath();
        path= Paths.get(sourcePath);
        fileName=file.getName();

        InputStream is= Files.newInputStream(Paths.get(sourcePath));
        byte[] fileBytes=new byte[is.available()];
        is.read(fileBytes);
        String fileContent=new String(fileBytes);
        txtText.setText(fileContent);
    }

    private void setMenus(){
        btnFile.getItems().clear();
        btnEdit.getItems().clear();
        btnHelp.getItems().clear();

        MenuItem menuItem1f = new MenuItem("New");
        MenuItem menuItem2f = new MenuItem("Open");
        MenuItem menuItem3f = new MenuItem("Save");
        MenuItem menuItem4f = new MenuItem("Print");
        MenuItem menuItem5f = new MenuItem("Exit");

        btnFile.getItems().addAll(menuItem1f,menuItem2f,menuItem3f,menuItem4f,menuItem5f);

        MenuItem menuItem1e= new MenuItem("cut");
        MenuItem menuItem2e = new MenuItem("copy");
        MenuItem menuItem3e = new MenuItem("paste");
        MenuItem menuItem4e= new MenuItem("Select All");

        btnEdit.getItems().addAll(menuItem1e,menuItem2e,menuItem3e,menuItem4e);

        menuItem1e.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Destination Folder");
            File file = directoryChooser.showDialog(null);
            String destinationPath = file.getAbsolutePath();
            path= Paths.get(file.getAbsolutePath());

            String fileContent=txtText.getText();
            byte[] bytes=fileContent.getBytes();
            OutputStream os= null;
            try {
                os = Files.newOutputStream(Paths.get(path + "/sample.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        menuItem2e.setOnAction(event -> {
            try {
                open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuItem3f.setOnAction(event -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuItem4f.setOnAction(event -> {
            System.out.println("Print");
        });

        menuItem5f.setOnAction(event -> {
            System.exit(0);
        });
    }


}
