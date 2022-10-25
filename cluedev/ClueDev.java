/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author bobja
 */
public class ClueDev extends Application {

    
    Stage primaryStage;
    Scene scene;
    Scene gameScene;
    Rectangle[][] rectArray;
    Board board;
    int tileSize = 35;
    GameHandler gameHandler;
    Card roomSelection;
    Card weaponSelection;
    Card suspectSelection;
    Rectangle roomRect;
    Rectangle weaponRect;
    Rectangle suspectRect;

    Player[] selectedPlayers;
    Weapon[] selectedWeapons;
    int humanPlayers;
    
    Card[] cardSelections = new Card[3];
    Rectangle[] selectionRects = new Rectangle[3];
    
    GridPane grid;
    GridPane menu;
    HBox root;
    VBox sidePanel;
    VBox sidePanelTop;
    VBox sidePanelMid;
    HBox sidePanelBot;
    
    GridPane dc = new GridPane();

    @Override
    public void start(Stage primaryStage) {
        menuScreen(primaryStage);
    }

    public void mainGame(Stage primaryStage) {
        
        
        this.primaryStage = primaryStage;
        
        for (int i=0; i<this.selectedPlayers.length;i++) {
            if (i>=this.humanPlayers) {
                //this.selectedPlayers[i].setAi(true);
                this.selectedPlayers[i] = new ComputerPlayer(this.selectedPlayers[i].getName());
            }
        }
        
        this.gameHandler = new GameHandler(this.selectedPlayers, this.selectedWeapons);
        //Create gamehandler 
        this.board = this.gameHandler.getBoard();

        int rows = this.board.getHeight();
        int cols = this.board.getWidth();

        this.rectArray = new Rectangle[rows][cols];

        this.root = new HBox();
        this.root.setAlignment(Pos.CENTER);
        
        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);  
        
        this.sidePanelTop = new VBox();
        StackPane dicePane = new StackPane();
        Rectangle dr = new Rectangle(300, 100);
        dicePane.getChildren().addAll(dr);
        this.sidePanelTop.getChildren().addAll(dicePane);
        
        this.sidePanelMid = new VBox();
        StackPane sPane = new StackPane();
        StackPane emPane = new StackPane();
        StackPane etPane = new StackPane();
        StackPane aPane = new StackPane();
        Rectangle sr = new Rectangle(300, 100);
        sPane.getChildren().addAll(sr);        
        Rectangle emr = new Rectangle(300,100);
        emPane.getChildren().addAll(emr);  
        Rectangle etr = new Rectangle(300,100);
        etPane.getChildren().addAll(etr);
        Rectangle ar = new Rectangle(300,100);
        aPane.getChildren().addAll(ar);
        this.sidePanelMid.getChildren().addAll(sPane, emPane, etPane, aPane);
        
        this.sidePanelBot = new HBox();
        
