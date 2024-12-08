package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Tela para confirmar o login do usuário antes de atualizar os dados.
 */
public class ConfirmarLoginView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public ConfirmarLoginView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        // Mensagem inicial para identificação
        Label tituloLabel = new Label(TranslationManager.getInstance().get("login.confirmation"));
        tituloLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Campos para CPF e senha
        Label cpfLabel = new Label(TranslationManager.getInstance().get("enter.cpf"));
        TextField cpfField = new TextField();
        cpfField.setPromptText(TranslationManager.getInstance().get("cpf"));

        Label senhaLabel = new Label(TranslationManager.getInstance().get("enter.password"));
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText(TranslationManager.getInstance().get("password"));

        // Botões de confirmar e cancelar
        Button confirmarButton = new Button(TranslationManager.getInstance().get("confirm"));
        Button cancelarButton = new Button(TranslationManager.getInstance().get("cancel"));

        confirmarButton.setOnAction(event -> {
            try {
                confirmarLogin(cpfField.getText().trim(), senhaField.getText());
            } catch (IOException e) {
                showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.accessing.storage"));
            }
        });
        cancelarButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        layout.getChildren().addAll(
                tituloLabel, cpfLabel, cpfField, senhaLabel, senhaField, confirmarButton, cancelarButton
        );

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("login.confirmation"));
        stage.show();
    }

    private void confirmarLogin(String cpf, String senha) throws IOException {
        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        if (controller.loginUsuario(cpf, senha, armazenamento)) {
            new AtualizarDadosView(stage, usuario, controller, armazenamento).show();
        } else {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.invalid.credentials"));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
