package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        Label tituloLabel = new Label(TranslationManager.getInstance().get("update.profile"));
        tituloLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TextField nomeField = new TextField(usuario.getNome());
        nomeField.setPromptText(TranslationManager.getInstance().get("name"));

        TextField emailField = new TextField(usuario.getEmail());
        emailField.setPromptText(TranslationManager.getInstance().get("email"));

        // Botões de ação
        Button salvarButton = new Button(TranslationManager.getInstance().get("save"));
        Button cancelarButton = new Button(TranslationManager.getInstance().get("cancel"));

        salvarButton.setOnAction(event -> salvarDados(nomeField.getText().trim(), emailField.getText().trim()));
        cancelarButton.setOnAction(event -> {
            // Redireciona para a tela principal, preservando o idioma atual
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        });

        // Adiciona os componentes ao layout
        layout.getChildren().addAll(
                tituloLabel,
                new Label(TranslationManager.getInstance().get("name") + ":"), nomeField,
                new Label(TranslationManager.getInstance().get("email") + ":"), emailField,
                salvarButton, cancelarButton
        );

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));

        // Adiciona um listener para detectar mudanças de idioma e recarregar a tela
        TranslationManager.getInstance().addLanguageChangeListener(this::refreshUI);

        stage.show();
    }

    private void refreshUI() {
        // Atualiza os textos da interface quando o idioma é alterado
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));
        // Atualiza os textos dos componentes da interface
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".label")) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String key = getKeyFromLabel(label);
                if (key != null) {
                    label.setText(TranslationManager.getInstance().get(key));
                }
            }
        }
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;
                String key = getKeyFromButton(button);
                if (key != null) {
                    button.setText(TranslationManager.getInstance().get(key));
                }
            }
        }
    }

    private String getKeyFromLabel(Label label) {
        // Retorna a chave de tradução associada à label, se disponível
        switch (label.getText()) {
            case "Atualizar Dados": return "update.profile";
            case "Nome:": return "name";
            case "Email:": return "email";
            default: return null;
        }
    }

    private String getKeyFromButton(Button button) {
        // Retorna a chave de tradução associada ao botão, se disponível
        switch (button.getText()) {
            case "Salvar": return "save";
            case "Cancelar": return "cancel";
            default: return null;
        }
    }

    private void salvarDados(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            System.out.println(TranslationManager.getInstance().get("updating.data"));
            controller.NovoCadastroUsuario(usuario, usuario.getLogin(), usuario.getSenha(), nome, email, armazenamento);
            System.out.println(TranslationManager.getInstance().get("data.updated"));

            showAlert(TranslationManager.getInstance().get("success"), TranslationManager.getInstance().get("data.updated.success"));
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        } catch (Exception e) {
            System.err.println(TranslationManager.getInstance().get("error.updating.data") + ": " + e.getMessage());
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.updating.data"));
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
