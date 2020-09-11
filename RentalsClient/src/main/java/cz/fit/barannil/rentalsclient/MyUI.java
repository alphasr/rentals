package cz.fit.barannil.rentalsclient;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import cz.fit.barannil.rentalsclient.dto.Car;
import cz.fit.barannil.rentalsclient.dto.Customer;
import cz.fit.barannil.rentalsclient.dto.Rental;
import cz.fit.barannil.rentalsclient.services.CarClient;
import cz.fit.barannil.rentalsclient.services.CustomerClient;
import cz.fit.barannil.rentalsclient.services.RentalClient;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Title("Rentals")
public class MyUI extends UI {

    private HorizontalLayout horzLayout;
    
    private Grid<Customer> customerTable;       
    private Grid<Car> carTable;
    private Grid<Rental> rentalTable;
    
    private Customer myCustomer;
    private Car myCar;
    private Rental myRental;
    
    private FormUI formUI;
    
    public MyUI() {
        
        horzLayout = new HorizontalLayout();
        formUI  = new FormUI(this);

        for(int i=0; i<3; ++i)
          setLayout(i);

        updateCustomers();
        updateCars();
        updateRentals();
    }
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(horzLayout);    
    }

    private void setLayout(int i) {
        
        VerticalLayout vertLayout = new VerticalLayout();
        
        Grid grid = null;
        Button delete = new Button("Delete", VaadinIcons.TRASH);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);
        Button update = new Button("Edit", VaadinIcons.EDIT);
        update.addStyleName(ValoTheme.BUTTON_PRIMARY);
        Button addnew = new Button("Add", VaadinIcons.PLUS);
        addnew.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    
        HorizontalLayout action = new HorizontalLayout();
        action.addComponents(delete, update, addnew);
        
        switch(i){
            case 0:
                grid = customerTable = new Grid<>();
                customerTable.setCaption("Customers Management");
                customerTable.addColumn(Customer::getId).setCaption("ID");
                customerTable.addColumn(Customer::getFirstname).setCaption("Name");
                customerTable.addColumn(Customer::getLastname).setCaption("Surname");
                customerTable.addColumn(Customer::getAge).setCaption("Age");

                customerTable.asSingleSelect().addValueChangeListener(l -> {
                    myCustomer = l.getValue();
                });

                delete.addClickListener(listener->{
                    if(myCustomer != null) {
                        deleteCustomer(myCustomer);
                        updateCustomers();
                    }
                });

                update.addClickListener(listener->{
                    if(myCustomer != null)
                    setForm(formUI.editCustomer(myCustomer));    
                });

                 addnew.addClickListener(listener->{
                    setForm(formUI.addCustomer());    
                });
                 break;
          
            case 1:
                
                grid = carTable = new Grid<>();
                carTable.setCaption("Cars Management");
                carTable.addColumn(Car::getId).setCaption("ID");
                carTable.addColumn(Car::getBrand).setCaption("Brand");
                carTable.addColumn(Car::getModel).setCaption("Model");
                carTable.addColumn(Car::getPrice).setCaption("Price");
                carTable.addColumn(Car::getStatus).setCaption("Status");

                carTable.asSingleSelect().addValueChangeListener(l -> {
                    myCar = l.getValue();
                });
                
                delete.addClickListener(listener -> {
                    if (myCar != null) {
                        deleteCar(myCar);
                        updateCars();
                    }
                });

                update.addClickListener(listener -> {
                    if (myCar != null) {
                        setForm(formUI.editCar(myCar));
                    }
                });

                addnew.addClickListener(listener -> {
                    setForm(formUI.addCar());
                });
                break;

            case 2:
                
                grid = rentalTable = new Grid<>();
                rentalTable.setCaption("Rentals Management");
                rentalTable.addColumn(Rental::getId).setCaption("ID");
                rentalTable.addColumn(Rental::getCheckIn).setCaption("Check-In")/*.setRenderer(new LocalDateRenderer())*/;
                rentalTable.addColumn(Rental::getCheckOut).setCaption("Check-Out")/*.setRenderer(new LocalDateRenderer())*/;
                rentalTable.addColumn(Rental::getCost).setCaption("Cost");
                
                rentalTable.asSingleSelect().addValueChangeListener(l -> {
                    myRental = l.getValue();
                    updateCustomers();
                    updateCars();
                });

                delete.addClickListener(listener -> {
                    if (myRental != null) {
                        deleteRental(myRental);
                        updateRentals();
                    }
                });

                update.addClickListener(listener -> {
                    if (myRental != null) {
                        setForm(formUI.editRental(myRental));
                    }
                });

                addnew.addClickListener(listener -> {
                    if (myCustomer != null) {
                        if (myCar != null) {
                            setForm(formUI.addRental(myCustomer, myCar));
                        }
                    }
                });
                break;
        }

        vertLayout.addComponents(grid, action);
        horzLayout.addComponent(vertLayout);        
    }
    
    private void setForm(Window formLayout) {
        horzLayout.getUI().addWindow(formLayout);
    }

    private void deleteCustomer(Customer customer) {
        
        CustomerClient custClient = new CustomerClient();        
        custClient.remove(customer.getId().toString());
        
        myCustomer = null;
    }

    public final void updateCustomers() {
        
        CustomerClient customerClient = new CustomerClient();
        
        Response response = customerClient.findAll_XML(Response.class);
        List<Customer> customers = response.readEntity(new GenericType<List<Customer>>(){});
        
        customerTable.setItems(customers);
        
    }
    
    private void deleteCar(Car car) {
        
        CarClient carClient = new CarClient();               
        carClient.remove(car.getId().toString());
        
        myCar = null;        
    }

    public final void updateCars() {
        
        CarClient carClient = new CarClient();
        
        Response response = carClient.findAll_XML(Response.class);
        List<Car> cars = response.readEntity(new GenericType<List<Car>>(){});
        
        carTable.setItems(cars);
        
    }
    
    private void deleteRental(Rental rental) {
        
        RentalClient rentalClient = new RentalClient();
        rentalClient.remove(rental.getId().toString());
        myRental = null;
    }

    public final void updateRentals() {
        
        RentalClient rentalClient = new RentalClient();
        
        Response response = rentalClient.findAll_XML(Response.class);
        List<Rental> rentals = response.readEntity(new GenericType<List<Rental>>(){});
        
        rentalTable.setItems(rentals);   
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
