package com.cen4802Project.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AddTest {
    User newUser;

    @BeforeEach
    public void setUp() {
        newUser = new User();
        newUser.setUsername("Lupapers");
        newUser.setPassword("T3stP@ssword");
        newUser.setFirstName("Luis");
        newUser.setLastName("Duarte");
        newUser.setEmail("Test@email.com");
    }

    //tests adding a new user
    @Test
    public void testAdd() {
        assertEquals("Lupapers", newUser.getUsername(), "User could not be added.");
        assertEquals("T3stP@ssword", newUser.getPassword(), "Password could not be added");
        assertEquals("Luis", newUser.getFirstName(), "First name could not be added");
        assertEquals("Duarte", newUser.getLastName(), "Last name could not be added");
        assertEquals("Test@email.com", newUser.getEmail(), "Email could not be added");
    }

}
