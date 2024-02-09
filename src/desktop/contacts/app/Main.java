package desktop.contacts.app;
	
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	private final static String STORAGEFILE = "contacts.bin";
	private TreeSet<Contact> contacts = new TreeSet<>();
	private AvailableContactsView availableView;
	Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			load();
			
			availableView = new AvailableContactsView(contacts);
			
			/**Rectangle rect = new Rectangle();
			rect.setArcHeight(20.0);
			rect.setArcWidth(20.0);
			availableView.setClip(rect);*/
			
			scene = new Scene(availableView, 780, 800);
			//rect.setHeight(scene.getHeight());
			//rect.setWidth(scene.getWidth());
			
			primaryStage.setTitle("Contacts");
			//scene.setFill(Color.TRANSPARENT);
			//primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(scene.getHeight());
			primaryStage.setMinWidth(scene.getWidth());
			primaryStage.show();
			
			//save to file before closing...
			primaryStage.setOnCloseRequest(e -> save());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//for loading up stand alone application 
	public static void main(String[] args) {
		launch(args);
	}
	
//------------------------------Utility functions------------------------------------
	/**
	 * save the current snapshot
	 * snapshot includes all the contact enteries at that 
	 * moment.
	 */
	public void save() {
		try {
			try( 
				ObjectOutputStream file = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(STORAGEFILE), 512 * 3));
			){
				file.writeObject(contacts);
			}
			
		}catch(Exception e) {
			System.out.println("An error occured: " + e.getMessage());
		}
	}
	
	/**
	 * Tries to read all available contacts that had been previously saved to
	 * the STORAGEFILE("contacts.bin"). 
	 * @return true if there contacts in it, otherwise false 
	 */
	@SuppressWarnings("unchecked")// casting of object to treeSet;
	public boolean load() {
		try {
			try( 
				ObjectInputStream file = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(STORAGEFILE), 512 * 3));
				){
				contacts = (TreeSet<Contact>) file.readObject();
				return true;
			}
		}catch(Exception e) {
			//show error only when its not from a file not found exception
			if(!(e instanceof FileNotFoundException))
				System.out.println("An error occured: " + e.getMessage());
			return false;
		}
	}
}
