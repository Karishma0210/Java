package karaoke_cw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.Predicate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Karaoke_cw extends Application {
    
    
    MediaView mediaView;
    Slider timeSlider;
    Button playB;
    Button prevB;
    Button nextB;
    Button stopB;
    Button libButton = new Button("Show Library");
    Button plButton = new Button("Show Playlist");
    Button plCloseB = new Button(" X ");
    Button libCloseB = new Button(" X ");
    Button plDeleteB = new Button();
    TextField searchField = new TextField();
    Button addButton = new Button( "Add to playlist ");
    Button clrPLB = new Button( "Clear playlist ");
    boolean isPlaying=false;
    boolean isPlVisible=false;
    boolean isLibVisible=false;
    boolean isStopPressed=false;
    Map<String, Song> library;
    Queue<Song> playList;
    ObservableList<Song> lib;
    ObservableList<Song> pl;
    ListView<Song> libLV;
    ListView<Song> plLV;
    Song currS = new Song();
    //Runnable r1;
    Runnable r2;
    Runnable r3;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        //System.out.println(getParameters().getNamed().get("sampleFile"));
        
        playList = new LinkedList<>();
        plCloseB.setStyle("-fx-background-color: red;");
        libCloseB.setStyle("-fx-background-color: red;");
        
        //Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        //MediaView for holding mediaPlayer
        VBox mvContainer = new VBox();
        mediaView = new MediaView();
        mediaView.setFitHeight(210);
        mediaView.setFitWidth(360);
        mvContainer.setStyle("-fx-background-color: black");
        mvContainer.getChildren().add(mediaView);
        gridPane.add(mvContainer, 0, 0);
        GridPane.setColumnSpan(mvContainer, 2);
        
        //Label of current song being played
        VBox mp3L = new VBox();
        Label curMp3L = new Label("");
        mp3L.getChildren().add(curMp3L);
        gridPane.add(mp3L, 0, 1);
        //Text mpStatus = new Text();
        //mp3L.getChildren().add(mpStatus);
        GridPane.setColumnSpan(mp3L, 2);
        mp3L.setAlignment(Pos.BASELINE_CENTER);
        
        //alert for empty playlist
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Empty Playlist!!!");
        alert.setContentText("First add songs in Playlist");
        
        /*
        //Listener for mediaPlayer.Status
        r1 = () -> {
            try{
                mediaView.getMediaPlayer().statusProperty().addListener((observable, oldValue, newValue) -> {
                        mpStatus.setText(newValue.toString());

                });
            }
            catch(NullPointerException ex){
                //do nothing, exception has occured because mediaPlayer
                //..for mediaView has not been initialized yet
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        };
        new Thread(r1).start();
        */
        
        //when stopTime has come for mediaPlayer
        r2 = () -> {
            try{
                mediaView.getMediaPlayer().setOnEndOfMedia(() -> {
                    
                    if(playList.peek() != null){
                        try{
                            Media video = new Media(new File(playList.peek().getVideoFile()).toURI().toString());
                            MediaPlayer mediaPlayer = new MediaPlayer(video);
                            mediaView.setMediaPlayer(mediaPlayer);
                            //new Thread(r1).start();
                            new Thread(r2).start();
                            new Thread(r3).start();
                        }catch(Exception ex){
                            System.out.println("File not found");
                        }
                    }
                    else{
                        curMp3L.setText("");
                        isPlaying = false;
                        playB.setText(" > ");
                    }
                    
                    //set status as stop - if status is READY then it won't work
                    //.. but this is required as we need to play songs from next playlist
                    mediaView.getMediaPlayer().stop();
                });
            }
            catch(NullPointerException ex){
                //do nothing, exception has occured because mediaPlayer
                //..for mediaView has not been initialized yet
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        };
        new Thread(r2).start();
        
        //for autoplying next song from queue if status=READY
        r3 = () -> {
            try{
                mediaView.getMediaPlayer().setOnReady(() -> {
                    
                    if((currS = playList.poll()) != null){
                        
                        mediaView.getMediaPlayer().setStopTime(Duration.seconds(currS.getDuration()));
                        curMp3L.setText(currS.toString());
                        isPlaying = true;
                        playB.setText(" | | ");
                        mediaView.getMediaPlayer().play();
                        
                        if(isPlVisible){
                            pl = FXCollections.<Song>observableList((List)playList);
                            plLV.setItems(pl);
                        }
                    }
                    
                });
            }
            catch(NullPointerException ex){
                //do nothing, exception has occured because mediaPlayer
                //..for mediaView has not been initialized yet
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        };
        new Thread(r3).start();
        
        //load songs in library
        try{
            loadLibrary();
        } catch (IOException ex) {
            System.out.println("something wrong with loadLibrary()");
            //Logger.getLogger(Karaoke_cw.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lib = FXCollections.observableArrayList();
        lib.setAll(library.values());
        libLV = new ListView<>(lib);
                
        //container for control buttons of player
        HBox mControllerBox = new HBox();
        
        playB = new Button(" > ");
        playB.setTooltip(new Tooltip("Play/ Pause Button"));
        playB.setOnAction((ActionEvent e) -> {
            //to handle play/ pause event
            if(isPlaying){   //user is pressing on pause button
                mediaView.getMediaPlayer().pause();
                isPlaying = false;
                playB.setText(" > ");
            }
            else{   //user is pressing on play button
                
                //if mediaPlayer has not been initialized yet
                if(mediaView.getMediaPlayer() == null ){
                    
                    if(playList.peek() != null){
                        try{
                            Media video = new Media(new File(playList.peek().getVideoFile()).toURI().toString());
                            MediaPlayer mediaPlayer = new MediaPlayer(video);
                            mediaView.setMediaPlayer(mediaPlayer);
                            //new Thread(r1).start();
                            new Thread(r2).start();
                            new Thread(r3).start();
                        }catch(Exception ex){
                            System.out.println("File not found");
                        }
                        //Gets READY state and event handler handles further
                    }
                    else{
                        alert.showAndWait();
                    }

                }
                else{
                    //for new playlist after one has been played
                    if(mediaView.getMediaPlayer().getStatus() == Status.STOPPED 
                            && !isStopPressed){ //&& stop is not pressed
                        
                        //get next element of playlist and play it
                        if(playList.peek() != null){

                            try{
                                Media video = new Media(new File(playList.peek().getVideoFile()).toURI().toString());
                                MediaPlayer mediaPlayer = new MediaPlayer(video);
                                mediaView.setMediaPlayer(mediaPlayer);
                                //new Thread(r1).start();
                                new Thread(r2).start();
                                new Thread(r3).start();
                            }catch(Exception ex){
                                System.out.println("File not found");
                            }
                            //Gets READY state and event handler handles further
                            
                        }
                        else{
                            alert.showAndWait();
                        }
                    }
                    else if(isStopPressed){
                        isStopPressed = false;
                        if(playList.peek() != null){//stop is pressed and user is playing play
                            mediaView.getMediaPlayer().play();
                            isPlaying = true;
                            playB.setText(" | | ");
                        }
                        else {alert.showAndWait();  }
                    }
                    else{
                        mediaView.getMediaPlayer().play();
                        isPlaying = true;
                        playB.setText(" | | ");
                    }
                }
            }
        });
        
        prevB = new Button(" |< ");
        prevB.setTooltip(new Tooltip("Previous song/ start over"));
        prevB.setOnAction((ActionEvent e) -> {
            try{
                if(mediaView.getMediaPlayer().getStatus() == Status.STOPPED ){
                    
                    if(currS != null ){
                        
                        curMp3L.setText(currS.toString());
                        mediaView.getMediaPlayer().seek(Duration.ZERO);
                        mediaView.getMediaPlayer().setStopTime(Duration.seconds(currS.getDuration()));
                        mediaView.getMediaPlayer().play();
                        isPlaying = true;
                        playB.setText(" | | ");

                        if(isPlVisible){
                            pl = FXCollections.<Song>observableList((List)playList);
                            plLV.setItems(pl);
                        }
                    }
                    else{
                        alert.showAndWait();
                    }
                }
                else{
                    if(currS != null){
                        curMp3L.setText(currS.toString());
                        mediaView.getMediaPlayer().seek(Duration.ZERO);
                        mediaView.getMediaPlayer().play();
                        isPlaying = true;
                        playB.setText(" | | ");
                        
                    }
                }
            }
            catch(NullPointerException ex){
                //playlist is empty
                alert.showAndWait();
            }
        });
        nextB = new Button(" >| ");
        nextB.setTooltip(new Tooltip("Skip/Next song"));
        nextB.setOnAction((ActionEvent e) -> {
            
            if(playList.peek() != null){
                try{
                    mediaView.getMediaPlayer().stop();
                }catch(NullPointerException exx){ //do nothing if player is not defined
                }
                try{
                    Media video = new Media(new File(playList.peek().getVideoFile()).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(video);
                    mediaView.setMediaPlayer(mediaPlayer);
                    //new Thread(r1).start();
                    new Thread(r2).start();
                    new Thread(r3).start();
                }catch(Exception ex){
                    System.out.println("File not found");
                }
                
            }
            else{
                curMp3L.setText("");
                isPlaying = false;
                
                try{
                    mediaView.getMediaPlayer().stop();
                }catch(NullPointerException exx){  
                    //do nothing if player is not defined
                }
                
                playB.setText(" > ");
                alert.showAndWait();
            }
        });
        
        //stop button
        stopB = new Button(" II ");
        stopB.setOnAction((ActionEvent e) -> {
            try{
                mediaView.getMediaPlayer().stop();
                isPlaying = false;
                playB.setText(" > ");
                isStopPressed = true;
            }
            catch(NullPointerException ex){}
        });
        stopB.setTooltip(new Tooltip("Stop Button"));
        
        mControllerBox.getChildren().addAll(prevB, playB, nextB, stopB);
        mControllerBox.setAlignment(Pos.BASELINE_CENTER);
        gridPane.add(mControllerBox, 0, 2);
        GridPane.setColumnSpan(mControllerBox, 2);
        
        //keep buttons disabled if no mediaPlayer
        if(mediaView.getMediaPlayer() == null){
            playB.setDisable(true);
            prevB.setDisable(true);
            nextB.setDisable(true);
        }
        
        HBox libB = new HBox();
        libB.getChildren().add(libButton);
        libB.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(libB, 0, 3);

        
        libButton.setOnAction((ActionEvent e) -> {
            
            //to avoid malfunctioning of application
            libButton.setDisable(true);
            plButton.setDisable(true);
            
            if(!isLibVisible){
                
                GridPane gp = new GridPane();
                gp.setVgap(5);
                gp.setHgap(5);
                gp.setPadding(new Insets(10));
                
                HBox hb1 = new HBox();
                hb1.getChildren().add(libCloseB);
                hb1.setAlignment(Pos.BASELINE_RIGHT);
                gp.add(hb1, 1, 0);
                
                HBox hb2 = new HBox();
                Text sText = new Text("Search Song: ");
                hb2.getChildren().add(sText);
                hb2.setAlignment(Pos.BASELINE_LEFT);
                sText.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 14));
                gp.add(hb2, 0, 1);
                
                HBox hb3 = new HBox();
                hb3.getChildren().add(addButton);
                hb3.setAlignment(Pos.BASELINE_RIGHT);
                gp.add(hb3, 1, 1);
                
                //searchField
                gp.add(searchField, 0, 2);
                searchField.setPromptText("Search");
                GridPane.setColumnSpan(searchField, 2);
                
                //listView of library
                libLV.setOrientation(Orientation.VERTICAL);
                libLV.setPrefSize(210, 300);
                gp.add(libLV, 0, 3);
                GridPane.setColumnSpan(libLV, 2);
                
                
                //Adding borderPane to carry gridPane and playlist
                BorderPane borderPane = new BorderPane();
                borderPane.setPadding(new Insets(10));
                borderPane.setCenter(gridPane);
                borderPane.setLeft(gp);
                borderPane.setStyle("-fx-background-color: lightblue");
                Scene s1 = new Scene(borderPane, 630, 370);
                primaryStage.setScene(s1);
                
                isLibVisible = true;
            }
        });
        
        libCloseB.setOnAction((ActionEvent e) -> {
            //enabling disabled buttons again
            plButton.setDisable(false);
            libButton.setDisable(false);
            
            //Adding borderpane to carry gridPane as same Pane cannot be root to more than 1 scene
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(gridPane);
            Scene s1 = new Scene(borderPane, 400, 370);
            primaryStage.setScene(s1);
            
            isLibVisible=false;
        });
        
        FilteredList<Song> filteredLib = new FilteredList<>(lib, p -> true); //p is predicator
        searchField.setOnKeyReleased(event -> {
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredLib.setPredicate((Predicate<? super Song>) song -> {
                    if(newValue == null || newValue.isEmpty())
                        return true; //return whole list as it was
                    else return song.getTitle().toLowerCase().startsWith(newValue.toLowerCase());
                        //only return filtered Songs
                });
            });
            
            libLV.setItems(filteredLib);
        });
        
        addButton.setOnAction((ActionEvent e) -> {
            Song selectedItem = libLV.getSelectionModel().getSelectedItem();
            playList.offer(selectedItem); //adds to the end of queue
            
            playB.setDisable(false);
            prevB.setDisable(false);
            nextB.setDisable(false);
            pl = FXCollections.<Song>observableList((List)playList);
            plLV = new ListView<>(pl);
        });
        
        try{
            plDeleteB.setGraphic(new ImageView( new Image(  new File("download.jpg").toURI().toString(), 20, 20, true, true)));
        }catch(Exception e){
            plDeleteB.setText("Delete");
        }
        plDeleteB.setTooltip(new Tooltip("Delete selected song"));
        plDeleteB.setOnAction((ActionEvent e) -> {
            
            ObservableList selectedIndices = plLV.getSelectionModel().getSelectedItems();
            for(Object o : selectedIndices){
                //System.out.println("o = " + o + " (" + o.getClass() + ")");
                playList.remove(o);
            }
            //System.out.println(playList);
            pl = FXCollections.<Song>observableList((List)playList);
            plLV.setItems(pl);
        });
        
        HBox plB = new HBox();
        plB.getChildren().add(plButton);
        plB.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.add(plB, 1, 3);
        
        plButton.setOnAction((ActionEvent e) -> {
            if(playList.isEmpty()){
                //show alert for empty PL
                alert.showAndWait();
            }
            else{
                //to avoid malfunctioning of application
                libButton.setDisable(true);
                plButton.setDisable(true);
                
                if(!isPlVisible){
                    GridPane gp = new GridPane();
                    gp.setHgap(5);
                    gp.setVgap(5);
                    gp.setPadding(new Insets(10));
                    
                    HBox hb1 = new HBox();
                    hb1.getChildren().addAll(plDeleteB, plCloseB);
                    hb1.setPadding(new Insets(0,20,0,20));
                    hb1.setAlignment(Pos.BASELINE_RIGHT);
                    gp.add(hb1, 1, 0);
                    
                    HBox hb2 = new HBox();
                    hb2.setAlignment(Pos.BASELINE_LEFT);
                    Text nsText = new Text("Next Song: ");
                    hb2.getChildren().add(nsText);
                    nsText.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 14));
                    gp.add(hb2, 0, 1);
                    
                    HBox hb3 = new HBox();
                    hb3.getChildren().add(clrPLB);
                    hb3.setAlignment(Pos.BASELINE_RIGHT);
                    gp.add(hb3, 1, 1);
                    
                    
                    plLV.setOrientation(Orientation.VERTICAL);
                    plLV.setPrefSize(210, 280);
                    gp.add(plLV,0, 2);
                    GridPane.setColumnSpan(plLV, 2);
                    
                    //Adding borderPane to carry gridPane and playlist
                    BorderPane borderPane = new BorderPane();
                    borderPane.setPadding(new Insets(10));
                    borderPane.setCenter(gridPane);
                    borderPane.setRight(gp);
                    borderPane.setStyle("-fx-background-color: lightblue");
                    Scene s1 = new Scene(borderPane, 640, 370);
                    primaryStage.setScene(s1);
                    
                    isPlVisible = true;
                }
            }
        });
        
        plCloseB.setOnAction((ActionEvent e) -> {
            //enabling disabled buttons again
            plButton.setDisable(false);
            libButton.setDisable(false);
            
            //Adding borderPane to carry gridPane as it cannot be root again
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(gridPane);
            Scene s1 = new Scene(borderPane, 400, 370);
            primaryStage.setScene(s1);
            
            isPlVisible=false;
        });
        
        clrPLB.setOnAction((ActionEvent e) -> {
            //alert for empty playlist
            Alert a = new Alert(AlertType.CONFIRMATION);
            a.setTitle("Comfirm");
            a.setHeaderText("Clearing your playlist!!!");
            a.setContentText("Are you sure you want to remove all the songs from playlist?");
            
            ButtonType btYes = new ButtonType(" Yes ", ButtonData.YES);
            ButtonType btCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            
            a.getButtonTypes().setAll(btYes, btCancel);
            
            Optional<ButtonType> result = a.showAndWait();
            if(!result.isPresent()){}
            else if(result.get() == btYes){
                playList.clear();
                pl.clear();
                plLV.setItems(null);
                plCloseB.fire();
            }
            else{}
        });
                
        gridPane.setStyle("-fx-background-color: lightblue");
        Scene scene = new Scene(gridPane, 400, 370); //(width*height)
        primaryStage.setTitle("Karaoke Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public void loadLibrary() throws IOException{
        
        library = new TreeMap<>(); //Creating TreeMap data structure for Red Black Tree implemetation
        try{
            File file = new File(getParameters().getNamed().get("sampleFile"));
            BufferedReader br = new BufferedReader(new FileReader(file)); 
        
            Song s; //for storing songs taken out from file temporarily
            String st;

            while ((st = br.readLine()) != null) {
                String[] words = st.split("	", 4);
                s = new Song();
                s.setTitle(words[0]);
                s.setArtist(words[1]);
                s.setDuration(Integer.valueOf(words[2]));
                s.setVideoFile(words[3]);

                library.put(s.getTitle(), s);
            }
        }catch(Exception ex){
            System.out.println("File not found");
        }
        
    }   
}
