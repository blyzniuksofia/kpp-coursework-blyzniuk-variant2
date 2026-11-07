package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.logging.Logger;


public class MainApp extends Application {
    private static final Logger LOG = Logger.getLogger(MainApp.class.getName());
    private final HttpEcoIncidentClient api = new HttpEcoIncidentClient();
    private final TableView<Location> locations = new TableView<>();
    private final TableView<Observation> observations = new TableView<>();
    private final TableView<Incident> incidents = new TableView<>();
    private final TableView<ResponseMeasure> responses = new TableView<>();
    @Override
    public void start(Stage stage) {
        TabPane tabs=new TabPane(
                new Tab("Локації",locationPane()),
                new Tab("Спостереження",observationPane()),
                new Tab("Інциденти",incidentPane()),
                new Tab("Заходи реагування",responsePane())
        );
        tabs.getTabs().forEach(t->t.setClosable(false));
        stage.setScene(new Scene(tabs,1050,650));
        stage.setTitle("Моніторинг екологічних інцидентів"); stage.show();
        refreshAll();
    }

    private Pane locationPane() {
        locations.getColumns().setAll(col("ID", "id"), col("Код", "code"), col("Назва", "name"), col("Місто", "city"));
        TextField code = new TextField(), name = new TextField(), city = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");
        add.setOnAction(e -> safe(() -> { api.addLocation(new Location(null, code.getText(), name.getText(), city.getText())); refreshLocations(); }));
        update.setOnAction(e -> safe(() -> { Location l = selected(locations); l.setCode(code.getText()); l.setName(name.getText()); l.setCity(city.getText()); api.updateLocation(l); refreshLocations(); }));
        delete.setOnAction(e -> safe(() -> { api.deleteLocation(selected(locations).getId()); refreshAll(); }));
        refresh.setOnAction(e -> refreshLocations());
        locations.getSelectionModel().selectedItemProperty().addListener((o, a, l) -> {
            if (l != null) { code.setText(l.getCode()); name.setText(l.getName()); city.setText(l.getCity()); }
        });
        return layout(locations, new VBox(8, new Label("Код"), code, new Label("Назва"), name, new Label("Місто"), city, new HBox(8, add, update, delete, refresh)));
    }

    private Pane observationPane() {
        observations.getColumns().setAll(col("ID", "id"), col("Локація", "locationId"), col("Показник", "indicator"),
                col("Значення", "observedValue"), col("Норматив", "limitValue"), col("Масштаб", "scaleScore"),
                col("Тривалість", "durationScore"), col("Дата", "observedDate"));
        TextField locationId = new TextField(), indicator = new TextField(), value = new TextField(), limit = new TextField(),
                scale = new TextField(), duration = new TextField(), date = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");
        add.setOnAction(e -> safe(() -> { api.addObservation(readObservation(null, locationId, indicator, value, limit, scale, duration, date)); refreshObservations(); }));
        update.setOnAction(e -> safe(() -> { Observation o = selected(observations); api.updateObservation(readObservation(o.getId(), locationId, indicator, value, limit, scale, duration, date)); refreshObservations(); }));
        delete.setOnAction(e -> safe(() -> { api.deleteObservation(selected(observations).getId()); refreshAll(); }));
        refresh.setOnAction(e -> refreshObservations());
        observations.getSelectionModel().selectedItemProperty().addListener((o, a, v) -> {
            if (v != null) {
                locationId.setText("" + v.getLocationId()); indicator.setText(v.getIndicator());
                value.setText("" + v.getObservedValue()); limit.setText("" + v.getLimitValue());
                scale.setText("" + v.getScaleScore()); duration.setText("" + v.getDurationScore());
                date.setText(v.getObservedDate());
            }
        });
        VBox form = new VBox(6, new Label("ID локації"), locationId, new Label("Показник"), indicator,
                new Label("Виміряне значення"), value, new Label("Норматив"), limit,
                new Label("Оцінка масштабу"), scale, new Label("Оцінка тривалості"), duration,
                new Label("Дата YYYY-MM-DD"), date, new HBox(8, add, update, delete, refresh));
        return layout(observations, form);
    }

