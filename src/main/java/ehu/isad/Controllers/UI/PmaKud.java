package ehu.isad.Controllers.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PmaKud implements Initializable {

    @FXML
    private TextField urlTF;

    @FXML
    private Button checkBTN;

    @FXML
    private TableView<?> infoT;

    @FXML
    private TableColumn<?, ?> urlTC;

    @FXML
    private TableColumn<?, ?> md5TC;

    @FXML
    private TableColumn<?, ?> versionTC;

    @FXML
    private Label egoeraL;

    @FXML
    void onClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
