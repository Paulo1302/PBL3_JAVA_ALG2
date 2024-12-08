package com.vendasInterface;

import com.example.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComprarIngressoView {

    private final Stage stage;
    private final Usuario usuario;
    private final Evento evento;
    private final Controller controller;
    private final Armazenamento armazenamento;
    private final Ingresso ingresso;
    private String selectedPaymentMethod;

    public ComprarIngressoView(Stage stage, Usuario usuario, Evento evento, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.evento = evento;
        this.controller = controller;
        this.armazenamento = armazenamento;
        this.ingresso = new Ingresso(evento, evento.getPreco());
        this.selectedPaymentMethod = null;
    }

    public void show() {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 30; -fx-background-color: #f9f9f9;");

        // Título
        Label titleLabel = new Label(TranslationManager.getInstance().get("buy.ticket"));
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Informações do evento
        Label eventName = new Label(TranslationManager.getInstance().get("event.name") + ": " + evento.getNome());
        Label eventDescription = new Label(TranslationManager.getInstance().get("event.description") + ": " + evento.getDescricao());
        Label eventDate = new Label(TranslationManager.getInstance().get("event.date") + ": " + formatDate(evento.getData()));
        Label ticketPrice = new Label(TranslationManager.getInstance().get("event.tickets.price") + ": R$ " + ingresso.getPreco());

        eventName.setStyle("-fx-font-size: 16;");
        eventDescription.setStyle("-fx-font-size: 14;");
        eventDate.setStyle("-fx-font-size: 14;");
        ticketPrice.setStyle("-fx-font-size: 14;");

        // Forma de pagamento
        Label paymentLabel = new Label(TranslationManager.getInstance().get("select.payment"));
        paymentLabel.setStyle("-fx-font-size: 14;");

        ComboBox<String> paymentOptions = new ComboBox<>();
        paymentOptions.getItems().addAll(
                TranslationManager.getInstance().get("credit.card"),
                TranslationManager.getInstance().get("bank.billet"),
                TranslationManager.getInstance().get("pix")
        );
        paymentOptions.setPromptText(TranslationManager.getInstance().get("select.payment"));

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button nextButton = new Button(TranslationManager.getInstance().get("next"));
        nextButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        nextButton.setOnAction(e -> {
            selectedPaymentMethod = paymentOptions.getValue();
            if (selectedPaymentMethod == null || selectedPaymentMethod.isEmpty()) {
                showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.select.payment"));
            } else {
                showConfirmationScreen();
            }
        });

        Button cancelButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        buttonBox.getChildren().addAll(nextButton, cancelButton);

        mainBox.getChildren().addAll(titleLabel, eventName, eventDescription, eventDate, ticketPrice, paymentLabel, paymentOptions, buttonBox);

        Scene scene = new Scene(mainBox, 1000, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("buy.ticket"));
        stage.show();

        // Adiciona um listener para detectar mudanças de idioma e recarregar a tela
        TranslationManager.getInstance().addLanguageChangeListener(this::refreshUI);
    }

    private void showConfirmationScreen() {
        VBox confirmationBox = new VBox(20);
        confirmationBox.setAlignment(Pos.CENTER);
        confirmationBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 30; -fx-background-color: #e0f7fa;");

        Label titleLabel = new Label(TranslationManager.getInstance().get("purchase.confirmation"));
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label eventName = new Label(TranslationManager.getInstance().get("event.name") + ": " + evento.getNome());
        Label eventDate = new Label(TranslationManager.getInstance().get("event.date") + ": " + formatDate(evento.getData()));
        Label ticketPrice = new Label(TranslationManager.getInstance().get("event.tickets.price") + ": R$ " + ingresso.getPreco());
        Label paymentMethod = new Label(TranslationManager.getInstance().get("payment.method") + ": " + selectedPaymentMethod);

        eventName.setStyle("-fx-font-size: 16;");
        eventDate.setStyle("-fx-font-size: 14;");
        ticketPrice.setStyle("-fx-font-size: 14;");
        paymentMethod.setStyle("-fx-font-size: 14;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button(TranslationManager.getInstance().get("confirm.purchase"));
        confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> handlePurchase());

        Button cancelButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> show());

        buttonBox.getChildren().addAll(confirmButton, cancelButton);

        confirmationBox.getChildren().addAll(titleLabel, eventName, eventDate, ticketPrice, paymentMethod, buttonBox);

        Scene scene = new Scene(confirmationBox, 1000, 600);
        stage.setScene(scene);
    }

    private void handlePurchase() {
        try {
            if (evento.getIngressos() > 0) {
                controller.comprarIngresso(usuario, evento.getID(), armazenamento, selectedPaymentMethod, new Date());

                String purchaseDetails = TranslationManager.getInstance().get("purchase.success") +
                        "\n" + TranslationManager.getInstance().get("event.name") + ": " + evento.getNome() +
                        "\n" + TranslationManager.getInstance().get("payment.method") + ": " + selectedPaymentMethod +
                        "\n" + TranslationManager.getInstance().get("purchase.date") + ": " + formatDate(new Date()) +
                        "\n" + TranslationManager.getInstance().get("receipt.sent") + ": " + usuario.getEmail();

                showAlert(TranslationManager.getInstance().get("success"), purchaseDetails);
                showAlert(TranslationManager.getInstance().get("notification"), TranslationManager.getInstance().get("purchase.success.notification"));

                new AppScreenView(stage, usuario, controller, armazenamento).show();
            } else {
                showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("no.tickets"));
            }
        } catch (Exception e) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("purchase.failed") + ": " + e.getMessage());
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

    private void refreshUI() {
        // Atualiza os textos da interface quando o idioma é alterado
        stage.setTitle(TranslationManager.getInstance().get("buy.ticket"));
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
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".combo-box")) {
            if (node instanceof ComboBox) {
                ComboBox<?> comboBox = (ComboBox<?>) node;
                if (comboBox.getPromptText() != null) {
                    comboBox.setPromptText(TranslationManager.getInstance().get(comboBox.getPromptText()));
                }
            }
        }
    }

    private String getKeyFromLabel(Label label) {
        // Mapear o texto do Label para a chave de tradução
        return null; // Implementar mapeamento específico, se necessário
    }

    private String getKeyFromButton(Button button) {
        // Mapear o texto do Button para a chave de tradução
        return null; // Implementar mapeamento específico, se necessário
    }
}
