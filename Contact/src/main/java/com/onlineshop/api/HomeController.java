package com.onlineshop.api;


import com.onlineshop.business.ContactService;
import com.onlineshop.business.FileUploadUtil;
import com.onlineshop.business.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
//import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

//    private static String uploadDir = "src/main/resources/static/photos";

    @Autowired
    ContactService contactService;

    @GetMapping({"/", "/home"})
    public String home(Model model, String keyword){

        if (keyword != null){
            model.addAttribute("contacts", contactService.getContactByName(keyword));
        }else {
            model.addAttribute("contacts", contactService.getAllContacts());
        }
        return "index";
    }

    @GetMapping("/addContact")
    public String contactAddGet(Model model){
        model.addAttribute("contact", new Contact());
        return "contactAdd";
    }

    @PostMapping("/addContact")
    public String contactAddPost(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("contactImage") MultipartFile file) throws IOException{

        Contact newContact = new Contact();
        newContact.setId(contact.getId());
        newContact.setAddress(contact.getAddress());
        newContact.setName(contact.getName());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "photos/" + fileName;

        newContact.setImageName(fileName);
        contactService.addContact(newContact);

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return "redirect:/home";
    }

    @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable UUID id){
        contactService.removeContactById(id);
        return "redirect:/home";
    }

    @GetMapping("/updateContact/{id}")
    public String updateProduct(@PathVariable UUID id, Model model){
        Contact contact = contactService.getContactById(id).get();
        Contact updatedContact = new Contact();
        updatedContact.setId(contact.getId());
        updatedContact.setName(contact.getName());
        updatedContact.setAddress(contact.getAddress());
//        updatedContact.setImageName(contact.getImageName());

        model.addAttribute("contact", updatedContact);

        return "contactAdd";
    }

    @GetMapping("/home/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=contacts_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Contact> listContacts = contactService.getAllContacts();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "Address", "Full Name"};
        String[] nameMapping = {"id", "address", "name"};

        csvWriter.writeHeader(csvHeader);

        for (Contact contact : listContacts) {
            csvWriter.write(contact, nameMapping);
        }

        csvWriter.close();
    }
}
