package com.vendasInterface;

import com.example.Armazenamento;
import com.example.Controller;
import com.example.Evento;
import com.example.Ingresso;
import com.example.Recibo;
import com.example.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppScreenView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public AppScreenView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        // Lado esquerdo: informações do usuário
        VBox userInfoBox = createUserInfoBox();

        // Lado direito: lista de eventos
        ScrollPane eventListPane = createEventListPane();

        // Layout principal
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setStyle("-fx-padding: 20;");

        mainLayout.getChildren().addAll(userInfoBox, eventListPane);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setTitle("User Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createUserInfoBox() {
        VBox userInfoBox = new VBox(15);
        userInfoBox.setAlignment(Pos.TOP_LEFT);
        userInfoBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 20;");
        userInfoBox.setPrefWidth(250);

        Label titleLabel = new Label("Informações do Usuário");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label welcomeLabel = new Label("Bem-vindo, " + usuario.getNome() + "!");
        Label usernameLabel = new Label("Usuário: " + usuario.getLogin());
        Label cpfLabel = new Label("CPF: " + usuario.getCpf());
        Label emailLabel = new Label("Email: " + usuario.getEmail());

       // Botão para listar ingressos comprados
        Button listarIngressosButton = new Button("Listar Ingressos");
        listarIngressosButton.setOnAction(event -> {
            new ListarIngressosView(stage, usuario, controller, armazenamento).show();
        });

        // Botão para listar recibos
        Button listarRecibosButton = new Button("Listar Recibos");
        listarRecibosButton.setOnAction(event -> {
            new ListarRecibosView(stage, usuario, controller, armazenamento).show();
        });

        // Botão para atualizar cadastro
        Button atualizarCadastroButton = new Button("Atualizar Cadastro");
        atualizarCadastroButton.setOnAction(event -> abrirAtualizarDados());

        // Botão para logout
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> new LoginRegisterView(stage).show());

        // Adicionando todos os elementos à VBox
        userInfoBox.getChildren().addAll(
            titleLabel, welcomeLabel, usernameLabel, cpfLabel, emailLabel,
            listarIngressosButton, listarRecibosButton, atualizarCadastroButton, logoutButton
        );

        return userInfoBox;
    }

    private void abrirAtualizarDados() {
        // Abre a tela de confirmação de login antes de atualizar os dados
        new ConfirmarLoginView(stage, usuario, controller, armazenamento).show();
    }

    private ScrollPane createEventListPane() {
        VBox eventListBox = new VBox(10);
        eventListBox.setStyle("-fx-padding: 20;");
        eventListBox.setAlignment(Pos.TOP_LEFT);

        Label eventListLabel = new Label("Eventos Disponíveis:");
        eventListLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        eventListBox.getChildren().add(eventListLabel);

        // Obter lista de IDs de eventos disponíveis
        List<String> eventosDisponiveis = armazenamento.listarEventosDisponiveis();

        // Verifica se a lista de eventos disponíveis não está vazia
        if (eventosDisponiveis.isEmpty()) {
            Label noEventsLabel = new Label("Nenhum evento disponível no momento.");
            eventListBox.getChildren().add(noEventsLabel);
        } else {
            // Exibir cada evento na interface
            for (String eventoID : eventosDisponiveis) {
                Evento evento = armazenamento.LerArquivoEvento(eventoID);

                if (evento != null) {
                    VBox eventBox = new VBox(5);
                    eventBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                    Label eventName = new Label("Nome: " + evento.getNome());
                    Label eventDescription = new Label("Descrição: " + evento.getDescricao());
                    Label eventDate = new Label("Data: " + formatDate(evento.getData()));
                    Label ticketsAvailable = new Label("Ingressos Disponíveis: " + evento.getIngressos());

                    Button buyButton = new Button("Comprar Ingresso");
                    buyButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    buyButton.setOnAction(e -> handleBuyTicket(evento));

                    eventBox.getChildren().addAll(eventName, eventDescription, eventDate, ticketsAvailable, buyButton);
                    eventListBox.getChildren().add(eventBox);
                } else {
                    System.err.println("Evento não encontrado: " + eventoID);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(eventListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9f9f9;");
        scrollPane.setPrefWidth(600);

        return scrollPane;
    }

    private void handleBuyTicket(Evento evento) {
        try {
            if (evento.getIngressos() > 0) {
                controller.comprarIngresso(usuario, evento.getID(), armazenamento, "Cartão de Crédito", new Date());
                showAlert("Sucesso", "Ingresso comprado com sucesso para " + evento.getNome());
                show(); // Atualiza a tela
            } else {
                showAlert("Erro", "Sem ingressos disponíveis para " + evento.getNome());
            }
        } catch (Exception e) {
            showAlert("Erro", "Falha ao comprar ingresso: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
