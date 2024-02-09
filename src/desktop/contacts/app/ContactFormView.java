package desktop.contacts.app;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ContactFormView extends BorderPane {
	
	Contact contact = null; //contact to be generated
	
	GridPane nameField = new GridPane();
	GridPane phoneField = new GridPane();
	GridPane emailField = new GridPane();
	GridPane addressField = new GridPane();
	GridPane noteField = new GridPane();
	
	Button save = new Button(); //create and contact and save
	Button cancel = new Button(); //cancel operation
	HBox actionButtons = new HBox();
	
	VBox detailLayout = new VBox();
	
	private SimpleIntegerProperty saveSwitch = new SimpleIntegerProperty(0);
	
	ContactFormView(TreeSet<Contact> contacts, AvailableContactsView view){
		
		//initialize data entry fields
		nameField = createSimpleEntryField("edit_black_48.png", "Full name", 450, 50, VPos.CENTER, 0);
		phoneField = createCompactEntryField("smartphone_black_48.png", "Phone number", Contact.PHONENUMBERlABEL);
		emailField = createCompactEntryField("email_black_48.png", "Primary email", Contact.EMAILlABEL);
		addressField = createSimpleEntryField("location_black_48.png", "Location", 450, 50, VPos.CENTER, 0);
		noteField = createSimpleEntryField("notes_black_48.png", "add a short description", 450, 100, VPos.TOP, 1);
		
		save = createButton("save_white_48.png", 23, "save");
		cancel = createButton("close_white_48.png", 23, "cancel [esc]");
		
		save.setDisable(true);
		save.setOnAction(e -> {
			Contact newContact =  generateContact();
			contacts.add(newContact);
			getCancel().setVisible(true);
			view.getList().setItems(FXCollections.observableList(new LinkedList<>(contacts)));
			view.getList().getSelectionModel().select(newContact);
			view.setRightSide(new StackPane(new ContactView(view.getList().getSelectionModel().getSelectedItem())));
			if (view.getLeft().isDisabled())
				view.getLeft().setDisable(false);
		});
		
		actionButtons.setSpacing(390);
		actionButtons.setAlignment(Pos.CENTER);
		actionButtons.getChildren().addAll(save, cancel);
		actionButtons.setStyle("-fx-padding: 10");
		actionButtons.setMaxWidth(450);
		
		detailLayout.getChildren().addAll(nameField, phoneField, emailField, addressField, noteField);
		detailLayout.setAlignment(Pos.CENTER);
		detailLayout.setPadding(new Insets(5, 20, 5, 20));
		detailLayout.setSpacing(30);
		detailLayout.setTranslateY(30);
		
		this.setOnKeyPressed(e -> {if(e.getCode() == KeyCode.ESCAPE) cancel.fire();});
		this.setCenter(detailLayout);
		this.setBottom(new StackPane(actionButtons));
		
		//short cuts
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ESCAPE)
				cancel.fire();
		});
		
		//enable saving when user enters at least the name and number of the contact 
		((TextField)nameField.getChildren().get(1)).setOnKeyTyped(e -> {
			if(!((TextField)nameField.getChildren().get(1)).getText().isEmpty() && 
					!((TextField)phoneField.getChildren().get(1)).getText().isEmpty())
				saveSwitch.set(1);
			else
				saveSwitch.set(0);
		});
		
		((TextField)phoneField.getChildren().get(1)).setOnKeyTyped(e -> {
			if(!((TextField)phoneField.getChildren().get(1)).getText().isEmpty() && 
					!((TextField)nameField.getChildren().get(1)).getText().isEmpty())
				saveSwitch.set(1);
			else
				saveSwitch.set(0);
		});
		
		saveSwitch.addListener(e -> {
			if(saveSwitch.getValue() == 0)
				save.setDisable(true);
			else
				save.setDisable(false);
		});
	}
	
	public GridPane createCompactEntryField(String iconName, String prompt, String[] labelTags) {
		ImageView icon = createIcon(iconName, 30);
		TextField textInput = createTextField(prompt);
		ComboBox<String> label = createComboBox(labelTags, 0);
		Button addRow = new Button("+");
		GridPane field = new GridPane();
		icon.setOpacity(.8);
		
		field.add(icon, 0, 0);
		field.add(textInput, 1, 0);
		field.add(label, 2, 0);
		field.add(addRow, 0, 1);
		GridPane.setColumnSpan(addRow, 3);
		GridPane.setHalignment(addRow, HPos.CENTER);;
		addRow.setMinWidth(430);
		addRow.setMinHeight(50);
		addRow.setStyle("-fx-background-color: transparent;" +
				"-fx-border-style: solid; -fx-border-color: #6c5d5376; -fx-border-width: 2.5; -fx-border-radius: 5");
		field.setMinHeight(120);
		field.setMaxWidth(450);
		field.setHgap(2);
		field.setStyle("-fx-background-color: #f9f9f9ff; -fx-padding: 10");
		
		Rectangle rect = new Rectangle(field.getMaxWidth(), field.getMinHeight());
		rect.setArcHeight(10.0);
		rect.setArcWidth(10.0);
		field.setClip(rect);
		
		addRow.setOnAction(e -> addRow(iconName, prompt, labelTags, field, 1));
		
		return field;
	}

	public GridPane createSimpleEntryField(String iconName, String prompt, double width, double height, VPos v, int s) {
		ImageView icon = createIcon(iconName, 30);
		javafx.scene.control.TextInputControl textInput;
		textInput = (s == 1) ? new TextArea() : new TextField();
		GridPane field = new GridPane();
		
		icon.setOpacity(.8);
		textInput.setPromptText(prompt);
		textInput.setFont(Font.font("ubuntu", FontWeight.SEMI_BOLD, 15));
		textInput.setStyle("-fx-background-color: transparent;");
		
		field.add(icon, 0, 0);
		field.add(textInput, 1, 0);
		
		field.setMinHeight(height);
		field.setMaxWidth(width);
		field.setHgap(5);
		field.setStyle("-fx-background-color: #f9f9f9ff; -fx-padding: 5");
		textInput.setMinWidth(405);
		if(textInput instanceof TextField)
			textInput.setMinHeight(height - 10);
		else {
			textInput.setMaxHeight(height - 10);
		}
		GridPane.setValignment(icon, v);
		
		Rectangle rect = new Rectangle(field.getMaxWidth(), field.getMinHeight());
		rect.setArcHeight(15.0);
		rect.setArcWidth(15.0);
		field.setClip(rect);
		
		return field;
	}
	public TextField createTextField(String prompt) {
		TextField textInput = new TextField();
		
		textInput.setPromptText(prompt);
		textInput.setFont(Font.font("ubuntu", FontWeight.SEMI_BOLD, 15));
		textInput.setMinHeight(50);
		textInput.setMinWidth(295);
		textInput.setStyle("-fx-background-color: transparent;");
		
		return  textInput;
	}
	
	public ComboBox<String> createComboBox(String[] labelTags, int n){
		ObservableList<String> labels = FXCollections.observableArrayList(labelTags);
		ComboBox<String> label = new ComboBox<>();
		
		label.setItems(labels);
		label.setValue(labels.get(n));
		label.setMinHeight(50);
		label.setStyle("-fx-font-family: ubuntu; -fx-background-color: transparent; -fx-font-weight: bold;");
		
		return label;
	}
	
	/**
	 * add additional phone number or email fields for 
	 * alternative numbers or mails. prompt defines the default prompt text.
	 * @param text
	 * @param prompt
	 * @param label
	 * @param fieldPane
	 */
	protected void addRow(String iconName, String prompt, String[] labelTags, GridPane pane, int n) {
		TextField newTextInput = createTextField("Alternate " + prompt.substring(prompt.indexOf(' ') + 1));
		ImageView icon = createIcon(iconName, 30);
		ComboBox<String> label = createComboBox(labelTags, n);
		icon.setOpacity(.8);
		
		Button removeRow = new Button("-");
		removeRow.setOnAction(e -> removeRow(iconName, prompt, labelTags, pane));
		
		GridPane.setColumnSpan(removeRow, 3);
		GridPane.setHalignment(removeRow, HPos.CENTER);;
		removeRow.setMinWidth(430);
		removeRow.setMinHeight(50);
		removeRow.setStyle("-fx-background-color: transparent;" +
				"-fx-border-style: solid; -fx-border-color: #6c5d5376; -fx-border-width: 2.5; -fx-border-radius: 5");
		
		pane.getChildren().remove(pane.getChildren().size() - 1);
		pane.add(icon, 0, 1);
		pane.add(newTextInput, 1, 1);
		pane.add(label, 2, 1);
		pane.add(removeRow, 0, 2);
		pane.setMinHeight(170);
		
		Rectangle rect = new Rectangle(pane.getMaxWidth(), pane.getMinHeight());
		rect.setArcHeight(10.0);
		rect.setArcWidth(10.0);
		pane.setClip(rect);
	}
	
	protected void removeRow(String iconName, String prompt, String[] labelTags, GridPane pane) {
		Button addRow = new Button("+");
		addRow.setOnAction(e -> addRow(iconName, prompt, labelTags, pane, 1));
		
		GridPane.setColumnSpan(addRow, 3);
		GridPane.setHalignment(addRow, HPos.CENTER);;
		addRow.setMinWidth(430);
		addRow.setMinHeight(50);
		addRow.setStyle("-fx-background-color: transparent; " +
				"-fx-border-style: solid; -fx-border-color: #6c5d5376; -fx-border-width: 2; -fx-border-radius: 5");
		pane.getChildren().remove(3);
		pane.getChildren().remove(3);
		pane.getChildren().remove(3);
		pane.getChildren().remove(3);
		pane.add(addRow, 0, 1);
		pane.setMinHeight(120);
		
		Rectangle rect = new Rectangle(pane.getMaxWidth(), pane.getMinHeight());
		rect.setArcHeight(10.0);
		rect.setArcWidth(10.0);
		pane.setClip(rect);
	}
	
	/**
	 * create a new contact from the entered details
	 * @return Contact, otherwise fails if name is blank.
	 */
	public Contact generateContact() {
		String name = ((TextField)nameField.getChildren().get(1)).getText();
		String address = ((TextField)addressField.getChildren().get(1)).getText();
		String note = ((TextArea)noteField.getChildren().get(1)).getText();
		
		//get phone numbers
		HashMap<String, String> numbers = new HashMap<>();
		loadItems(numbers, phoneField);
		
		//get emails
		HashMap<String, String> mails = new HashMap<>();
		loadItems(mails, emailField);
		
		contact = new Contact(name, numbers, mails, address, note);
		contact.setProfileColors(new double[] {Math.random(), Math.random(), Math.random()});
		return contact;
	}
	
	public ImageView createIcon(String name, double iconSize) {
		ImageView image = new ImageView(name);
		image.setFitHeight(iconSize);
		image.setFitWidth(iconSize);
		return image;
	}
	
	public Button createButton(String iconName, double iconSize, String tip) {
		Button button = new Button();
		button.setGraphic(createIcon(iconName, iconSize));
		button.setStyle("-fx-background-color: #e3dedb15;");
		button.setPadding(new Insets(5));
		button.setTooltip(new Tooltip(tip));
		button.getTooltip().setFont(Font.font("ubuntu", 14));
		
		button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e3dedb5f;"));
		button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e3dedb15;"));
		
		Rectangle rect = new Rectangle(iconSize + 10, iconSize +10);
		rect.setArcHeight(30);
		rect.setArcWidth(30);
		button.setClip(rect);
		
		return button;
	}
	
	/**
	 * extract entered details from the text fields and combo box
	 * @param storage
	 * @param pane
	 */
	@SuppressWarnings("unchecked") //casting hbox item to combo box
	public void loadItems(HashMap<String, String> storage, GridPane pane) {
		int textIndex = 1;
		for(int i = 1; i <= (pane.getChildren().size() / 3); i++) {
			storage.put(((TextField)pane.getChildren().get(textIndex)).getText(),
					((ComboBox<String>)pane.getChildren().get(textIndex + 1)).getValue());
			textIndex += 3;
		}
	}

	/**
	 * @return the cancel
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	/**
	 * @return the nameField
	 */
	public GridPane getNameField() {
		return nameField;
	}

	/**
	 * @return the phoneField
	 */
	public GridPane getPhoneField() {
		return phoneField;
	}

	/**
	 * @return the emailField
	 */
	public GridPane getEmailField() {
		return emailField;
	}

	/**
	 * @return the addressField
	 */
	public GridPane getAddressField() {
		return addressField;
	}

	/**
	 * @return the noteField
	 */
	public GridPane getNoteField() {
		return noteField;
	}
}