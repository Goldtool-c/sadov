package bsu.rpact.ui;

import bsu.rpact.Application;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ClientController implements Initializable {

    private File imageFile;
    @FXML
    public ImageView preImage;

    @FXML
    private Button startButton;

    @FXML
    private MenuButton rgbChooser;

    @FXML
    private MenuButton layersChooser;
    private String rgb;
    private int layer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<MenuItem> rgbChooserItems = rgbChooser.getItems();
        for (int i = 0; i < rgbChooserItems.size(); i++) {
            int finalI = i;
            rgbChooserItems.get(i).setOnAction(event -> rgb = rgbChooserItems.get(finalI).getText());
        }
        ObservableList<MenuItem> layers = layersChooser.getItems();
        for (int i = 0; i < layers.size() ; i++) {
            int finalI = i;
            layers.get(i).setOnAction(event -> {
                layer = Integer.parseInt(layers.get(finalI).getText());
            });
        }
        startButton.setOnAction(event -> {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(imageFile.getAbsolutePath());

                JSONObject jsonObject = new JSONObject();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                jsonObject.put("rgb", rgb);
                jsonObject.put("layer", layer);
                jsonObject.put("text", "secretInfo");
                jsonObject.put("image", new String(bytes));
                URL server = new URL ("https://localhost:7176");
                /*HttpURLConnection httpcon=(HttpURLConnection)server.openConnection();
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestMethod("POST");OutputStreamWriter output=new OutputStreamWriter(httpcon.getOutputStream());
                output.write(jsonObject.toString());
                httpcon.connect();
                String output1=httpcon.getResponseMessage();
                System.out.println(output1);*/
                Client client = Client.create();
                WebResource webResource = client.resource("https://localhost:7176");
                ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonObject.toJSONString());
                String output = response.getEntity(String.class);
                System.out.println(output);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void openFile(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image files","*.png",".*jpg","*.jpeg","*.bmp"));
        fileChooser.setTitle("Select an image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        imageFile = fileChooser.showOpenDialog(new Stage());
        preImage.setImage(new Image(new FileInputStream(imageFile.getAbsolutePath())));
    }

    @FXML
    public void saveAs(ActionEvent actionEvent) {
        //TODO preImage -> postImage
        if(preImage==null){
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image files","*.png",".*jpg","*.jpeg","*.bmp"));
        fileChooser.setTitle("Choose directory to save");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showSaveDialog(new Stage());
        //TODO preImage->postImage
        saveToFile(preImage.getImage(),file.getAbsolutePath(),
                FilenameUtils.getExtension(file.getAbsolutePath()));
    }

    @FXML
    public void reset(ActionEvent actionEvent) {
        preImage.setImage(null);
    }

    @FXML
    public void quit(ActionEvent actionEvent) {
        System.exit(1);
    }

    private static void saveToFile(Image image, String path, String format) {
        File outputFile = new File(path);
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, format, outputFile);
        } catch (IOException ex) {
            Logger.getLogger(String.valueOf(ClientController.class)).log(Level.SEVERE, null, ex);
        }
    }
}
