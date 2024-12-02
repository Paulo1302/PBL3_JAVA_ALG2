package com.vendasInterface;

import com.example.Controller;
import com.example.Armazenamento;
import com.example.Usuario;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginRegisterView {

    private final Controller controller = new Controller();
    private final Armazenamento armazenamentoDados = new Armazenamento();
    private final Stage stage;

    public LoginRegisterView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Configuração do layout de login
        VBox loginBox = createLoginBox();

        // Configuração do layout de registro
        VBox registerBox = createRegisterBox();

        // Layout principal
        HBox hbox = new HBox(50);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(loginBox, registerBox);

        Scene scene = new Scene(hbox, 1000, 600);
        stage.setTitle("Login & Register");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLoginBox() {
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(300);

        Label userLabel = new Label("CPF:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label feedbackLabel = new Label();

        loginBox.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, feedbackLabel);

        loginButton.setOnAction(event -> handleLogin(userField, passField, feedbackLabel));
        userField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handleLogin(userField, passField, feedbackLabel);
        });
        passField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handleLogin(userField, passField, feedbackLabel);
        });

        return loginBox;
    }

    private VBox createRegisterBox() {
        VBox registerBox = new VBox(10);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300);

        Label registrationLabel = new Label("Register New User");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter full name");
        TextField cpfField = new TextField();
        cpfField.setPromptText("Enter CPF");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        Button submitButton = new Button("Register");
        Label registrationFeedback = new Label();

        registerBox.getChildren().addAll(registrationLabel, usernameField, passwordField, nameField, cpfField, emailField, submitButton, registrationFeedback);

        submitButton.setOnAction(event -> handleRegistration(usernameField, passwordField, nameField, cpfField, emailField, registrationFeedback));
        submitButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handleRegistration(usernameField, passwordField, nameField, cpfField, emailField, registrationFeedback);
        });

        return registerBox;
    }

    private void handleLogin(TextField userField, PasswordField passField, Label feedbackLabel) {
        String cpf = userField.getText();
        String password = passField.getText();

        if (cpf.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("CPF and password cannot be empty.");
            return;
        }

        try {
            if (controller.loginUsuario(cpf, password, armazenamentoDados)) {
                Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
                new AppScreenView(stage, user, controller, armazenamentoDados).show();
            } else {
                feedbackLabel.setText("Invalid CPF or password!");
            }
        } catch (IOException e) {
            feedbackLabel.setText("Error accessing user data.");
            e.printStackTrace();
        }
    }

    private void handleRegistration(TextField usernameField, PasswordField passwordField, TextField nameField, TextField cpfField, TextField emailField, Label registrationFeedback) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String cpf = cpfField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || cpf.isEmpty() || email.isEmpty()) {
            registrationFeedback.setText("All fields must be filled!");
            return;
        }

        try {
            controller.cadastrarUsuario(username, password, name, cpf, email, false, armazenamentoDados);
            Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
            registrationFeedback.setText("User registered successfully!");
            new AppScreenView(stage, user, controller, armazenamentoDados).show();

        } catch (Exception e) {
            registrationFeedback.setText("Error during registration!");
            e.printStackTrace();
        }
    }
}
