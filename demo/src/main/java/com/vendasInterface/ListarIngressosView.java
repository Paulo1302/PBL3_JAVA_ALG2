package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Ingresso;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ListarIngressosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public ListarIngressosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setStyle("-fx-padding: 20;");

        Label titleLabel = new Label("Ingressos Comprados");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Adiciona o título ao layout
        layout.getChildren().add(titleLabel);

        // Lista de ingressos comprados pelo usuário
        List<Ingresso> ingressos = controller.listarIngressosComprados(usuario);

        if (ingressos.isEmpty()) {
            Label noTicketsLabel = new Label("Você não comprou nenhum ingresso.");
            layout.getChildren().add(noTicketsLabel);
        } else {
            for (Ingresso ingresso : ingressos) {
                VBox ingressoBox = new VBox(5);
                ingressoBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                // Informações do ingresso
                Label ingressoInfo = new Label("ID do Ingresso: " + ingresso.getId());
                Label eventoInfo = new Label("ID do Evento: " + ingresso.getEventoID());
                Label precoInfo = new Label("Preço: R$" + ingresso.getPreco());
                Label statusInfo = new Label("Status: " + (ingresso.isAtivo() ? "Ativo" : "Cancelado"));

                // Adiciona os detalhes do ingresso ao layout
                ingressoBox.getChildren().addAll(ingressoInfo, eventoInfo, precoInfo, statusInfo);
                layout.getChildren().add(ingressoBox);
            }
        }

        // Botão de volta
        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> {
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        });
        layout.getChildren().add(backButton);

        // Cria um ScrollPane para permitir rolagem
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true); // Ajusta a largura para o tamanho da tela
        scrollPane.setFitToHeight(true); // Ajusta a altura para o tamanho da tela

        // Define o tamanho padrão da cena
        Scene scene = new Scene(scrollPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Ingressos Comprados");
        stage.setResizable(true); // Permite redimensionar a janela
        stage.show();
    }
}
