package com.example.cen_4802.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    //fields for login

    private TextField usernameField = new TextField("Username");
    //change this to a secret and figure out how to make input show up encrypted
    private TextField passwordField = new TextField("Password");

    public MainView(UserRepository repository){
        this.repository = repository;

        grid.setColumns("firstName", "lastName", "email");

        add(getForm(), grid);
        refreshGrid();
    }

    private Component getForm(){
        var addlayout = new HorizontalLayout();
        addlayout.setAlignItems(Alignment.BASELINE);

        //make it so that this is in vertical layout
        var loginLayout = new VerticalLayout();

        var loginButton = new Button("Login");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addlayout.add(firstName, lastName, email, addButton, loginButton);

        //inspects layout for fields that match the fields in the model and binds them together
        binder.bindInstanceFields(this);

        addButton.addClickListener(click -> {
            try{
                var username = new User();
                binder.writeBean(username);
                repository.save(username);
                refreshGrid();
            }catch(ValidationException e ){
                //
            }
        });

        loginButton.addClickListener(click -> {
            try{
                addlayout.add(usernameField, passwordField);
            }catch(Exception e){
                //
            }
        });

        return addlayout;

    }

    private void refreshGrid(){
        List<User> users = repository.findAll();
        grid.setItems(users);
    }
}
