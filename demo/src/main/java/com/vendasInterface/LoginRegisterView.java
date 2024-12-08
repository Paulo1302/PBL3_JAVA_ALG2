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

    // Componentes de UI para serem atualizados
    private Label languageLabel;
    private ComboBox<String> languageComboBox;
    private Label userLabel;
    private Label passLabel;
    private Button loginButton;
    private Label feedbackLabel;
    private Label registrationLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField nameField;
    private TextField cpfField;
    private TextField emailField;
    private Button submitButton;
    private Label registrationFeedback;

    public LoginRegisterView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Carrega o idioma salvo nas preferências
        TranslationManager.getInstance().loadLanguagePreference();

        // Layout principal
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Adiciona o botão de seleção de idioma
        HBox languageBox = createLanguageSelector();
        mainLayout.getChildren().add(languageBox);

        // Layout horizontal para login e registro
        HBox formBox = createFormBox();
        mainLayout.getChildren().add(formBox);
        
        Label titleLabel = new Label("Sistema de Venda de Ingressos");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");
        mainLayout.getChildren().add(0, titleLabel); // Adiciona no topo do layout

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
        stage.setScene(scene);
        stage.show();
        
    }


    private HBox createLanguageSelector() {
        languageLabel = new Label(TranslationManager.getInstance().get("select.language"));
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("pt", "en"); // Adiciona as opções de idioma
        languageComboBox.setValue(TranslationManager.getInstance().getCurrentLanguage()); // Carrega o idioma salvo

        // Configura a mudança de idioma
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            TranslationManager.getInstance().setLanguage(selectedLanguage);
            refreshUI(); // Atualiza a interface para refletir a mudança de idioma
        });

        HBox languageBox = new HBox(10);
        languageBox.setAlignment(Pos.CENTER_LEFT);
        languageBox.getChildren().addAll(languageLabel, languageComboBox);
        languageBox.setPrefWidth(200);
        languageBox.setPrefHeight(100);

        return languageBox;
    }


    private HBox createFormBox() {
        // Configuração do layout de login
        VBox loginBox = createLoginBox();
        // Configuração do layout de registro
        VBox registerBox = createRegisterBox();

        // Layout horizontal para login e registro
        HBox formBox = new HBox(50);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(loginBox, registerBox);
        return formBox;
    }

    private VBox createLoginBox() {
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(300);
        loginBox.setPrefHeight(800);
        loginBox.setLayoutY(50);

        userLabel = new Label(TranslationManager.getInstance().get("login.cpf"));
        TextField userField = new TextField();
        passLabel = new Label(TranslationManager.getInstance().get("login.password"));
        PasswordField passField = new PasswordField();
        loginButton = new Button(TranslationManager.getInstance().get("login.button"));
        feedbackLabel = new Label();

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
        registerBox.setPrefHeight(800);

        registrationLabel = new Label(TranslationManager.getInstance().get("register.new.user"));
        usernameField = new TextField();
        usernameField.setPromptText(TranslationManager.getInstance().get("register.username"));
        passwordField = new PasswordField();
        passwordField.setPromptText(TranslationManager.getInstance().get("register.password"));
        nameField = new TextField();
        nameField.setPromptText(TranslationManager.getInstance().get("register.full.name"));
        cpfField = new TextField();
        cpfField.setPromptText(TranslationManager.getInstance().get("register.cpf"));
        emailField = new TextField();
        emailField.setPromptText(TranslationManager.getInstance().get("register.email"));

        submitButton = new Button(TranslationManager.getInstance().get("register.button"));
        registrationFeedback = new Label();

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
            feedbackLabel.setText(TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            if (controller.loginUsuario(cpf, password, armazenamentoDados)) {
                Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
                new AppScreenView(stage, user, controller, armazenamentoDados).show();
            } else {
                feedbackLabel.setText(TranslationManager.getInstance().get("error.invalid.credentials"));
            }
        } catch (IOException e) {
            feedbackLabel.setText(TranslationManager.getInstance().get("error.accessing.data"));
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
            registrationFeedback.setText(TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            controller.cadastrarUsuario(username, password, name, cpf, email, false, armazenamentoDados);
            Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
            registrationFeedback.setText(TranslationManager.getInstance().get("registration.success"));
            new AppScreenView(stage, user, controller, armazenamentoDados).show();
        } catch (Exception e) {
            registrationFeedback.setText(TranslationManager.getInstance().get("error.registration"));
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        // Atualiza os textos dos elementos de UI com as mensagens de tradução
        languageLabel.setText(TranslationManager.getInstance().get("select.language"));
        userLabel.setText(TranslationManager.getInstance().get("login.cpf"));
        passLabel.setText(TranslationManager.getInstance().get("login.password"));
        loginButton.setText(TranslationManager.getInstance().get("login.button"));
        registrationLabel.setText(TranslationManager.getInstance().get("register.new.user"));
        usernameField.setPromptText(TranslationManager.getInstance().get("register.username"));
        passwordField.setPromptText(TranslationManager.getInstance().get("register.password"));
        nameField.setPromptText(TranslationManager.getInstance().get("register.full.name"));
        cpfField.setPromptText(TranslationManager.getInstance().get("register.cpf"));
        emailField.setPromptText(TranslationManager.getInstance().get("register.email"));
        submitButton.setText(TranslationManager.getInstance().get("register.button"));
        feedbackLabel.setText("");
        registrationFeedback.setText("");
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
    }
}
