package com.example.application.views.masterdetailviewdesigner;

import java.util.Optional;

import com.example.application.data.entity.Book;
import com.example.application.data.service.BookService;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.templatemodel.TemplateModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

@Route(value = "master-detail-view-designer")
@PageTitle("master-detail-view-designer")
@JsModule("./views/masterdetailviewdesigner/masterdetailviewdesigner-view.js")
@Tag("masterdetailviewdesigner-view")
public class MasterdetailviewdesignerView extends PolymerTemplate<TemplateModel> {

    // This is the Java companion file of a design
    // You can find the design file in
    // /frontend/src/views/views/masterdetailviewdesigner/masterdetailviewdesigner-view.js
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    // Grid is created here and not mapped from the template so we can pass the
    // class to the constructor
    private Grid<Book> grid = new Grid<>(Book.class, false);

        @Id
    private Upload image;
    @Id
    private TextField name;
    @Id
    private TextField author;
    @Id
    private DatePicker publicationDate;
    @Id
    private NumberField pages;
    @Id
    private TextField isbn;
    @Id
    private TextField price;


    @Id
    private Button cancel;
    @Id
    private Button save;

    private Binder<Book> binder;

    private Book book;

    public MasterdetailviewdesignerView(@Autowired BookService bookService) {
        setId("masterdetailviewdesigner-view");

        TemplateRenderer<Book> imageRenderer = TemplateRenderer.<Book>of("<span style='width:var(--lumo-icon-size-l);height:var(--lumo-icon-size-l);border-radius: 50%; overflow: hidden; display: flex; align-items: center; justify-content: center'><img style='height:100%;' src='[[item.image]]'></span>").withProperty("image", Book::getImage);
grid.addColumn(imageRenderer).setHeader("Image").setWidth("68px").setFlexGrow(0);

         grid.addColumn("name").setAutoWidth(true);
         grid.addColumn("author").setAutoWidth(true);
         grid.addColumn("publicationDate").setAutoWidth(true);
         grid.addColumn("pages").setAutoWidth(true);
         grid.addColumn("isbn").setAutoWidth(true);
         grid.addColumn("price").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);

        grid.setDataProvider(new CrudServiceDataProvider<Book, Void>(bookService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        // Add to the `<slot name="grid">` defined in the template
        grid.getElement().setAttribute("slot", "grid");
        getElement().appendChild(grid.getElement());

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Book> bookFromBackend = bookService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (bookFromBackend.isPresent()) {
                    populateForm(bookFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new Binder<>(Book.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.book == null) {
                    this.book = new Book();
                }
                binder.writeBean(this.book);
                bookService.update(this.book);
                clearForm();
                refreshGrid();
                Notification.show("Book details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the book details.");
            }
        });
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Book value) {
        this.book = value;
        binder.readBean(this.book);
    }
}
