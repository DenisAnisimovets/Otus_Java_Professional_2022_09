package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "client")
public class Client implements Cloneable {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private List<Phone> phones;


    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        this.phones = new ArrayList<>();
        this.address = null;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = new Address(address);
        this.phones = phones;
        this.phones.forEach(phone -> phone.setClient(this));
    }

    public Client() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Phone> copyPhones(List<Phone> phones) {
        if(phones == null) {
            return null;
        }
        return phones.stream().peek(phone -> phone.setClient(this)).toList();
    }

    @Override
    public Client clone() {
        List<Phone> newPhones = new ArrayList<>();
        Client newClient = new Client(this.id, this.name, this.getAddress(), newPhones);
        phones.forEach(phone -> {
            Phone newPhone = new Phone(phone.getId(), phone.getNumber());
            newPhone.setClient(newClient);
            newPhones.add(newPhone);
        });

        return newClient;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPhones() {
        if(phones == null) {
            return "";
        } else {
            return phones.stream().map(phone -> phone.toString()).collect(Collectors.joining(", "));
        }
    }
}
