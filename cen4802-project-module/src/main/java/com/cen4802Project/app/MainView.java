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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("")
public class MainView extends VerticalLayout {
    private UserRepository repository;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private EmailField email = new EmailField("Email");
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

        Button addButton = new Button("Add");
        Button loginButton = new Button("Login");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(firstName, lastName, email, addButton, loginButton);

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
        TextField usernameField = new TextField("username");
        PasswordField passwordField = new PasswordField("password");

        usernameField.addValueChangeListener(e -> {
            username = e.getValue();
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
}
