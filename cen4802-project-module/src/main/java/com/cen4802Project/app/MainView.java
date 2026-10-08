package com.cen4802Project.app;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("")
public class MainView extends VerticalLayout {
    private UserRepository repository;
    @PropertyId("username") private TextField usernameField = new TextField("Username");
    @PropertyId("password") private PasswordField passwordField = new PasswordField("Password");
    @PropertyId("firstName")private TextField firstNameField = new TextField("First Name");
    @PropertyId("lastName")private TextField lastNameField = new TextField("Last Name");
    @PropertyId("email") private EmailField emailField = new EmailField("Email");
    //binder lets us connect a UI input field with a data model field
    private Binder<User> binder = new Binder<>(User.class);
    private Grid<User> grid = new Grid<>(User.class);


    public MainView(UserRepository repository) {
        this.repository = repository;

        grid.setColumns("username", "password", "firstName", "lastName", "email");

        add(getForm(), grid);
        refreshGrid();
    }

    private Component getForm() {
        var layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);

        TextField searchField = new TextField("Search Field");
        searchField.setMaxLength(400);
        searchField.setWidth("400px");
        searchField.setHeight("50px");

        Button searchButton = new Button("Search");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");

        layout.add(firstNameField, lastNameField, emailField, addButton, searchField, searchButton, loginButton, signUpButton);

        //inspects layout for fields that match the fields in the model and binds them together
        binder.bindInstanceFields(this);

        addButton.addClickListener(click -> {
            try {
                var user = new User();
                binder.writeBean(user);
                repository.save(user);
                refreshGrid();
                addButton.setText("Successfully added user.");
            } catch (ValidationException e) {
                System.out.println("User could not be added.");
            }
        });

        loginButton.addClickListener(c ->{
           openLoginDialog();
        });
        signUpButton.addClickListener(c ->{
           openSignUpDialog();
        });

        return layout;
    }

    private void refreshGrid() {
        List<User> users = repository.findAll();
        grid.setItems(users);
    }

    String username = "";
    String password = "";

    public void openLoginDialog(){
        Dialog dialog = new Dialog();
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        usernameField.addValueChangeListener(e -> {
            username = e.getValue();

        });
        passwordField.addValueChangeListener(e -> {
            password = e.getValue();
        });
        Button closeButton = new Button("Close", e -> {
            dialog.close();
        });
        Button loginButton = new Button("Login",e ->{
            //
        });
        dialog.add(usernameField, passwordField, loginButton, closeButton);
        dialog.addOpenedChangeListener(e ->{
            if(!e.isOpened()){
                System.out.println("Values entered:" + username + password);
            }
        });
        dialog.open();
    }

    String firstName = "";
    String lastName = "";
    String email = "";

    public void openSignUpDialog(){
        Dialog dialog = new Dialog();
        var layout = new VerticalLayout();
        usernameField.addValueChangeListener(e ->{
           username = e.getValue();
        });
        passwordField.addValueChangeListener(e ->{
            password = e.getValue();
        });
        firstNameField.addValueChangeListener(e ->{
           firstName = e.getValue();
        });
        lastNameField.addValueChangeListener(e ->{
           lastName = e.getValue();
        });
        emailField.addValueChangeListener(e ->{
            email = e.getValue();
        });
        Button closeButton = new Button("Close", e -> {
            dialog.close();
        });
        Button signUpButton = new Button("Sign up",e ->{
            //
        });
        layout.add(usernameField, passwordField, firstNameField, lastNameField, emailField);
        dialog.add(layout, signUpButton, closeButton);
        dialog.addOpenedChangeListener(e ->{
            if(!e.isOpened()){
                System.out.println("Values entered:" + username);
                System.out.println("Values entered:" + password);
                System.out.println("Values entered:" + firstName);
                System.out.println("Values entered:" + lastName);
                System.out.println("Values entered:" + email);
            }
        });
        dialog.open();
    }
}
