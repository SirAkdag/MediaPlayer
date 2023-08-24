package de.fernschulen.mediaplayer;

import java.awt.event.MouseEvent;
import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javax.swing.*;

public class FXMLController {
    //für die Bühne
    private Stage meineStage;
    //für den Player
    private MediaPlayer mediaplayer;

    //für die MediaView
    @FXML
    private MediaView mediaview;
    //für die ImageView mit dem Symbol
    @FXML
    private ImageView symbol;
    //für das Listenfeld
    @FXML
    private ListView<String> liste;

    //für die ImageView mit dem Symbol
    @FXML
    private ImageView playButtonImage;

    @FXML
    private Button playButton;

    //die Methode setzt die Bühne auf den übergebenen Wert
    public void setMeineStage(Stage meineStage) {
        //Aufgabe 3: Da beim Start des Programms keine Dateien geladen werden,
        //sind die Symbole für die Steuerung der Wiedergabe deaktiviert.
        playButton.setDisable(true);
        this.meineStage = meineStage;
    }

    //die Methode zum Laden
    @FXML
    protected void ladenKlick(ActionEvent event) {
        //eine neue Instanz der Klasse FileChooser erzeugen
        FileChooser oeffnenDialog = new FileChooser();
        //den Titel für den Dialog setzen
        oeffnenDialog.setTitle("Datei öffnen");
        //die Filter setzen
        oeffnenDialog.getExtensionFilters().add(new ExtensionFilter("Audiodateien", "*.mp3"));
        oeffnenDialog.getExtensionFilters().add(new ExtensionFilter("Videodateien", "*.mp4"));

        //den Startordner auf den Benutzerordner setzen
        oeffnenDialog.setInitialDirectory(new File(System.getProperty("user.home")));

        //den Öffnendialog anzeigen und das Ergebnis beschaffen
        File datei = oeffnenDialog.showOpenDialog(meineStage);
        //wurde eine Datei ausgewählt
        if (datei != null)

            //dann über eine eigene Methode laden
            dateiLaden(datei);
    }


