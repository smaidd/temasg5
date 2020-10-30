package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public String showListForm (Model model){
        model.addAttribute("contacts", contactService.getAllContacts());
        return "listForm";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model){
        ContactModel contact = "<new>".equals(id) ? new ContactModel() : contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "editForm";
    }
    @GetMapping("/remove/{id}")
    public String removeContact(@PathVariable("id") String id, Model model) throws IOException {
        contactService.removeContact(id);
        return "redirect:/";
    }
    @PostMapping("/save")
    public String saveContact(ContactModel contactModel, Model model){
        contactService.saveContact(contactModel);
       return "redirect:/";
    }
}
