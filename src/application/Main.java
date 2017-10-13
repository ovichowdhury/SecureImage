package application;
	
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	private Stage primaryStage;
	private static Stage promptStage;
	private Label welcomeLabel;
	
	private ImageView imageContainer;
	//private Image img;
	
	private FileIO fileIO;
	
	private MenuBar menuBar;
	private Menu file;
	private Menu edit;
	private Menu key;
	private Menu help;
	private MenuItem New;
	private MenuItem save;
	private MenuItem saveAs;
	private MenuItem exit;
	private MenuItem encrypt;
	private MenuItem decrypt;
	private MenuItem showKey;
	private MenuItem setKey;
	private MenuItem setDefKey;
	private MenuItem aboutSoft;
	
	private BorderPane root;
	
	@Override
	public void init(){
		imageContainer = new ImageView();
		welcomeLabel = new Label("Please Enter an Image");
		fileIO = new FileIO();
		
		menuBar = new MenuBar();
		file = new Menu("File");
		edit = new Menu("Edit");
		key = new Menu("Key");
		help = new Menu("Help");
		New = new MenuItem("New");
		save = new MenuItem("Save");
		saveAs = new MenuItem("Save as");
		exit = new MenuItem("Exit");
		encrypt = new MenuItem("Encrypt");
		decrypt = new MenuItem("Decrypt");
		showKey = new MenuItem("Show Key");
		setKey = new MenuItem("Set Key");
		setDefKey = new MenuItem("Set Default Key");
		aboutSoft = new MenuItem("About");
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			// layout
			root = new BorderPane();
			
			// scene
			Scene scene = new Scene(root,700,500);
			
			//adding Menu
			this.addMenu();
			// adding label
			root.setBottom(welcomeLabel);
			// adding image container
			root.setCenter(imageContainer);
			this.setStyleToPane();
			
			// adding listener
			this.addMenuListeners();
			this.addWindowListener();
			
			//creating key for encryption
			FileIO.key = FileIO.createKey();
			
			// stage
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:src/images/icon.jpg"));
			primaryStage.setResizable(true);
			primaryStage.setTitle("Secure Image");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	private void addWindowListener() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "Do You Want To Exit?");
				if(i == 0){
					primaryStage.close();
				}
				e.consume();
			}
		});
		
	}

	@Override
	public void stop(){
		try{
			FileIO.tempFile.deleteOnExit();
		}catch(Exception ex){
			
		}
		
	}
	
	private void addMenu(){
		
		file.getItems().addAll(New,save,saveAs,exit);
		edit.getItems().addAll(encrypt,decrypt);
		help.getItems().addAll(aboutSoft);
		key.getItems().addAll(showKey,setKey,setDefKey);
		
		menuBar.getMenus().add(file);
		menuBar.getMenus().add(edit);
		menuBar.getMenus().add(key);
		menuBar.getMenus().add(help);
		root.setTop(menuBar);
		
	}
	
	private void setStyleToPane(){
		welcomeLabel.setStyle("-fx-font-size: 150%");
		welcomeLabel.setTextFill(Color.WHITE);
		
		root.setStyle("-fx-background-color: gray");
		BorderPane.setAlignment(imageContainer, Pos.CENTER);
		imageContainer.setFitHeight(400); //imageContainer.setFitHeight(300);
		imageContainer.setFitWidth(500);  //imageContainer.setFitWidth(400);
		imageContainer.setImage(new Image("file:src/images/welcome.jpg"));
		
		//menu style
		
		file.setStyle("-fx-font-weight: bold");
		edit.setStyle("-fx-font-weight: bold");
		help.setStyle("-fx-font-weight: bold");
		key.setStyle("-fx-font-weight: bold");
	}
	
	private void addMenuListeners(){
		// NEW
		New.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				System.out.println("Code is in new listener");
				fileIO.openNewFile(primaryStage, imageContainer, welcomeLabel,encrypt,decrypt);
			}
		});
		// Encrypt
		encrypt.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				try{
					fileIO.encryptImage(encrypt);
					JOptionPane.showMessageDialog(null, "Image Encrypted");
				}catch(Exception ex){
					
				}

			}
			
		});
		// Decrypt
		decrypt.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				fileIO.decryptAndSaveImage();
				
			}
			
		});
		// save
		save.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				fileIO.saveImage();
			}	
		});
		// save as
		saveAs.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				fileIO.saveAsImage(primaryStage);
				
			}
			
		});
		// exit
		exit.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
				
			}
			
		});
		// about
		
		aboutSoft.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				String message = "Secure image is a (.JPG) photo encryption software";
				JOptionPane.showMessageDialog(null,message );
			}
			
		});
		
		//show key
		
		showKey.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				String message = "Key : " +Integer.toString(FileIO.key*9999);
				JOptionPane.showMessageDialog(null, message);
			}
			
		});
		
		setKey.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				Main.promptUserInput();
			}
			
		});
		
		setDefKey.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				FileIO.key = FileIO.createKey();
				JOptionPane.showMessageDialog(null, "Key is changed to default");
			}
			
		});
	}
	
	private static void promptUserInput(){
		Pane root = new Pane();
		TextField field = new TextField();
		Label lbl = new Label("Key : ");
		Button btn = new Button("OK");
		
		lbl.setLayoutX(10);
		lbl.setLayoutY(27);
		
		field.setLayoutX(40);
		field.setLayoutY(25);
		
		btn.setLayoutX(90);
		btn.setLayoutY(60);
		
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try{
					FileIO.key = Integer.valueOf(field.getText())/9999;
					Main.promptStage.close();
				}catch(Exception ex){
					
				}
			}
			
		});
		
		root.getChildren().addAll(lbl,field,btn);
		
		promptStage = new Stage();
		Scene promptScene = new Scene(root,200,100);
		
		promptStage.setScene(promptScene);
		promptStage.setTitle("Set Key");
		promptStage.setResizable(false);
		promptStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
