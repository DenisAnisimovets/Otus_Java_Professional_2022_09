package ru.otus.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;

@Controller
public class ClientController {
    private final DBServiceClient dbServiceClient;
    private final Gson gson = new Gson();

    public ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/", "/clients"})
    public String clientsListView(Model model) {
        List<Client> clientList = dbServiceClient.findAll();
        model.addAttribute("clients", clientList);
        return "clients";
    }

    @PostMapping("/api/client")
    public RedirectView clientSave(@RequestBody String body) {
        Client newClient = gson.fromJson(body, Client.class);
        dbServiceClient.saveClient(newClient);
        return new RedirectView("/clients", true);
    }
}