    //die Methode für die Pause
    @FXML
    protected void pauseKlick(ActionEvent event) {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            //dann unterbrechen
            mediaplayer.pause();
            //Aufgabe 2: wird das Schaltflächenbild auf "Play" gesetzt.
            playButtonImage.setImage(new Image("de/fernschulen/mediaplayer/icons/play.gif"));
        }
    }

    //die Methode für die Wiedergabe
    @FXML
    protected void playKlick(ActionEvent event) {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            //dann wiedergeben
            mediaplayer.play();
            //Aufgabe 2: wird das Schaltflächenbild auf "Pause" gesetzt.
            playButtonImage.setImage(new Image("de/fernschulen/mediaplayer/icons/pause.gif"));
        }
    }

    //die Methode für das Ein- und Ausschalten der Lautstärke
    @FXML
    protected void lautsprecherKlick(ActionEvent event) {
        //gibt es überhaupt einen Mediaplayer?
        String dateiname;
        if (mediaplayer != null) {
            //ist die Lautstärke 0?
            if (mediaplayer.getVolume() == 0) {
                //dann auf 100 setzen
                mediaplayer.setVolume(100);
                //und das "normale" Symbol setzen
                dateiname = "src/de/fernschulen/mediaplayer/icons/mute.gif";
            } else {
                //sonst auf 0 setzen
                mediaplayer.setVolume(0);
                //und das durchgestrichene Symbol setzen
                dateiname = "src/de/fernschulen/mediaplayer/icons/mute_off.gif";
            }
            //das Bild erzeugen und anzeigen
            File bilddatei = new File(dateiname);
            Image bild = new Image(bilddatei.toURI().toString());
            symbol.setImage(bild);
        }
    }

    //hier wurde MouseEvent erstellt.
    @FXML
    protected void initialize() {
        liste.setOnMouseClicked(event -> {
            // Ausgabe 1: mit einem Klick wird die Methode eingeschaltet
            if (event.getClickCount() == 1) {
                //Ausgabe 1: Die Variable selectedMediaPath wurde erstellt,
                //um die ausgewählte Datei in der Liste zu halten.
                String selectedMediaPath = liste.getSelectionModel().getSelectedItem();
                if (selectedMediaPath != null) {
                    mediaplayer.stop();
                    Media medium = new Media(selectedMediaPath);
                    mediaplayer = new MediaPlayer(medium);
                    mediaview.setMediaPlayer(mediaplayer);
                    //die Wiedergabe starten
                    mediaplayer.play();

                    //Aufgabe 1:Die Methode "clearSelection" wurde verwendet,
                    //damit keine ausgewählte Datei in der Liste vorhanden ist.
                    //Andernfalls wird der MediaPlayer neu gestartet, wenn Sie mit der Maus irgendwo klicken.
                    liste.getSelectionModel().clearSelection();
                    //Ausgabe 2:Wenn eine Datei geladen ist und der MediaPlayer läuft,
                    //wird das Schaltflächenbild auf "Pause" gesetzt.
                    playButtonImage.setImage(new Image("de/fernschulen/mediaplayer/icons/pause.gif"));
                }
            }
        });
    }

    ////die Methode für Play- und PauseImage
    @FXML
    protected void buttonClicked(ActionEvent event) {
        String buttonDateiName;
        //Ausgabe 2: gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            if (mediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                //Ausgabe 2:Bei laufender Wiedergabe wird beim Aufruf der buttonClicked-Methode
                //die Schaltflächenanzeige auf "Play" geändert und
                //die Wiedergabe mit der pauseKlick-Methode angehalten.
                buttonDateiName = "src/de/fernschulen/mediaplayer/icons/play.gif";
                pauseKlick(event);
            } else {
                //Ausgabe 2:Wenn die buttonClicked-Methode aufgerufen wird,
                //während die Wiedergabe gestoppt ist, wird die Schaltflächenanzeige in "Pause" geändert und
                //die Wiedergabe mit der 'playKlick'-Methode weitergeführt.
                buttonDateiName = "src/de/fernschulen/mediaplayer/icons/pause.gif";
                playKlick(event);
                //Bei laufender Wiedergabe wird das Bild der Taste auf "Pause" geändert.
            }
            //Ausgabe 2:das Bild erzeugen und anzeigen
            File buttonBildDatei = new File(buttonDateiName);
            Image bild = new Image(buttonBildDatei.toURI().toString());
            playButtonImage.setImage(bild);

        }
    }


    //die Methode zum Beenden
    @FXML
    protected void beendenKlick(ActionEvent event) {
        Platform.exit();
    }

    //die Methode lädt eine Datei
    public void dateiLaden(File datei) {
        //läuft schon eine Wiedergabe?
        if (mediaplayer != null && mediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
            //dann anhalten
            mediaplayer.stop();
            //Ausgabe 2: hier wurde die Schaltflächenanzeige auf 'Play' geändert
            playButtonImage.setImage(new Image("de/fernschulen/mediaplayer/icons/play.gif"));
        }
        //Ausgabe 1: Die Methode "contains" wird verwendet, um zu prüfen,
        //ob die ausgewählte Datei in der Liste steht.
        String mediaPath = datei.toURI().toString();
        if (!liste.getItems().contains(mediaPath)) {
            try {
                //das Medium, den Mediaplayer und die MediaView erzeugen
                Media medium = new Media(datei.toURI().toString());
                mediaplayer = new MediaPlayer(medium);
                mediaview.setMediaPlayer(mediaplayer);
                //die Wiedergabe starten
                mediaplayer.play();
                //Ausgabe 3: hier wurde die Symbole für die Steuerung der Wiedergabe wieder aktiviert.
                playButton.setDisable(false);

                //Ausgabe 2:Wenn eine Datei geladen ist und der MediaPlayer läuft,
                //wird das Schaltflächenbild auf "Pause" gesetzt.
                playButtonImage.setImage(new Image("de/fernschulen/mediaplayer/icons/pause.gif"));

                //den Pfad in das Listenfeld eintragen
                liste.getItems().add(mediaPath);
                //und die Titelleiste anpassen
                meineStage.setTitle("JavaFX Multimedia-Player " + datei.toString());
            } catch (Exception ex) {
                //den Dialog erzeugen und anzeigen
                Alert meinDialog = new Alert(AlertType.INFORMATION, "Beim Laden hat es ein Problem gegeben.\n" + ex.getMessage());
                //den Text setzen
                meinDialog.setHeaderText("Bitte beachten");
                meinDialog.initOwner(meineStage);
                //den Dialog anzeigen
                meinDialog.showAndWait();
            }
        } else {
            //Ausgabe 1: hier wird eine Fehlermeldung erzeugt, wenn die ausgewählte Datei in der Liste steht.
            Alert errorDialog = new Alert(AlertType.ERROR, "Die ausgewählte Datei ist bereits vorhanden.");
            errorDialog.setHeaderText("Error");
            errorDialog.initOwner(meineStage);
            errorDialog.showAndWait();
        }


    }
}
