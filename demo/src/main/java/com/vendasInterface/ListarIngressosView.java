package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Evento;
import com.example.Ingresso;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Date;
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
        BorderPane mainLayout = new BorderPane();

        HBox contentLayout = new HBox(20); // Layout horizontal para ingressos
        contentLayout.setStyle("-fx-padding: 20;");

        // Coluna de ingressos válidos
        VBox validTicketsLayout = new VBox(15);
        validTicketsLayout.setAlignment(Pos.TOP_LEFT);
        validTicketsLayout.setPrefWidth(400);

        Label validTicketsLabel = new Label(TranslationManager.getInstance().get("valid.tickets"));
        validTicketsLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        validTicketsLayout.getChildren().add(validTicketsLabel);

        // Coluna de ingressos inválidos
        VBox invalidTicketsLayout = new VBox(15);
        invalidTicketsLayout.setAlignment(Pos.TOP_LEFT);
        invalidTicketsLayout.setPrefWidth(400);

        Label invalidTicketsLabel = new Label(TranslationManager.getInstance().get("invalid.tickets"));
        invalidTicketsLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        invalidTicketsLayout.getChildren().add(invalidTicketsLabel);

        // Carrega os ingressos do usuário
        List<Ingresso> ingressos = usuario.getIngressos();

        if (ingressos.isEmpty()) {
            Label noTicketsLabel = new Label(TranslationManager.getInstance().get("no.tickets"));
            validTicketsLayout.getChildren().add(noTicketsLabel);
        } else {
            for (Ingresso ingresso : ingressos) {
                VBox ingressoBox = new VBox(5);
                ingressoBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                Label ingressoInfo = new Label(TranslationManager.getInstance().get("ticket.id") + ": " + ingresso.getId());
                Label eventoInfo = new Label(TranslationManager.getInstance().get("event.id") + ": " + ingresso.getEventoID());
                Label precoInfo = new Label(TranslationManager.getInstance().get("price") + ": R$" + ingresso.getPreco());

                if (ingresso.isAtivo()) {
                    // Botão para cancelar ingresso
                    Button cancelButton = new Button(TranslationManager.getInstance().get("cancel.ticket"));
                    cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    cancelButton.setOnAction(event -> {
                        boolean sucesso = controller.cancelarCompra(usuario, ingresso, new Date(), armazenamento);
                        if (sucesso) {
                            show(); // Atualiza a tela
                        } else {
                            mostrarErro(TranslationManager.getInstance().get("error.cancel.ticket"));
                        }
                    });

                    ingressoBox.getChildren().addAll(ingressoInfo, eventoInfo, precoInfo, cancelButton);
                    validTicketsLayout.getChildren().add(ingressoBox);
                } else {
                    Evento evento = armazenamento.LerArquivoEvento(ingresso.getEventoID());
                    Label statusInfo = new Label(TranslationManager.getInstance().get("status.cancelled"));

                    // Campo e botão para adicionar avaliação
                    HBox evaluationBox = new HBox(5);
                    TextField evaluationField = new TextField();
                    evaluationField.setPromptText(TranslationManager.getInstance().get("rate.event"));
                    Button evaluationButton = new Button(TranslationManager.getInstance().get("send.review"));
                    evaluationButton.setOnAction(event -> {
                        String avaliacao = evaluationField.getText();
                        if (!avaliacao.isEmpty()) {
                            try {
                                controller.avaliarEvento(evento, usuario, avaliacao, armazenamento);
                                mostrarMensagem(TranslationManager.getInstance().get("review.success"));
                                evaluationField.clear();
                            } catch (SecurityException e) {
                                mostrarErro(TranslationManager.getInstance().get("error.no.review"));
                            }
                        }
                    });

                    evaluationBox.getChildren().addAll(evaluationField, evaluationButton);
                    ingressoBox.getChildren().addAll(ingressoInfo, eventoInfo, statusInfo, evaluationBox);
                    invalidTicketsLayout.getChildren().add(ingressoBox);
                }
            }
        }

        // Adiciona as colunas ao layout horizontal com rolagem
        ScrollPane validTicketsScrollPane = new ScrollPane(validTicketsLayout);
        validTicketsScrollPane.setFitToWidth(true);
        validTicketsScrollPane.setPrefHeight(500);

        ScrollPane invalidTicketsScrollPane = new ScrollPane(invalidTicketsLayout);
        invalidTicketsScrollPane.setFitToWidth(true);
        invalidTicketsScrollPane.setPrefHeight(500);

        contentLayout.getChildren().addAll(validTicketsScrollPane, invalidTicketsScrollPane);

        // Botão para voltar à página inicial
        Button backButton = new Button(TranslationManager.getInstance().get("back.to.home"));
        backButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        HBox footerLayout = new HBox(backButton);
        footerLayout.setAlignment(Pos.CENTER);
        footerLayout.setStyle("-fx-padding: 10;");

        // Adiciona as áreas ao layout principal
        mainLayout.setCenter(contentLayout);
        mainLayout.setBottom(footerLayout);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("purchased.tickets"));
        stage.setResizable(true);
        stage.show();
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
