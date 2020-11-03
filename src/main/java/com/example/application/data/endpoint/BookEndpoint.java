package com.example.application.data.endpoint;


import com.example.application.data.CrudEndpoint;
import com.example.application.data.entity.Book;
import com.example.application.data.service.BookService;
import com.vaadin.flow.server.connect.Endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;

@Endpoint
public class BookEndpoint extends CrudEndpoint<Book, Integer> {

    private BookService service;

    public BookEndpoint(@Autowired BookService service) {
        this.service = service;
    }

    @Override
    protected BookService getService() {
        return service;
    }

}
