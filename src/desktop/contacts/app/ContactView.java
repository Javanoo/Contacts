/**
 * @author matthews offen
 */
package desktop.contacts.app;

import java.util.HashMap;
import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
* This class creates objects that display a detailed view of a contact.
* its instantiation requires a Contact Object. the contact object is an instance that 
* contains all contact information such as name, phone numbers, email, address and a short
* description note(that either identifies the contact or has additional details).
* 
* At the very bottom of this view(when instantiated), there's an information location entry, which shows where 
* the information is coming from (thus either on your local machine or the cloud such as google drive).
* As of now the location of data is only on your location, later on, a feature might be added to allow you to store 
* your contacts storage on google drive.
*/
public class ContactView extends BorderPane {
	private Contact contact = null;

	private Label name = new Label("unknown");

	private VBox phoneEntryView = new VBox();

	private VBox emailEntryView = new VBox();

	private GridPane address = new GridPane();
	private GridPane note = new GridPane();

	private GridPane informationLocation = new GridPane();

	/**
	 * @implNote contact objects are created using the {@link Contact} class. 
	 * @param contact, the Contact Object to display information of.
	 */
	ContactView(Contact contact){
		this.contact = contact;

		ProfId profPic = new ProfId(contact.getName().charAt(0));;

		name.setText(contact.getName().replace(contact.getName().charAt(0), contact.getName().toUpperCase().charAt(0)));
		name.setTextFill(Color.WHITE);
		name.setFont(Font.font("ubuntu", FontWeight.BOLD, 24));

		//get numbers from contact
		loadItems(contact.getPhoneNumbers(), phoneEntryView, "smartphone_black_48.png", 30);
		phoneEntryView.setAlignment(Pos.CENTER);
		phoneEntryView.setSpacing(3);

		//get emails
		loadItems(contact.getEmail(), emailEntryView, "email_black_48.png", 28);
		emailEntryView.setAlignment(Pos.CENTER);
		emailEntryView.setSpacing(3);

		//creates the address/location grid pane
		address = populateGridpaneField(contact.getAddress(), "location", "location_black_48.png", 30, 450, 50);


		//creates the note grid pane
		note = populateGridpaneField("Note", contact.getNote(), "notes_black_48.png", 28, 450, 118);
		GridPane.setRowSpan(note.getChildren().get(0), 1);

		//Note: not automatically populated because its shown a little bit different
		//from the above panes.
		//informationLocation is an indicator of where the contact is taken from, either from your computer  
		//or from google drive( a feature sonn to be added).
		ImageView inforLocationIcon = createIcon("ssd_black_48.png", 28);
		inforLocationIcon.setOpacity(.8);
		informationLocation.add(inforLocationIcon, 0, 0);
		informationLocation.add(new Text("On Device"), 1, 0);
		informationLocation.setHgap(5);
		informationLocation.setMinHeight(50);
		informationLocation.setMaxWidth(450);
		((Text)informationLocation.getChildren().get(1)).setFont(Font.font("ubuntu", FontWeight.BOLD, 14));
		((Text)informationLocation.getChildren().get(1)).setOpacity(.8);
		informationLocation.setStyle("-fx-background-color: #f9f9f9ff;");
		informationLocation.setAlignment(Pos.CENTER);

		//get the rounded border effect for the information location pane
		Rectangle rect3 = new Rectangle(450, 45);
		rect3.setArcHeight(10.0);
		rect3.setArcWidth(10.0);
		informationLocation.setClip(rect3);

		//place all the above panes into the final pane
		//this excludes the inforlocation pane.
		VBox detail = new VBox(name, phoneEntryView, emailEntryView, address, note);
		detail.setAlignment(Pos.CENTER);
		detail.setSpacing(30);

		//place the final panes(lettered id logo, details and inforlocation) into this pane.
		//initialize the object.
		this.setTop(profPic.getProfView());
		this.setCenter(detail);
		this.setBottom(informationLocation);
		
		
		BorderPane.setAlignment(informationLocation, Pos.CENTER);
		BorderPane.setMargin(detail, new Insets(40, 0, -180, 0));
		this.getTop().setTranslateY(-150);
		this.getCenter().setTranslateY(-150);
	}

	/**
	 * This creates a grid pane with icon, label and text placed in it.
	 * @param label, the panes label
	 * @param paneText, the pane's text, more like label's caption
	 * @param imageName, icon name
	 * @param imageSize, icon size
	 * @param paneWidth, pane's maximum width
	 * @param paneHeight, pane's minimum height
	 * @return GridPane
	 */
	protected GridPane populateGridpaneField(String label, String paneText, String imageName, int imageSize,
			int paneWidth, int paneHeight) {
		GridPane pane = new GridPane();
		Label paneLabel = new Label(label);
		Text text = new Text(paneText);
		ImageView image = createIcon(imageName, imageSize);

		pane.setMinHeight(paneHeight);
		pane.setMaxWidth(paneWidth);
		pane.setHgap(5);
		pane.setStyle("-fx-background-color: #f9f9f9ff; -fx-padding: 10");

		paneLabel.setFont(Font.font("ubuntu", FontWeight.BOLD, 14));

		text.setFont(Font.font("ubuntu"));
		text.setOpacity(.5);

		image.setOpacity(.8);

		pane.add(image, 0, 0);
		pane.add(paneLabel, 1, 0);
		pane.add(text, 1, 1);
		GridPane.setRowSpan(image, 2);

		Rectangle rect = new Rectangle(pane.getMaxWidth(), pane.getMinHeight());
		rect.setArcHeight(10.0);
		rect.setArcWidth(10.0);
		pane.setClip(rect);

		return pane;
	}


	/**
	 * This loads information into gridpanes which are dynamically created and later on added to a 
	 * VBox.
	 * @param size, dimensions of the icon
	 * @param name, name of the image to be used as an icon
	 * @param View, name of the VBox within which the gridpanes will be placed
	 * @param list, storage of the numbers or emails
	 */
	protected void loadItems(HashMap<String, String> list, VBox view, String name, int size) {
		LinkedList<String> items = new LinkedList<>(list.keySet());
		for(String elem : items) {
			//place the panes
			view.getChildren().add(populateGridpaneField(elem, list.get(elem), name, size, 450, 50));
		}
	}

	/**
	 * creat a custom icon and set its dimensions.
	 * @param name
	 * @param size
	 * @return
	 */
	protected ImageView createIcon(String name, int size) {
		ImageView image = new ImageView(name);
		image.setFitHeight(size);
		image.setFitWidth(size);
		return image;
	}
	
	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}
	
	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	/**
	 * This inner class is for creating the lettered id logo
	 * object that's centered at the top of the contactView UI.
	 */
	class ProfId {
		private Circle backCircle = new Circle(130);
		private Label letterId = new Label();
		private StackPane profView;
		private Color backColor;

		ProfId(char l){
			
			//capitalize the letter 
			letterId.setText((String.valueOf(l)).toUpperCase());
			
			//set color and font of the letter 
			letterId.setTextFill(Color.WHITE);
			letterId.setFont(Font.font("Glacial Indifference", FontWeight.BOLD, 80));
			
			//set background color of the circle
			backColor = new Color(contact.getProfileColors()[0], contact.getProfileColors()[1], contact.getProfileColors()[2], .4);
			backCircle.setFill(backColor);
			
			//creates the lettered Logo item
			profView = new StackPane(backCircle, letterId);
			letterId.setTranslateY(60);
		}

		/**
		 * @return profView, a stack pane consisting the lettered logo item
		 */
		public StackPane getProfView() {
			return this.profView;
		}
	}
}