    private Pane incidentPane() {
        incidents.getColumns().setAll(col("ID", "id"), col("Спостереження", "observationId"), col("Статус", "status"),
                col("F1", "f1"), col("F2", "f2"), col("F3", "f3"), col("Критичний індекс", "criticalIndex"));
        TextField observationId = new TextField();
        ComboBox<IncidentStatus> status = new ComboBox<>(FXCollections.observableArrayList(IncidentStatus.values()));
        Button add = new Button("Створити"), update = new Button("Перерахувати"), change = new Button("Змінити статус"),
                delete = new Button("Видалити"), refresh = new Button("Оновити");
        add.setOnAction(e -> safe(() -> { api.addIncident(Long.parseLong(observationId.getText())); refreshIncidents(); }));
        update.setOnAction(e -> safe(() -> { api.updateIncident(selected(incidents).getId(), Long.parseLong(observationId.getText())); refreshIncidents(); }));
        change.setOnAction(e -> safe(() -> { api.changeIncidentStatus(selected(incidents).getId(), status.getValue()); refreshIncidents(); }));
        delete.setOnAction(e -> safe(() -> { api.deleteIncident(selected(incidents).getId()); refreshAll(); }));
        refresh.setOnAction(e -> refreshIncidents());
        incidents.getSelectionModel().selectedItemProperty().addListener((o, a, v) -> {
            if (v != null) observationId.setText("" + v.getObservationId());
        });
        return layout(incidents, new VBox(8, new Label("ID спостереження"), observationId, new Label("Новий статус"), status,
                new HBox(8, add, update, change, delete, refresh)));
    }

    private Pane responsePane() {
        responses.getColumns().setAll(col("ID", "id"), col("Інцидент", "incidentId"), col("Опис", "description"), col("Дата", "plannedDate"));
        TextField incidentId = new TextField(), description = new TextField(), date = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");
        add.setOnAction(e -> safe(() -> { api.addResponse(new ResponseMeasure(null, Long.parseLong(incidentId.getText()), description.getText(), date.getText())); refreshResponses(); }));
        update.setOnAction(e -> safe(() -> { ResponseMeasure r = selected(responses); api.updateResponse(new ResponseMeasure(r.getId(), Long.parseLong(incidentId.getText()), description.getText(), date.getText())); refreshResponses(); }));
        delete.setOnAction(e -> safe(() -> { api.deleteResponse(selected(responses).getId()); refreshResponses(); }));
        refresh.setOnAction(e -> refreshResponses());
        responses.getSelectionModel().selectedItemProperty().addListener((o, a, r) -> {
            if (r != null) { incidentId.setText("" + r.getIncidentId()); description.setText(r.getDescription()); date.setText(r.getPlannedDate()); }
        });
        return layout(responses, new VBox(8, new Label("ID інциденту"), incidentId, new Label("Опис"), description,
                new Label("Дата YYYY-MM-DD"), date, new HBox(8, add, update, delete, refresh)));
    }

    private Observation readObservation(Long id, TextField locationId, TextField indicator, TextField value, TextField limit,
                                        TextField scale, TextField duration, TextField date) {
        return new Observation(id, Long.parseLong(locationId.getText()), indicator.getText(),
                Double.parseDouble(value.getText()), Double.parseDouble(limit.getText()),
                Double.parseDouble(scale.getText()), Double.parseDouble(duration.getText()), date.getText());
    }

    private void refreshAll() { refreshLocations(); refreshObservations(); refreshIncidents(); refreshResponses(); }
    private void refreshLocations() { safe(() -> locations.setItems(FXCollections.observableArrayList(api.getLocations()))); }
    private void refreshObservations() { safe(() -> observations.setItems(FXCollections.observableArrayList(api.getObservations()))); }
    private void refreshIncidents() { safe(() -> incidents.setItems(FXCollections.observableArrayList(api.getIncidents()))); }
    private void refreshResponses() { safe(() -> responses.setItems(FXCollections.observableArrayList(api.getResponses()))); }

    private <T> T selected(TableView<T> table) {
        T v = table.getSelectionModel().getSelectedItem();
        if (v == null) throw new IllegalArgumentException("Виберіть запис");
        return v;
    }
    private void safe(Runnable action) {
        try { action.run(); } catch (Exception e) { LOG.warning(e.getMessage()); showError(e); }
    }
    private void showError(Throwable e) {
        Alert d = new Alert(Alert.AlertType.ERROR, e == null ? "Помилка" : e.getMessage(), ButtonType.OK);
        d.showAndWait();
    }
    private Pane layout(TableView<?> table, Pane form) {
        SplitPane p = new SplitPane(table, form);
        p.setDividerPositions(0.68);
        BorderPane root = new BorderPane(p);
        root.setPadding(new Insets(10));
        return root;
    }
    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(property));
        return c;
    }
    public static void main(String[] args) { launch(args); }
}
