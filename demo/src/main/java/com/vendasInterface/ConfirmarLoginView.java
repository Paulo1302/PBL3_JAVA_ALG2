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
        Label tituloLabel = new Label("Confirmação de Login");
        tituloLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Campos para CPF e senha
        Label cpfLabel = new Label("Digite seu CPF:");
        TextField cpfField = new TextField();
        cpfField.setPromptText("CPF");

        Label senhaLabel = new Label("Digite sua senha:");
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("Senha");

        // Botões de confirmar e cancelar
        Button confirmarButton = new Button("Confirmar");
        Button cancelarButton = new Button("Cancelar");

        confirmarButton.setOnAction(event -> {
            try {
                confirmarLogin(cpfField.getText().trim(), senhaField.getText());
            } catch (IOException e) {
                showAlert("Erro", "Erro ao acessar o armazenamento. Tente novamente.");
            }
        });
        cancelarButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        layout.getChildren().addAll(
                tituloLabel, cpfLabel, cpfField, senhaLabel, senhaField, confirmarButton, cancelarButton
        );

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Confirmação de Login");
        stage.show();
    }

    private void confirmarLogin(String cpf, String senha) throws IOException {
        // Valida se os campos não estão vazios
        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert("Erro", "CPF e senha são obrigatórios!");
            return;
        }

        // Usa o método loginUsuario do Controller para validar as credenciais
        if (controller.loginUsuario(cpf, senha, armazenamento)) {
            // Redireciona para a tela de atualização de dados
            new AtualizarDadosView(stage, usuario, controller, armazenamento).show();
        } else {
            showAlert("Erro", "CPF ou senha incorretos!");
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
