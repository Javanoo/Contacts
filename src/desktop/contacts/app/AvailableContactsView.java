package desktop.contacts.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class AvailableContactsView extends BorderPane{
	private TreeSet<Contact> contacts;
	
	private TextField search =  new TextField();
	private HBox searchPane = new HBox();
	private ListView<Contact> list = new ListView<>();
		
	//action buttons
	private Button add = new Button();
	private Button options = new Button();
	private Button edit = new Button();
	private Button delete =  new Button();
	private Button cancel = new Button();
	//holder of buttons.
	private HBox bottomButtons = new HBox();
	//left side holds, the search, list and bottom buttons
	private BorderPane leftSide = new BorderPane();
	
	//right side holds either a contactView object or ContactFormView object
	private ContactView contactView;
	private ContactFormView form = new ContactFormView(contacts, this);
	private StackPane rightSide;
	
	private String backgroundcolor = "#19202de1";
	private String backgroundcolor2 = "#374845ff";
	
	AvailableContactsView(){
	}
	
	/*
	 * When instantiating this class, start by initializing this class's member data.
	 * hence this constructor starts by configuring the search & search pane, followed by the 
	 * listView then the bottomButtons and lastly placing all those objects on the left side and 
	 * placing either contact view or contact form on the right side.
	 */
	AvailableContactsView(TreeSet<Contact> contactList){
		contacts = contactList;
		
		//---------------------------SEARCH SECTION -------------------
		//search bar icon
		ImageView searchIcon = createIcon("search_black_48.png", 25);
		searchIcon.setOpacity(.7);
		
		//style 
		search.setPadding(new Insets(10, 20, 10, 10));
		search.setPromptText("Search");
		search.setFont(Font.font("ubuntu", 15));
		search.setStyle("-fx-background-color: transparent;" + 
										"-fx-color: black;");
		searchPane.setStyle("-fx-background-color: #f9f9f9ff; -fx-padding: 5");
		
		//register actions
		search.setOnAction(e -> search());
		search.setOnKeyPressed(e -> {if(e.getCode() == KeyCode.ENTER)
			search();});
		search.setOnKeyTyped(e -> search());
		
		//place the icon and field into the pane
		searchPane.getChildren().addAll(new StackPane(searchIcon), search);
		
		
		//---------------------------LIST SECTION ----------------------
		//place holder for when there no contacts to show
		StackPane placeHolder = new StackPane(new Label("Add your first contact"));
		placeHolder.getChildren().get(0).setStyle("-fx-font-family: ubuntu; -fx-opacity: .6");
		placeHolder.setStyle("-fx-background-color: none");
				
		//set the list's contacts
		list = new ListView<>(FXCollections.observableList(new LinkedList<>(contacts)));
		list.getSelectionModel().selectFirst();
		list.setFixedCellSize(60);

		//set the list's placeholder
		list.setPlaceholder(placeHolder);
				
		//update contact view when selected item changes
		getList().getSelectionModel().selectedItemProperty().addListener(e -> {
			if(list.getSelectionModel().getSelectedItem() != null) {
				setContactView(new ContactView(list.getSelectionModel().getSelectedItem()));
				setRightSide(new StackPane(contactView));
			}
		});
		
		
		//---------------------------BUTTONS SECTION ----------------------
		//add button
		add.setText("Create");
		Rectangle rect = new Rectangle(80, 30);
		rect.setArcHeight(30);
		rect.setArcWidth(30);
		add.setClip(rect);
		add.setMinWidth(80);
		add.setMinHeight(30);
		add.setAlignment(Pos.CENTER);
		add.setTextFill(Color.WHITE);
		add.setFont(Font.font("ubuntu", FontWeight.BOLD, 12));
		add.setStyle("-fx-background-color: " + backgroundcolor);
		add.setPadding(new Insets(5, 10, 5, 10));
		add.setOnMouseEntered(e -> add.setStyle("-fx-background-color: #6c5d532c;"));
		add.setOnMouseExited(e -> add.setStyle("-fx-background-color: " + backgroundcolor));
		add.setOnAction(e -> {
			form = new ContactFormView(contacts, this);
			form.getCancel().setOnAction(k -> {
				setRightSide(new StackPane(new ContactView(getList().getSelectionModel().getSelectedItem())));
				getLeft().setDisable(false);
				getCancel().fire();
			});
			setRightSide(new StackPane(form));
			getLeft().setDisable(true);
		});
				
		//selection button
		options = createButton("menu_black.png", 25, "options");
		options.getGraphic().setOpacity(.8);
		options.setOnAction(e -> optionToggled());
				
		//edit button
		edit = createButton("edit_black_48.png", 23, "edit contact");
		edit.setOnAction(e -> editContact());
				
		//delete button
		delete = createButton("trash_black_48.png", 23, "delete contacts");
		delete.setOnAction(e -> deleteContact());
				
		cancel = createButton("close_black_48.png", 23, "cancel");
		cancel.setOnAction(e -> {
			bottomButtons = new HBox();
			//add and delete buttons
			bottomButtons.setSpacing(136);
			bottomButtons.getChildren().addAll(add, options);
			bottomButtons.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9ff");
			setBottomButtons(bottomButtons);
			getList().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		});
				
		form.getCancel().setOnAction(e -> {
			setRightSide(new StackPane(new ContactView(getList().getSelectionModel().getSelectedItem())));
			getCancel().fire();
		});
		
		//
		//add and delete buttons
		bottomButtons.setAlignment(Pos.CENTER);
		bottomButtons.setStyle("-fx-padding: 5; -fx-background-color: #f9f9f9ff");
		bottomButtons.setSpacing(136);
		bottomButtons.setMinWidth(200);
		bottomButtons.getChildren().addAll(add, options);
		
		leftSide.setTop(searchPane);
		leftSide.setCenter(list);
		leftSide.setBottom(new StackPane(bottomButtons));
		leftSide.setMinWidth(200);
		
		//---------------------------LAYOUT SECTION ----------------------
		this.setLeft(leftSide);
		this.setCenter(rightSide);
		
		//if there no contacts show the add contact form view else 
		//show the available contacts
		if(getList().getSelectionModel().getSelectedItem() != null) {
			setRightSide(new StackPane(new ContactView(getList().getSelectionModel().getSelectedItem())));
			this.getCenter().setStyle("-fx-background-color: " + backgroundcolor);
		}else {
			createFirstContactForm();
			this.getCenter().setStyle("-fx-background-color: " + backgroundcolor);
		}
	}
	
	
	/**
	 * updates the bottom tab with new buttons when the options button is
	 * triggered.
	 */
	protected void optionToggled() {
		//change the selection model to allow multiple selections
		getList().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//replace the bottom buttons with new ones
		bottomButtons = new HBox();
		bottomButtons.setStyle("-fx-padding: 10, 0, 15, 0; -fx-background-color: #f9f9f9ff;");
		bottomButtons.getChildren().addAll(edit, delete, cancel);
		bottomButtons.setSpacing(75);
		setBottomButtons(bottomButtons);
	}
	
	/**
	 * creates a contact form view object and then populates the form's 
	 * fields with the contact's information for editing.
	 */
	@SuppressWarnings("unchecked")
	protected void editContact() {
		//get contact to edit
		Contact contactToEdit = getList().getSelectionModel().getSelectedItem();
	
		//generate the contact form
		form = new ContactFormView(contacts, this);
	
		//set name to edit
		((TextField)form.getNameField().getChildren().get(1)).setText(contactToEdit.getName());
		
		//set phone numbers to edit
		LinkedList<String> phoneNumbers = new LinkedList<>(contactToEdit.getPhoneNumbers().keySet());
		LinkedList<String> phoneNumberTags = new LinkedList<>(contactToEdit.getPhoneNumbers().values());
		((TextField)form.getPhoneField().getChildren().get(1)).setText(phoneNumbers.getFirst());
		((ComboBox<String>)form.getPhoneField().getChildren().get(2)).setValue(phoneNumberTags.getFirst());
		if(phoneNumbers.size() == 2) {
			((Button)form.getPhoneField().getChildren().get(3)).fire();
			((TextField)form.getPhoneField().getChildren().get(4)).setText(phoneNumbers.getLast());
			((ComboBox<String>)form.getPhoneField().getChildren().get(5)).setValue(phoneNumberTags.getLast());
		}
		
		//set emails to edit
		LinkedList<String> email = new LinkedList<>(contactToEdit.getEmail().keySet());
		LinkedList<String> emailTags = new LinkedList<>(contactToEdit.getEmail().values());
		((TextField)form.getEmailField().getChildren().get(1)).setText(email.getFirst());
		((ComboBox<String>)form.getEmailField().getChildren().get(2)).setValue(emailTags.getFirst());
		if(email.size() == 2) {
			((Button)form.getEmailField().getChildren().get(3)).fire();
			((TextField)form.getEmailField().getChildren().get(4)).setText(email.getLast());
			((ComboBox<String>)form.getEmailField().getChildren().get(5)).setValue(emailTags.getLast());
		}
		
		//set address to edit
		((TextField)form.getAddressField().getChildren().get(1)).setText(contactToEdit.getAddress());
		
		//set note to edit
		((TextInputControl)form.getNoteField().getChildren().get(1)).setText(contactToEdit.getNote());
	
		//register the save and cancel button's action event 
		//save the preferred alteration
		form.save.setOnAction(e -> {
			LinkedList<Contact> copyContactList = new LinkedList<>(contacts);
			copyContactList.set(copyContactList.indexOf(contactToEdit), form.generateContact());
			contacts = new TreeSet<>(copyContactList);
		 
			getCancel().setVisible(true);
			getList().setItems(FXCollections.observableList(new LinkedList<>(contacts)));
			getList().getSelectionModel().selectLast();
			setRightSide(new StackPane(new ContactView(getList().getSelectionModel().getSelectedItem())));
			if (getLeft().isDisabled())
				getLeft().setDisable(false);
		});
		
		//revert everything when the contact forms cancel button is triggered
		form.getCancel().setOnAction(k -> {
			setRightSide(new StackPane(new ContactView(getList().getSelectionModel().getSelectedItem())));
			getLeft().setDisable(false);
			getCancel().fire();
		});
		
		//enable the save button because both the name and phone number fields are not empty.
		form.save.setDisable(false);
	
		//set the view to right side and disable the left
		setRightSide(new StackPane(form));
		getLeft().setDisable(true);
 	}

	
	/**
	 * deletes (all) the selected contact(s).
	 */
	protected void deleteContact() {
		contacts.removeAll(list.getSelectionModel().getSelectedItems());
		getList().getSelectionModel().selectNext();
		getList().setItems(FXCollections.observableList(new LinkedList<>(contacts)));
			
		//if there still elements, show the selected one else
		//load the contact form
		if(!list.getItems().isEmpty()) {
			setContactView(new ContactView(list.getSelectionModel().getSelectedItem()));
			setRightSide(new StackPane(contactView));
		}else {
			createFirstContactForm();
		}
	}
	
	/**
	 * displays all available searched items.
	 */
	protected void search() {
		String query = getSearch().getText();
		TreeSet<Contact> matchedItems = new TreeSet<>();
		Iterator<Contact> iter = contacts.iterator();
		while(iter.hasNext() && !query.isEmpty()) {
			Contact contact = iter.next();
			if((contact.getName()).matches(query + ".*")) 
				matchedItems.add(contact);
		}
		getList().setItems((FXCollections.observableList(new LinkedList<>(matchedItems))));
		getLeftSide().setCenter(getList());
	}
	
	/**
	 * creates an icon with predetermined dimensions
	 * @param name, name of image to use 
	 * @param iconSize, dimesions of the icon
	 * @return image, the icon
	 */
	public ImageView createIcon(String name, double iconSize) {
		ImageView image = new ImageView(name);
		image.setFitHeight(iconSize);
		image.setFitWidth(iconSize);
		return image;
	}
	
	/**
	 * creates a styled buttons with generic actions registered for a more lively interaction 
	 * @param iconName, image to use as icon 
	 * @param iconSize, icon dimensions
	 * @param tip, tip describing the button
	 * @return Button
	 */
	public Button createButton(String iconName, double iconSize, String tip) {
		Button button = new Button();
		button.setGraphic(createIcon(iconName, iconSize));
		button.setStyle("-fx-background-color: #e3dedb5f;");
		button.setPadding(new Insets(5));
		button.setTooltip(new Tooltip(tip));
		button.getTooltip().setFont(Font.font("ubuntu", 14));
		
		button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #6c5d532c;"));
		button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e3dedb5f;"));
		
		Rectangle rect = new Rectangle(iconSize + 10, iconSize +10);
		rect.setArcHeight(30);
		rect.setArcWidth(30);
		button.setClip(rect);
		
		return button;
	}
	
	/**
	 * creates first ever contact form, used when there no contacts at all. 
	 */
	public void createFirstContactForm() {
		getLeft().setDisable(true);
		form = new ContactFormView(contacts, this);
		form.getCancel().setVisible(false);
		setRightSide(new StackPane(form));
		
	}
	
	
//---------------------------------------Getter/Setter methods -----------------------------------------------	
	/**
	 * @return the search
	 */
	public TextField getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(TextField search) {
		this.search = search;
	}

	/**
	 * @return the list
	 */
	public ListView<Contact> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ListView<Contact> list) {
		this.list = list;
		fadeIn(getList());
	}

	/**
	 * @return the contactView
	 */
	public ContactView getContactView() {
		return contactView;
	}

	/**
	 * @param contactView the contactView to set
	 */
	public void setContactView(ContactView contactView) {
		this.contactView = contactView;
	}

	/**
	 * @return the add
	 */
	public Button getAdd() {
		return add;
	}

	/**
	 * @param add the add to set
	 */
	public void setAdd(Button add) {
		this.add = add;
	}

	/**
	 * @return the select
	 */
	public Button getSelect() {
		return options;
	}

	/**
	 * @param select the select to set
	 */
	public void setSelect(Button select) {
		this.options = select;
	}

	/**
	 * @return the edit
	 */
	public Button getEdit() {
		return edit;
	}

	/**
	 * @param edit the edit to set
	 */
	public void setEdit(Button edit) {
		this.edit = edit;
	}

	/**
	 * @return the delete
	 */
	public Button getDelete() {
		return delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(Button delete) {
		this.delete = delete;
	}

	/**
	 * @return the bottomButtons
	 */
	public HBox getBottomButtons() {
		return bottomButtons;
	}

	/**
	 * @param bottomButtons the bottomButtons to set
	 */
	public void setBottomButtons(HBox bottomButtons) {
		this.bottomButtons = bottomButtons;
		fadeIn(getBottomButtons());
		leftSide.setBottom(bottomButtons);
	}

	/**
	 * @return the leftSide
	 */
	public BorderPane getLeftSide() {
		return leftSide;
	}

	/**
	 * @param leftSide the leftSide to set
	 */
	public void setLeftSide(BorderPane leftSide) {
		this.leftSide = leftSide;
		fadeIn(getLeftSide());
	}

	/**
	 * @return the rightSide
	 */
	public StackPane getRightSide() {
		return rightSide;
	}

	/**
	 * @param rightSide the rightSide to set
	 */
	public void setRightSide(StackPane rightSide) {
		this.rightSide = rightSide;
		this.setCenter(this.rightSide);
		this.getCenter().setStyle("-fx-background-color: " + backgroundcolor);
		if(getRightSide().getChildren().get(0) instanceof ContactView)
			fadeIn((ContactView)getRightSide().getChildren().get(0));
		else
			fadeIn((ContactFormView)getRightSide().getChildren().get(0));
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
	
	
//---------------------------------------Animation functions -----------------------------------------------		
	public void fadeIn(Parent E) {
		FadeTransition fade = new FadeTransition(new Duration(300), E);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.setFromValue(0.2);
		fade.setToValue(1);
		fade.play();
	}
}
