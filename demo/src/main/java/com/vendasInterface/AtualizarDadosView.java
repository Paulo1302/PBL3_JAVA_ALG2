package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Tela para atualizar os dados do usuário.
 */
public class AtualizarDadosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public AtualizarDadosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        // Campos para atualização
        Label tituloLabel = new Label("Atualizar Dados");
        tituloLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TextField nomeField = new TextField(usuario.getNome());
        nomeField.setPromptText("Nome");

        TextField emailField = new TextField(usuario.getEmail());
        emailField.setPromptText("Email");

        // Botões de ação
        Button salvarButton = new Button("Salvar");
        Button cancelarButton = new Button("Cancelar");

        salvarButton.setOnAction(event -> salvarDados(nomeField.getText().trim(), emailField.getText().trim()));
        cancelarButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        layout.getChildren().addAll(
                tituloLabel,
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                salvarButton, cancelarButton
        );

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Atualizar Dados");
        stage.show();
    }

    private void salvarDados(String nome, String email) {
        // Verifica se os campos estão preenchidos
        if (nome.isEmpty() || email.isEmpty()) {
            showAlert("Erro", "Nome e Email são obrigatórios!");
            return;
        }

        // Atualiza os dados do usuário
        try {
            System.out.println("Atualizando dados do usuário...");
            controller.NovoCadastroUsuario(usuario, usuario.getLogin(), usuario.getSenha(), nome, email, armazenamento);
            System.out.println("Dados atualizados com sucesso.");

            showAlert("Sucesso", "Dados atualizados com sucesso!");
            // Redireciona para a tela principal após a atualização
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar os dados: " + e.getMessage());
            showAlert("Erro", "Erro ao atualizar os dados. Tente novamente.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
