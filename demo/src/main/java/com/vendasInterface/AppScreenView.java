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

        // Adiciona listener para mudar a interface quando o idioma for alterado
        TranslationManager.getInstance().addLanguageChangeListener(this::refreshUI);
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
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
        stage.setScene(scene);
        stage.show();
    }

    private VBox createUserInfoBox() {
        VBox userInfoBox = new VBox(10);
        userInfoBox.setAlignment(Pos.TOP_LEFT);
        userInfoBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #ffffff;");
        userInfoBox.setPrefWidth(250);

        Label titleLabel = new Label(TranslationManager.getInstance().get("user.info"));
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label welcomeLabel = new Label(TranslationManager.getInstance().get("welcome", usuario.getNome()));
        Label usernameLabel = new Label(TranslationManager.getInstance().get("username", usuario.getLogin()));
        Label cpfLabel = new Label(TranslationManager.getInstance().get("cpf", usuario.getCpf()));
        Label emailLabel = new Label(TranslationManager.getInstance().get("email", usuario.getEmail()));

        Button listarIngressosButton = new Button(TranslationManager.getInstance().get("list.tickets"));
        listarIngressosButton.setOnAction(event -> new ListarIngressosView(stage, usuario, controller, armazenamento).show());

        Button listarRecibosButton = new Button(TranslationManager.getInstance().get("list.receipts"));
        listarRecibosButton.setOnAction(event -> new ListarRecibosView(stage, usuario, controller, armazenamento).show());

        Button atualizarCadastroButton = new Button(TranslationManager.getInstance().get("update.profile"));
        atualizarCadastroButton.setOnAction(event -> abrirAtualizarDados());

        Button logoutButton = new Button(TranslationManager.getInstance().get("logout"));
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

        Label eventListLabel = new Label(TranslationManager.getInstance().get("available.events"));
        eventListLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        eventListBox.getChildren().add(eventListLabel);

        List<String> eventosDisponiveis = armazenamento.listarEventosDisponiveis();

        if (eventosDisponiveis.isEmpty()) {
            Label noEventsLabel = new Label(TranslationManager.getInstance().get("no.events"));
            eventListBox.getChildren().add(noEventsLabel);
        } else {
            for (String eventoID : eventosDisponiveis) {
                Evento evento = armazenamento.LerArquivoEvento(eventoID);

                if (evento != null) {
                    VBox eventBox = new VBox(8);
                    eventBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f0f0f0;");

                    Label eventName = new Label(TranslationManager.getInstance().get("event.name") + ": " + evento.getNome());
                    Label eventDescription = new Label(TranslationManager.getInstance().get("event.description") + ": " + evento.getDescricao());
                    Label eventDate = new Label(TranslationManager.getInstance().get("event.date") + ": " + formatDate(evento.getData()));
                    Label ticketsAvailable = new Label(TranslationManager.getInstance().get("event.tickets.available", evento.getIngressos()));

                    Button buyButton = new Button(TranslationManager.getInstance().get("buy.ticket"));
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

        Label notificationTitle = new Label(TranslationManager.getInstance().get("notifications"));
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

    // Método para atualizar a interface de acordo com a mudança de idioma
    private void refreshUI() {
        // Atualiza os textos da interface quando o idioma é alterado
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
        // Outros componentes de UI podem ser atualizados conforme necessário
    }
}
