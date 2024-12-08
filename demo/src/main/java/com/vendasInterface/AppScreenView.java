package com.vendasInterface;

import com.example.*;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppScreenView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    private VBox notificationBox;
    private List<Ingresso> lastCheckedIngressos;

    public AppScreenView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
        this.lastCheckedIngressos = new ArrayList<>(usuario.getIngressos());
    }
    

    public void show() {
        VBox userInfoBox = createUserInfoBox();
        ScrollPane eventListPane = createEventListPane();
        notificationBox = createNotificationBox();

        HBox mainLayout = new HBox(10);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setStyle("-fx-padding: 20;");

        mainLayout.getChildren().addAll(userInfoBox, eventListPane, notificationBox);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setTitle("User Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createUserInfoBox() {
        VBox userInfoBox = new VBox(10);
        userInfoBox.setAlignment(Pos.TOP_LEFT);
        userInfoBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #ffffff;");
        userInfoBox.setPrefWidth(250);

        Label titleLabel = new Label("Informações do Usuário");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label welcomeLabel = new Label("Bem-vindo, " + usuario.getNome() + "!");
        Label usernameLabel = new Label("Usuário: " + usuario.getLogin());
        Label cpfLabel = new Label("CPF: " + usuario.getCpf());
        Label emailLabel = new Label("Email: " + usuario.getEmail());

        Button listarIngressosButton = new Button("Listar Ingressos");
        listarIngressosButton.setOnAction(event -> new ListarIngressosView(stage, usuario, controller, armazenamento).show());

        Button listarRecibosButton = new Button("Listar Recibos");
        listarRecibosButton.setOnAction(event -> new ListarRecibosView(stage, usuario, controller, armazenamento).show());

        Button atualizarCadastroButton = new Button("Atualizar Cadastro");
        atualizarCadastroButton.setOnAction(event -> abrirAtualizarDados());

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> new LoginRegisterView(stage).show());

        userInfoBox.getChildren().addAll(
                titleLabel, welcomeLabel, usernameLabel, cpfLabel, emailLabel,
                listarIngressosButton, listarRecibosButton, atualizarCadastroButton, logoutButton
        );

        return userInfoBox;
    }

    private void abrirAtualizarDados() {
        new ConfirmarLoginView(stage, usuario, controller, armazenamento).show();
    }

    private ScrollPane createEventListPane() {
        VBox eventListBox = new VBox(15);
        eventListBox.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");
        eventListBox.setAlignment(Pos.TOP_LEFT);

        Label eventListLabel = new Label("Eventos Disponíveis:");
        eventListLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        eventListBox.getChildren().add(eventListLabel);

        List<String> eventosDisponiveis = armazenamento.listarEventosDisponiveis();

        if (eventosDisponiveis.isEmpty()) {
            Label noEventsLabel = new Label("Nenhum evento disponível no momento.");
            eventListBox.getChildren().add(noEventsLabel);
        } else {
            for (String eventoID : eventosDisponiveis) {
                Evento evento = armazenamento.LerArquivoEvento(eventoID);

                if (evento != null) {
                    VBox eventBox = new VBox(8);
                    eventBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f0f0f0;");

                    Label eventName = new Label("Nome: " + evento.getNome());
                    Label eventDescription = new Label("Descrição: " + evento.getDescricao());
                    Label eventDate = new Label("Data: " + formatDate(evento.getData()));
                    Label ticketsAvailable = new Label("Ingressos Disponíveis: " + evento.getIngressos());

                    Button buyButton = new Button("Comprar Ingresso");
                    buyButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    buyButton.setOnAction(e -> openBuyTicketView(evento));

                    eventBox.getChildren().addAll(eventName, eventDescription, eventDate, ticketsAvailable, buyButton);
                    eventListBox.getChildren().add(eventBox);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(eventListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9f9f9;");
        scrollPane.setPrefWidth(600);

        return scrollPane;
    }

    private VBox createNotificationBox() {
        VBox notificationBox = new VBox(15);
        notificationBox.setAlignment(Pos.TOP_LEFT);
        notificationBox.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 15;");
        notificationBox.setPrefWidth(300);

        Label notificationTitle = new Label("Notificações:");
        notificationTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        notificationBox.getChildren().add(notificationTitle);

        return notificationBox;
    }

    private void openBuyTicketView(Evento evento) {
        new ComprarIngressoView(stage, usuario, evento, controller, armazenamento).show();
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
