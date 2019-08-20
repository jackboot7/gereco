package controllers;

import application.Main;
import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.Institution;
import services.InstitutionService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialController implements Initializable {
    public JFXTextField txtRegisterName;
    public JFXTextField txtRegisterEmail;
    public JFXPasswordField txtRegisterPassword;
    public JFXTextField txtLoginEmail;
    public JFXPasswordField txtLoginPassword;
    public Label lblLoginMessage;
    public Label lblRegisterMessage;
    public JFXButton btnLogin;
    public StackPane stackPaneLogin;

    private InstitutionService institutionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        institutionService = new InstitutionService();
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            lblLoginMessage.setText("Falha ao efetuar login");
        else
            openHomeView();
    }

    @FXML
    protected void efetuateInstitutionRegister() {
        Institution newInstitution = new Institution(txtRegisterName.getText(), txtRegisterEmail.getText(),
                txtRegisterPassword.getText());
        boolean validRegister = institutionService.insertInstitution(newInstitution);

        lblRegisterMessage.setText(validRegister ? "Cadastro efetuado com sucesso" : "Falha ao efetuar o cadastro");
    }

    private void openHomeView(){
        Main.mainStage.close();
        Main.mainStage = new Stage();
        maximizeView();

        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/home/home.fxml")));

            Main.mainStage .setScene(scene);
            Main.mainStage .setResizable(true);
            Main.mainStage .centerOnScreen();
            Main.mainStage .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void maximizeView(){
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Main.mainStage.setX(bounds.getMinX());
        Main.mainStage.setY(bounds.getMinY());
        Main.mainStage.setWidth(bounds.getWidth());
        Main.mainStage.setHeight(bounds.getHeight());
    }

    @FXML
    protected void loadForgotPasswordDialog(){
        JFXButton btnConfirm = new JFXButton("Ok");
        String heading = "Redefinir senha";
        String body = "Siga as instruções enviadas no seu email para redefinir sua senha";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, btnConfirm, stackPaneLogin);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        btnConfirm.setOnAction(action -> dialog.close());
        dialog.show();
    }
}