        this.sidePanel = new VBox();
        this.sidePanel.setAlignment(Pos.CENTER);
        this.sidePanel.getChildren().addAll(this.sidePanelTop, this.sidePanelMid, this.sidePanelBot);
          
          
        root.getChildren().addAll(this.grid, this.sidePanel);
        
        
        Image image = new Image(getClass().getResourceAsStream("/images/board finished.png"));
        BackgroundSize bSize = new BackgroundSize(26*tileSize, 27*tileSize, false, false, false, false);
        BackgroundImage background_fill = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize);
        Background background = new Background(background_fill);
        grid.setBackground(background);
        
        
        this.gameScene = new Scene(this.root, (cols * this.tileSize + 200), (rows * this.tileSize) + 50);
        
        drawBoard();

        gameHandler.startGame();

        this.gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        gameHandler.currentPlayer.playerMove(gameHandler.currentPlayer.getDiceRollNum(), "n");
                        resetBoard();
                        drawPath("n");
                        drawPlayers();
                        break;
                    case DOWN:
                        gameHandler.currentPlayer.playerMove(gameHandler.currentPlayer.getDiceRollNum(), "s");
                        resetBoard();
                        drawPath("s");
                        drawPlayers();
                        break;
                    case LEFT:
                        gameHandler.currentPlayer.playerMove(gameHandler.currentPlayer.getDiceRollNum(), "w");
                        resetBoard();
                        drawPath("w");
                        drawPlayers();
                        break;
                    case RIGHT:
                        gameHandler.currentPlayer.playerMove(gameHandler.currentPlayer.getDiceRollNum(), "e");
                        resetBoard();
                        drawPath("e");
                        drawPlayers();
                        break;
                }
            }
        });

        drawBoard();
        detectiveCardWindow();;
        diceRollWindow(true);
        suggestionWindow(true);
        endMoveWindow(true);
        endTurnWindow(true);
        accusationWindow(true);

        this.primaryStage.setScene(gameScene);
        this.primaryStage.show();

        drawPlayers();

    }

    /**
     * Checks if the current human player can make a suggestion and if so,
     * opens a card selection window for them
     */
    public void checkRoom() {
        if (this.gameHandler.currentPlayer.canSuggest()) {
            cardSelectionWindow(this.gameHandler.getDeck().getCardList(), 3, "s");
        }
    }
    
    /**
     * Check if current ai player can make a suggestion and if so, generate
     * 3 random unseen cards for a suggestion and make it
     */
    public void aiCheckRoom() {
        if (this.gameHandler.currentPlayer.canSuggest()) {
            ComputerPlayer currentPlayer = (ComputerPlayer) gameHandler.currentPlayer;
            this.cardSelections =  currentPlayer.getComputerCards();
            makeSuggestion();
        }    
    }
    
    /**
     * UI Controller for the dice roll panel. 
     *
     * @param boolean a Boolean for whether or not the button should be active or not
     */
    public void diceRollWindow(boolean a) {
        StackPane pane = (StackPane) this.sidePanelTop.getChildren().get(0);
        Rectangle diceRect = (Rectangle) pane.getChildren().get(0);
        
        Image imageInactive = new Image(getClass().getResourceAsStream("/images/ui/roll dice grey.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/ui/roll dice.png"));
        Image imageActiveDragover = new Image(getClass().getResourceAsStream("/images/ui/roll dice mouseover.png"));
        Image imageBlank = new Image(getClass().getResourceAsStream("/images/ui/roll dice blank.png"));
        
        diceRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
            
        boolean active = a;
        
        if (!active) {
            diceRect.setFill(new ImagePattern(imageInactive, 0, 0, 1, 1, true));
            pane.getChildren().clear();
            pane.getChildren().add(diceRect);
        } else {
            pane.getChildren().clear();
            pane.getChildren().add(diceRect);
        }
        
        
        diceRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {    
                if (active) {
                    if (gameHandler.currentPlayer.getDiceRollNum() == 0) {
                        int roll = gameHandler.currentPlayer.diceRoll(2);
                        gameHandler.currentPlayer.setDiceRollNum(roll);
                        diceRect.setFill(new ImagePattern(imageBlank, 0, 0, 1, 1, true));
                        Text diceText = new Text(""+roll);
                        diceText.setFont(Font.font ("Verdana", 40));
                        pane.getChildren().add(diceText);              
                    }  
                }                 
            }
        });
        diceRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    diceRect.setFill(new ImagePattern(imageActiveDragover, 0, 0, 1, 1, true));
                }         
            }
        });
        diceRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    diceRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
                }           
            }
        });
        
        //this.sidePanelTop.getChildren().addAll(diceRect);
    }
    
    /**
     * UI Controller for the suggestion button panel. 
     *
     * @param boolean a Boolean for whether or not the button should be active or not
     */
    public void suggestionWindow(boolean a) {
        StackPane pane = (StackPane) this.sidePanelMid.getChildren().get(0);
        Rectangle sugRect = (Rectangle) pane.getChildren().get(0);
        
        Image imageInactive = new Image(getClass().getResourceAsStream("/images/ui/Suggestion grey.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/ui/Suggestion.png"));
        Image imageActiveDragover = new Image(getClass().getResourceAsStream("/images/ui/Suggestion mouseover.png"));

        
        sugRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
            
        boolean active = a;  
        
        sugRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {    
                if (active) {
                    if (gameHandler.currentPlayer.getDiceRollNum() == 0) {
                        checkRoom();
                        //sugRect.setFill(new ImagePattern(imageBlank, 0, 0, 1, 1, true));          
                    }  
                }                 
            }
        });
        sugRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    sugRect.setFill(new ImagePattern(imageActiveDragover, 0, 0, 1, 1, true));
                }         
            }
        });
        sugRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    sugRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
                }           
            }
        });
    }
    
    /**
     * UI Controller for the end move button panel. 
     *
     * @param boolean a Boolean for whether or not the button should be active or not
     */
    public void endMoveWindow(boolean a) {
        StackPane pane = (StackPane) this.sidePanelMid.getChildren().get(1);
        Rectangle emRect = (Rectangle) pane.getChildren().get(0);
        
        Image imageInactive = new Image(getClass().getResourceAsStream("/images/ui/end move grey.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/ui/end move.png"));
        Image imageActiveDragover = new Image(getClass().getResourceAsStream("/images/ui/end move mouseover.png"));

        
        emRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
            
        boolean active = a;  
        
        emRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {    
                if (active) {
                    if (gameHandler.currentPlayer.getLocation() instanceof SpecialTile) {
                        SpecialTile s = (SpecialTile) gameHandler.currentPlayer.getLocation();
                        if (s.getSpecialType().equals("r") && !gameHandler.currentPlayer.extraMove){
                            diceRollWindow(true);
                        }            
                    }   
                    gameHandler.currentPlayer.endMove();             
                    resetBoard();
                    drawPlayers();
                    resetBoard();
                    drawPlayers();
                }                 
            }
        });
        emRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    emRect.setFill(new ImagePattern(imageActiveDragover, 0, 0, 1, 1, true));
                }         
            }
        });
        emRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    emRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
                }           
            }
        });
    }
    
    /**
     * UI Controller for the end turn button panel. 
     *
     * @param boolean a Boolean for whether or not the button should be active or not
     */
    public void endTurnWindow(boolean a) {
        StackPane pane = (StackPane) this.sidePanelMid.getChildren().get(2);
        Rectangle etRect = (Rectangle) pane.getChildren().get(0);
        
        Image imageInactive = new Image(getClass().getResourceAsStream("/images/ui/end turn grey.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/ui/end turn.png"));
        Image imageActiveDragover = new Image(getClass().getResourceAsStream("/images/ui/end turn mouseover.png"));
  
        etRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
            
        boolean active = a;  
        
        etRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {    
                if (active) {
                    diceRollWindow(false);
                    diceRollWindow(true);
                    gameHandler.currentPlayer.endMove();    
                    gameHandler.nextTurn();     
                    updateDc();
                    resetBoard();
                    if (!(gameHandler.currentPlayer instanceof ComputerPlayer)) {
                        checkStart();
                    } else {
                        ComputerPlayer pc = (ComputerPlayer) gameHandler.currentPlayer;
                        diceRollWindow(false);
                        if (pc.shouldAccuse()) {
                            System.out.println("AI Player "+ pc.getName() + " has accused!");
                            makeAccusation();
                        }
                        aiCheckStart();
                        pc.startMove();
                        drawPlayers();
                        aiCheckRoom();
                    }
                    drawPlayers();
                }                 
            }
        });
        etRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    etRect.setFill(new ImagePattern(imageActiveDragover, 0, 0, 1, 1, true));
                }         
            }
        });
        etRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    etRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
                }           
            }
        });
    }
    
    /**
     * UI Controller for the accusation button panel. 
     *
     * @param boolean a Boolean for whether or not the button should be active or not
     */
    public void accusationWindow(boolean a) {
        StackPane pane = (StackPane) this.sidePanelMid.getChildren().get(3);
        Rectangle aRect = (Rectangle) pane.getChildren().get(0);
        
        Image imageInactive = new Image(getClass().getResourceAsStream("/images/ui/Accusation grey.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/ui/Accusation.png"));
        Image imageActiveDragover = new Image(getClass().getResourceAsStream("/images/ui/Accusation mouseover.png"));
  
        aRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
            
        boolean active = a;  
        
        aRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {    
                if (active) {
                    makeAccusation();
                    diceRollWindow(false);
                    diceRollWindow(true);
                    gameHandler.currentPlayer.endMove();    
                    gameHandler.nextTurn();     
                    updateDc();
                    resetBoard();
                    if (!(gameHandler.currentPlayer instanceof ComputerPlayer)) {
                        checkStart();
                    } else {
                        ComputerPlayer pc = (ComputerPlayer) gameHandler.currentPlayer;
                        diceRollWindow(false);
                        if (pc.shouldAccuse()) {
                            System.out.println("AI Player "+ pc.getName() + " has accused!");
                            makeAccusation();
                        }
                        aiCheckStart();
                        pc.startMove();
                        drawPlayers();
                        aiCheckRoom();
                    }
                    drawPlayers();
                }                 
            }
        });
        aRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    aRect.setFill(new ImagePattern(imageActiveDragover, 0, 0, 1, 1, true));
                }         
            }
        });
        aRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (active && gameHandler.currentPlayer.getDiceRollNum() == 0) {
                    aRect.setFill(new ImagePattern(imageActive, 0, 0, 1, 1, true));
                }           
            }
        });
    }
    
    /**
     * Handles behaviour of other players selecting and revealing cards to  
     * a player that made the suggestion
     */
    public void makeSuggestion(){
        Card[] cards = this.getCardSelections();
        this.gameHandler.currentPlayer.setMadeSuggestion(true);
        for(int i = 0; i < this.gameHandler.getPlayers().length; i++){
            if(this.gameHandler.getPlayers()[i] != this.gameHandler.currentPlayer){
                ArrayList<Card> suggestedList = this.gameHandler.getPlayers()[i].checkSuggestion(cards[0], cards[1], cards[2]);
                if(suggestedList.size() > 0){
                    if (this.gameHandler.getPlayers()[i] instanceof ComputerPlayer) {
                        this.cardSelections[0] = suggestedList.get(0);
                    } else {
                        cardSelectionWindow(suggestedList, 1, "r");
                    }
                    this.gameHandler.currentPlayer.seenCard(getCardSelections()[0]);                 
                }
            }
        }
        updateDc();
    }
    
    /**
     * Handles behaviour for making accusations
     */
    public void makeAccusation() {
        System.out.println("ACCUSATION MADE");
        if (!(this.gameHandler.currentPlayer instanceof ComputerPlayer)) {
            cardSelectionWindow(this.gameHandler.getDeck().getCardList(), 3, "a");
        } else {
            ComputerPlayer temp = (ComputerPlayer) this.gameHandler.currentPlayer;
            this.cardSelections = temp.getComputerCards();
        }
        
        Card[] cards = getCardSelections();
        boolean correct = this.gameHandler.accusation(cards[0],cards[1],cards[2]);
        if (correct) {
            this.primaryStage.close();
            showEndScreen();
        } 
    }
    
    /**
     * Brings up game over screen when game is finished
     */
    public void showEndScreen() {
        Stage esStage = new Stage();
        esStage.setTitle("Card Selection");

        GridPane g = new GridPane();
        Scene esScene = new Scene(g, 200, 300);
        
        Label end = new Label("Game over.");
        g.add(end, 0, 0, tileSize, tileSize);

        esStage.setScene(esScene);
        esStage.show();
    }
    
    /**
     * Check if the human player is in a room at the start of their move
     * and allow them to exit the room
     */
    public void checkStart() {
        Tile loc = this.gameHandler.currentPlayer.getLocation();
        if (loc instanceof RoomTile) {
            RoomTile rt = (RoomTile) loc;
            showExits(rt.getRoom());
        }
    }
    
    /**
     * Check if the ai player is in a room at the start of their move and automatically
     * move them out of a door
     */
    public void aiCheckStart() {
        Tile loc = this.gameHandler.currentPlayer.getLocation();
        if (loc instanceof RoomTile) {
            RoomTile rt = (RoomTile) loc;
            Room r = rt.getRoom();
            ArrayList<Tile> exits = r.getExitTileArray();
            for (Tile t1 : exits) {
                for (Tile t2 : t1.getSurrounding().values()) {
                    if (!(t2.getOccupied()) && (t2 instanceof BlankTile)) {
                        this.gameHandler.currentPlayer.getLocation().setOccupied(false);
                        this.gameHandler.currentPlayer.setLocation(t1);
                    }
                }
            }
        }
    }
    
    /**
     * Displays detective cards for the current player to the right of the board.
     * Updates dynamically as cards are revealed.
     */
    public void detectiveCardWindow() {
        this.sidePanelBot.getChildren().clear();
        
        HashMap<Card, Boolean> cards = this.gameHandler.currentPlayer.getDetectiveCard();
        GridPane g = new GridPane();
        
        int[] counts = new int[3];
        for (Card c : cards.keySet()) {         
            Rectangle newRect = new Rectangle();
            newRect.setHeight(150);
            newRect.setWidth(100);
            newRect.setStyle("-fx-stroke: black;");
            Image image;
            if (cards.get(c)) {
                //System.out.println(c.getName());
                image = new Image(getClass().getResourceAsStream(c.getImage()));
            } else {
                image = new Image(getClass().getResourceAsStream("/images/cards/hidden.png"));
            }
            newRect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
            
            int i = 0;
            switch (c.getType()) {
                case "Room":
                    i = 0;
                    counts[0]++;
                    break;
                case "Weapon":
                    i = 1;
                    counts[1]++;
                    break;
                case "Suspect":
                    i = 2; 
                    counts[2]++;
                    break;
            }
            
            g.add(newRect, counts[i], i, 1, 1);
        }
        this.sidePanelBot.getChildren().addAll(g);
    }
    
    /**
     * Update images of cards in detective card window to show revealed cards
     */
    public void updateDc() {
        HashMap<Card, Boolean> cards = this.gameHandler.currentPlayer.getDetectiveCard();
        
        ArrayList<Image> images = new ArrayList<>();
        
        for (Card c : cards.keySet()) {         
            Image image;
            if (cards.get(c)) {
                //System.out.println(c.getName());
                image = new Image(getClass().getResourceAsStream(c.getImage()));
            } else {
                image = new Image(getClass().getResourceAsStream("/images/cards/hidden.png"));
            }
            images.add(image);
        }
        
        ObservableList<Node> list = this.sidePanelBot.getChildren();
        try {
            GridPane newPane = (GridPane) list.get(0);
            ObservableList<Node> list2 = newPane.getChildren();
            for (int n=0; n<list2.size(); n++) {
                Rectangle r = (Rectangle) list2.get(n);
                r.setFill(new ImagePattern(images.get(n), 0, 0, 1, 1, true));     
            }    
        } catch (Exception e) { 
        }
    }

    /**
     * Create an array of arraylists of rectangles to display 3 arraylists, one for each
     * type of card.
     *
     * @param ArrayList<Card>[] cards An array of ArrayLists holding cards
     * @return ArrayList<Rectangle>[] Array of arraylists of rectangles displaying cards 
     */
    public ArrayList<Rectangle>[] populateCardRectArrays(ArrayList<Card>[] cards) {
        ArrayList<Rectangle>[] rects = new ArrayList[3];
        rects[0] = new ArrayList<Rectangle>();
        rects[1] = new ArrayList<Rectangle>();
        rects[2] = new ArrayList<Rectangle>();
        for (int n = 0; n < cards.length; n++) {
            for (Card c : cards[n]) {
                Rectangle newRect = new Rectangle();
                newRect.setHeight(150);
                newRect.setWidth(100);
                newRect.setStyle("-fx-stroke: black;");
                //System.out.println("Drawing: "+c.getImage());
                Image image = new Image(getClass().getResourceAsStream(c.getImage()));
                newRect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));

                rects[n].add(newRect);
            }
        }
        return rects;
    }

    /**
     * Contains the listener method attatched to each card rectangle int he card selection
     * window.
     *
     * @param MouseEvent t Mouse event that took place on the rectangle
     * @param Card c Card to be drawn
     * @param String purpose Single character to determine the purpose of the selection window:
     *              "s" for suggestion, "a" for accusation and "r" for response to a suggestion
     */
    public void cardSelectListener(MouseEvent t, Card c, String purpose) {
        int i = 0;
        if (purpose.equals("a") || purpose.equals("s")) {
            switch (c.getType()) {
                case "Room":
                    i = 0;
                    break;
                case "Weapon":
                    i = 1;
                    break;
                case "Suspect":
                    i = 2;
                    break;
            }
        }
        MouseButton pressed = t.getButton();
        if (pressed == MouseButton.PRIMARY) {
            System.out.println(c.getName());
            if (cardSelections[i] != null) {
                Image defaultImage = new Image(getClass().getResourceAsStream(cardSelections[i].getImage()));
                selectionRects[i].setFill(new ImagePattern(defaultImage, 0, 0, 1, 1, true));
            }

            Rectangle rect = (Rectangle) t.getSource();
            cardSelections[i] = c;
            selectionRects[i] = rect;
            Image image = new Image(getClass().getResourceAsStream("/images/cards/hidden.png"));
            rect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
        }
    }
    
    /**
     * Handler for the card selection window
     * 
     * @param ArrayList<Card> cards Array of cards to select between
     * @param int numToSelect number of cards that need to be selected
     * @param String purpose String to show purpose of card selection window:
     *          "s" for suggestion, "a" for accusation and "r" for response to a suggestion
     */
    public void cardSelectionWindow(ArrayList<Card> cards, int numToSelect, String purpose) {
        Stage csStage = new Stage();
        String sceneTitle = "Card Selection";
        switch (purpose) {
            case "s":
                sceneTitle = this.gameHandler.currentPlayer.getName()+", make your suggestion.";
                break;
            case "a":
                sceneTitle = this.gameHandler.currentPlayer.getName()+", make your accusation.";
                break;
            case "r":
                sceneTitle = "Choose a card to reveal to the suggestion maker.";
                break;
        }
        csStage.setTitle(sceneTitle);
        //this.sidePanelBot.getChildren().clear();
        
        GridPane g = new GridPane();
        g.setVgap(1);
        g.setHgap(5);
        Scene csScene = new Scene(g, 1000, 500);
        
        cardSelections = new Card[3];
        selectionRects = new Rectangle[3];

        ArrayList<Card>[] sorted = this.gameHandler.getDeck().sortSplitCards(cards);
        ArrayList<Rectangle>[] allCardRects = populateCardRectArrays(sorted);

        for (int n = 0; n < sorted.length; n++) {
            for (int r = 0; r < sorted[n].size(); r++) {
                Card c = sorted[n].get(r);
                allCardRects[n].get(r).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        cardSelectListener(t, c, purpose);
                    }
                });
                g.add(allCardRects[n].get(r), r, n, 1, 1);
            }
        }
        

        Button playButton = new Button("Make Selection");
        Label notEnoughSelectError = new Label();
        playButton.setAlignment(Pos.CENTER);
        playButton.setOnAction(event -> {
            int nullSelections = 0;
            for (Card card : cardSelections) {
                if (card==null) {
                    nullSelections++;
                }
            }
            if (nullSelections == (3-numToSelect)) {
                if (purpose.equals("s")) {
                    makeSuggestion();
                } 
                csStage.close();
            } else {
                notEnoughSelectError.setText("Not enough cards selected");
            }
        });
        g.add(playButton, 4, 7, 1, 1);
        g.add(notEnoughSelectError, 4, 8, 1, 1);     
        
        csStage.setScene(csScene);
        csStage.showAndWait();
    }

    /**
     * @return Card[] returns the card array stored in this.cardSelection
     */
    public Card[] getCardSelections() {
        return this.cardSelections;
    }

    /**
     * Iterates through the board array and fills the board gridpane with transparent rectangles
     * These rectangles will later have their images changed to represent sprites
     */
    public void drawBoard() {
        
        Tile[][] boardTiles = this.gameHandler.getBoard().getTileArray();

        for (int r = 0; r < this.gameHandler.getBoard().getHeight(); r++) {
            for (int c = 0; c < this.gameHandler.getBoard().getWidth(); c++) {
                //Tile tempTile = boardTiles[r][c];

                Rectangle newRect = new Rectangle();
                newRect.setHeight(this.tileSize);
                newRect.setWidth(this.tileSize);
                //newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 1.0");
                newRect.setFill(Color.TRANSPARENT);
                //Image image = new Image(getClass().getResourceAsStream(tempTile.getImage()));
                //newRect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));

                this.rectArray[r][c] = newRect;
                this.grid.add(newRect, c, r + 1, 1, 1);

            }
        }
    }
    
    /**
     * removes images from all rectangles in the board array
     */
    public void resetBoard() {
        for (int r = 0; r < this.gameHandler.getBoard().getHeight(); r++) {
            for (int c = 0; c < this.gameHandler.getBoard().getWidth(); c++) {
                this.rectArray[r][c].setFill(null);
            }
        }
    }

    /**
     * Draws all sprites of players and weapons at their current locations
     */
    public void drawPlayers() {
        int r;
        int c;
        for (Player p : this.gameHandler.getPlayers()) {
            r = p.getLocation().getRow();
            c = p.getLocation().getCol();
            Image image = new Image(getClass().getResourceAsStream(p.getImage()));
            this.rectArray[r][c].setFill(new ImagePattern(image, 0, 0, 1, 1, true));
        }
        for (Weapon w : this.gameHandler.getWeapons()) {
            r = w.getLocation().getRow();
            c = w.getLocation().getCol();
            Image image = new Image(getClass().getResourceAsStream(w.getImage()));
            this.rectArray[r][c].setFill(new ImagePattern(image, 0, 0, 1, 1, true));
        }
    }
    
    /**
     * Draws the path of the player from their start tile to their current tile on their
     * current move. Allows user to easily retrace their steps.
     *
     * @param String direction The direction that was last pressed by the user
     */
    public void drawPath(String direction) {
        ArrayList<Tile> path = new ArrayList<Tile>(); 
        for (Tile t : this.gameHandler.currentPlayer.getMoveQueue()) {
            path.add(t);
        }
        Image image;
        String[] directions = new String[]{"n", "s", "e", "w"};
        for (int n=0; n<path.size(); n++) {
            if (n>0) {
                String goingTo = "";
                for (String d1 : directions) {
                    Tile src1 = path.get(n);
                    Tile end1;
                    if ((n+1) == path.size()) {
                        end1 = this.gameHandler.currentPlayer.getLocation();
                    } else {
                        end1 = path.get(n+1);
                    }
                    if (src1.getTileAtDirection(d1) == end1) {
                        goingTo = d1;
                    }
                }
                if (n==1) {
                    image = new Image(getClass().getResourceAsStream("/images/paths/m_"+goingTo+".png"));
                    this.rectArray[path.get(n).getRow()][path.get(n).getCol()].setFill(new ImagePattern(image, 0, 0, 1, 1, true));
                } else if (n>1) {
                    String comeFrom = "";
                    for (String d2 : directions) {
                        Tile src2 = path.get(n-1);
                        Tile end2 = path.get(n);
                        if (end2.getTileAtDirection(d2) == src2) {
                            comeFrom = d2;
                        }
                    }
                    image = new Image(getClass().getResourceAsStream("/images/paths/"+comeFrom+"_"+goingTo+".png"));
                    this.rectArray[path.get(n).getRow()][path.get(n).getCol()].setFill(new ImagePattern(image, 0, 0, 1, 1, true));
                }
            }
        }
        
        
    }

    /**
     * Highlights all exits from a room for a user that is currently inside it. They can then
     * select a door from which they wish to exit.
     *
     * @param Room room The room for which exits should be highlighted and selectable
     */
    public void showExits(Room room) {
        int r;
        int c;
        for (Tile d : room.getExitTileArray()) {
            r = d.getRow();
            c = d.getCol();
            Rectangle temp = this.rectArray[r][c];
            Image image = new Image(getClass().getResourceAsStream("/images/valid.png"));
            Image image1 = new Image(getClass().getResourceAsStream("/images/background.png"));
            temp.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
            temp.setOnMouseClicked((MouseEvent event) -> {          
                Rectangle t = (Rectangle) event.getSource();
                for (Tile tile : room.getExitTileArray()) {
                    this.rectArray[tile.getRow()][tile.getCol()].setFill(new ImagePattern(image, 0, 0, 1, 1, true));
                }
                //System.out.println("coords" + d.getCol() + d.getRow());
                this.gameHandler.currentPlayer.setLocation(d);
                drawPlayers();
                t.setFill(new ImagePattern(image1, 0, 0, 1, 1, true));
            });
            this.rectArray[r][c] = temp;

        }

    }

    /**
     * Draws the menu screen
     *
     * @param Stage stage Stage for the menu screen to be drawn on
     */
    public void menuScreen(Stage stage) {
        this.menu = new GridPane();
        this.scene = new Scene(menu, 400, 400);
        menu.setAlignment(Pos.TOP_CENTER);
        //menu.setVgap(10);
        
        Label totalPlayerLabel = new Label("Total no. of players (3-6):");
        TextField totalPlayerField = new TextField("6");
        menu.add(totalPlayerLabel, 15, 16, 1, 1);  
        menu.add(totalPlayerField, 16, 16, 1, 1);
        
        Label humanNumLabel = new Label("No. of human players (1-6):");       
        TextField humanNumField = new TextField("6");
        menu.add(humanNumLabel, 15, 17, 1, 1);  
        menu.add(humanNumField, 16, 17, 1, 1);
        
        Button playButton = new Button("Start Game");
        playButton.setAlignment(Pos.CENTER);
        playButton.setOnAction(event -> {
            int totalNum = 0;
            int humanNum = 0;
            boolean playerFieldOk = false;
            boolean humanFieldOk = false;
            try {
                totalNum = Integer.parseInt(totalPlayerField.getText());
                humanNum = Integer.parseInt(humanNumField.getText());
                playerFieldOk = (totalNum>=3 && totalNum<=6);
                humanFieldOk = (humanNum>=1 && humanNum<=totalNum);
            } catch (Exception e) {
            }
            if (playerFieldOk && humanFieldOk) {
                this.menu.getChildren().clear();
                this.selectedPlayers = new Player[totalNum];
                for (int n=0;n<totalNum;n++) {
                    this.selectedPlayers[n] = null;
                }
                this.selectedWeapons = new Weapon[6];
                for (int n=0;n<totalNum;n++) {
                    this.selectedWeapons[n] = null;
                }
                this.humanPlayers = humanNum;
                selectChar(stage, totalNum);
            }       
        });
        menu.add(playButton, 16, 18, 1, 1);

        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * A window in which the user can select their chosen number of characters
     * that they wish to have in their game.
     *
     * @param Stage stage Stage for the character selection screen to be drawn onto
     * @param int playerNum number of players in the game
     */
    public void selectChar(Stage stage, int playerNum) {
        FullPlayerList allPlayers = new FullPlayerList(playerNum);
        ArrayList<Player> playerList = allPlayers.getAllPlayers();
        ArrayList<Player> selected = new ArrayList<Player>();
        for (int n=0; n<playerList.size(); n++) {
            Player current = playerList.get(n);
            Rectangle rect = new Rectangle(50,50);
            Image image = new Image(getClass().getResourceAsStream(playerList.get(n).getImage()));
            //Image hidden = new Image(getClass().getResourceAsStream("/images/valid.png"));
            rect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    //rect.setFill(new ImagePattern(hidden, 0, 0, 1, 1, true));
                    menu.getChildren().remove(t.getSource());
                    int i=0;
                    boolean swapped = false;
                    while (!swapped) {
                        if (selectedPlayers[i] == null) {
                            selectedPlayers[i] = current;
                            swapped = true;
                        } 
                        i++;
                    }
                    if (i > playerNum-1) {
                        
                        selectWep(stage);
                    }
                }
             });
            menu.add(rect, n%4, n/4, 1, 1);
        }
    }
    
    /**
     * Screen on which the user selects the 6 weapons that they wish to have in 
     * their game
     *
     * @param Stage stage Stage for the character selection screen to be drawn onto
     */
    public void selectWep(Stage stage) {
        this.menu.getChildren().clear();
        FullWeaponList allWeapons = new FullWeaponList(6);
        ArrayList<Weapon> weaponList = allWeapons.getAllWeapons();
        ArrayList<Weapon> selected = new ArrayList<Weapon>();
        for (int n=0; n<weaponList.size(); n++) {
            Weapon current = weaponList.get(n);
            Rectangle rect = new Rectangle(50,50);
            Image image = new Image(getClass().getResourceAsStream(weaponList.get(n).getImage()));
            //Image hidden = new Image(getClass().getResourceAsStream("/images/valid.png"));
            rect.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    //rect.setFill(new ImagePattern(hidden, 0, 0, 1, 1, true));
                    menu.getChildren().remove(t.getSource());
                    int i=0;
                    boolean swapped = false;
                    while (!swapped) {
                        if (selectedWeapons[i] == null) {
                            selectedWeapons[i] = current;
                            swapped = true;
                        } 
                        i++;
                    }
                    if (i > 6-1) {
                        mainGame(stage);
                    }
                }
             });
            menu.add(rect, n%4, n/4, 1, 1);
        }
    }

    /**
     * Main method for clueDev, launches the application
     *
     * @param String[] args No arguments required.
     */
    public static void main(String[] args) {
        System.out.println("Start Program");
        Board board1 = new Board();
        launch(args);
    }

}
