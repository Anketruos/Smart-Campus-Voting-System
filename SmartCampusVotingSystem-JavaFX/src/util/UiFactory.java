package util;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class UiFactory {

    private UiFactory() {
    }

    public static Label label(String text, String... styleClasses) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    public static VBox authHero(String eyebrow, String title, String body, String... points) {
        VBox hero = new VBox(16);
        hero.getStyleClass().add("hero-panel");
        hero.getChildren().addAll(
            label(eyebrow, "eyebrow", "hero-eyebrow"),
            label(title, "hero-title"),
            label(body, "hero-copy")
        );

        for (String point : points) {
            hero.getChildren().add(label("- " + point, "hero-point"));
        }

        return hero;
    }

    public static VBox authCard(String eyebrow, String title, String body, Node... children) {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.getChildren().addAll(
            label(eyebrow, "eyebrow"),
            label(title, "page-title"),
            label(body, "page-subtitle")
        );
        card.getChildren().addAll(children);
        return card;
    }

    public static VBox metricCard(String value, String caption) {
        VBox card = new VBox(6);
        card.getStyleClass().add("metric-card");
        card.getChildren().addAll(
            label(value, "metric-value"),
            label(caption, "metric-caption")
        );
        return card;
    }

    public static VBox detailCard(String title, String text) {
        VBox card = new VBox(10);
        card.getStyleClass().add("detail-card");
        card.getChildren().addAll(
            label(title, "section-title"),
            label(text, "section-copy")
        );
        return card;
    }

    public static Label chip(String text, String... styleClasses) {
        Label chip = label(text, "chip");
        chip.getStyleClass().addAll(styleClasses);
        return chip;
    }

    public static VBox heroStat(String value, String caption) {
        VBox stat = new VBox(2);
        stat.getStyleClass().add("hero-stat");
        stat.getChildren().addAll(
            label(value, "hero-stat-value"),
            label(caption, "hero-stat-caption")
        );
        return stat;
    }

    public static VBox fieldBlock(String labelText, Node input) {
        VBox block = new VBox(6);
        block.getStyleClass().add("input-group");
        block.getChildren().addAll(
            label(labelText, "field-label"),
            input
        );
        return block;
    }

    public static Button secondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        return button;
    }

    public static Button ghostButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("ghost-button");
        return button;
    }

    public static Button dangerButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("danger-button");
        return button;
    }

    public static VBox panel(Node... children) {
        VBox panel = new VBox(16, children);
        panel.getStyleClass().add("surface-panel");
        return panel;
    }

    public static VBox commandPanel(Node... children) {
        VBox panel = panel(children);
        panel.getStyleClass().add("command-panel");
        return panel;
    }

    public static HBox metricRow(Node... cards) {
        HBox row = new HBox(14, cards);
        row.getStyleClass().add("metric-row");
        return row;
    }

    public static Region spacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public static void makeWide(Button... buttons) {
        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
        }
    }

    public static VBox dialogContent(Node... children) {
        VBox box = new VBox(10, children);
        box.setPadding(new Insets(8));
        return box;
    }

    public static StackPane scrollPage(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("page-scroll");

        StackPane shell = new StackPane(scrollPane);
        shell.getStyleClass().add("page-shell");
        return shell;
    }
}
