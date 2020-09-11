/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fit.barannil.rentalsclient;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import cz.fit.barannil.rentalsclient.dto.Car;
import cz.fit.barannil.rentalsclient.dto.Customer;
import cz.fit.barannil.rentalsclient.dto.Rental;
import cz.fit.barannil.rentalsclient.services.CarClient;
import cz.fit.barannil.rentalsclient.services.CustomerClient;
import cz.fit.barannil.rentalsclient.services.RentalClient;
import java.time.ZoneId;


/**
 *
 * @author Nilay
 */
public class FormUI {
    
    private MyUI myUI; 
    private Window myWindow;
    
    FormUI(MyUI aThis) {
      myUI = aThis;
      myWindow = new Window();
      myWindow.setModal(true);
    }
    
    Window addCustomer() {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        TextField name = new TextField("Name");
        TextField surname = new TextField("Surname");
        TextField age = new TextField("Age");
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button addnew = new Button("Add Customer",e->{
              CustomerClient customerClient = new CustomerClient();
              Customer cust = new Customer();
              cust.setFirstname(name.getValue());
              cust.setLastname(surname.getValue());
              cust.setAge(Integer.parseInt(age.getValue()));
              customerClient.create_XML(cust);
              myUI.updateCustomers();
              myWindow.close();
          });
        addnew.addStyleName(ValoTheme.BUTTON_FRIENDLY);
      
        HorizontalLayout action = new HorizontalLayout();
        action.addComponents(cancel,addnew);
        formLayout.addComponents(id,name,surname,age,action);
        
        myWindow.setCaption("Add New Customer");
        myWindow.setContent(formLayout);
        return myWindow;
    }
    
    Window editCustomer(Customer myCustomer) {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        TextField name = new TextField("Name");
        TextField surname = new TextField("Surname");
        TextField age = new TextField("Age");
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button update = new Button("Update",e->{
            CustomerClient customerClient = new CustomerClient();
            myCustomer.setFirstname(name.getValue());
            myCustomer.setLastname(surname.getValue());
            myCustomer.setAge(Integer.parseInt(age.getValue()));
            customerClient.edit_XML(myCustomer,myCustomer.getId().toString());
            myUI.updateCustomers();
            myWindow.close();
        });
        update.addStyleName(ValoTheme.BUTTON_PRIMARY);
          
          id.setValue("ID: " + myCustomer.getId().toString());
          name.setValue(myCustomer.getFirstname());
          surname.setValue(myCustomer.getLastname());
          age.setValue(myCustomer.getAge().toString());
      
          HorizontalLayout buttons = new HorizontalLayout();
          buttons.addComponents(cancel,update);
          formLayout.addComponents(id,name,surname,age,buttons);
        
        myWindow.setCaption("Update Customer: " + myCustomer.getFirstname());
        myWindow.setContent(formLayout);
        return myWindow;
    }
    
    Window addCar() {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        TextField brand = new TextField("Brand");
        TextField model = new TextField("Model");
        TextField price = new TextField("Price");
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button addnew = new Button("Add Car",e->{
              CarClient carClient = new CarClient();
              Car car = new Car();
              car.setBrand(brand.getValue());
              car.setModel(model.getValue());
              car.setPrice(Integer.parseInt(price.getValue()));
              car.setStatus("Available");
              carClient.create_XML(car);
              
              myUI.updateCars();
              myWindow.close();
          });
        addnew.addStyleName(ValoTheme.BUTTON_FRIENDLY);
      
        HorizontalLayout action = new HorizontalLayout();
        action.addComponents(cancel,addnew);
        formLayout.addComponents(id,brand,model,price,action);
        
        myWindow.setCaption("Add New Car");
        myWindow.setContent(formLayout);
        return myWindow;
    }
    
    Window editCar(Car myCar) {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        TextField brand = new TextField("Brand");
        TextField model = new TextField("Model");
        TextField price = new TextField("Price");
        TextField status = new TextField("Status");
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button update = new Button("Update",e->{
            CarClient carClient = new CarClient();
            myCar.setBrand(brand.getValue());
            myCar.setModel(model.getValue());
            myCar.setPrice(Integer.parseInt(price.getValue()));
            myCar.setStatus(status.getValue());
            carClient.edit_XML(myCar,myCar.getId().toString());
            myUI.updateCars();
            myWindow.close();
        });
        update.addStyleName(ValoTheme.BUTTON_PRIMARY);
          
          id.setValue("ID: " + myCar.getId().toString());
          brand.setValue(myCar.getBrand());
          model.setValue(myCar.getModel());
          price.setValue(myCar.getPrice().toString());
          status.setValue(myCar.getStatus());
      
          HorizontalLayout buttons = new HorizontalLayout();
          buttons.addComponents(cancel,update);
          formLayout.addComponents(id,brand,model,price,status,buttons);
        
        myWindow.setCaption("Update Car: " + myCar.getId().toString());
        myWindow.setContent(formLayout);
        return myWindow;
    }
   
    Window addRental(Customer myCustomer, Car myCar) {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        DateField checkIn = new DateField("Check-In");
        DateField checkOut = new DateField("Check-Out");
        //TextField cost = new TextField("Cost");
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button addnew = new Button("Add Rental",e->{
              CustomerClient customerClient = new CustomerClient();
              CarClient carClient = new CarClient();
              RentalClient rentalClient = new RentalClient();
              
              Rental rental = new Rental();
              rental.setCheckIn(java.sql.Date.valueOf(checkIn.getValue()));
              rental.setCheckOut(java.sql.Date.valueOf(checkOut.getValue())); 
              //rental.setCost(Integer.parseInt(cost.getValue()));
              rental.setCost(checkIn.getValue().until(checkOut.getValue()).getDays() * myCar.getPrice());
              rental.setCustomer(myCustomer);
              rental.setCar(myCar);
              rentalClient.create_XML(rental);

              myCar.setStatus("Busy");
              carClient.edit_XML(myCar, myCar.getId().toString());
              
              myUI.updateRentals();
              myUI.updateCars();
              myUI.updateCustomers();
              myWindow.close();
          });
        addnew.addStyleName(ValoTheme.BUTTON_FRIENDLY);
      
        HorizontalLayout action = new HorizontalLayout();
        action.addComponents(cancel,addnew);
        //formLayout.addComponents(id,checkIn,checkOut,cost,action);
        formLayout.addComponents(id,checkIn,checkOut,action);

        myWindow.setCaption("Add New Rental");
        myWindow.setContent(formLayout);
        return myWindow;
    }
    
    Window editRental(Rental myRental) {
        
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        
        Label id = new Label("ID");
        DateField checkIn = new DateField("Check-In");
        DateField checkOut = new DateField("Check-Out");
        TextField cost = new TextField("Cost");
        TextField customer = new TextField("Customer");
        customer.setEnabled(false);
        TextField car = new TextField("Car");
        car.setEnabled(false);
        Button cancel = new Button("Cancel",e->myWindow.close());
        Button update = new Button("Update",e->{
            RentalClient rentalClient = new RentalClient();
            myRental.setCheckIn(java.sql.Date.valueOf(checkIn.getValue()));
            myRental.setCheckOut(java.sql.Date.valueOf(checkOut.getValue()));
            myRental.setCost(Integer.parseInt(cost.getValue()));            
            rentalClient.edit_XML(myRental,myRental.getId().toString());
            myUI.updateRentals();
            myWindow.close();
        });
        update.addStyleName(ValoTheme.BUTTON_PRIMARY);
          
          id.setValue("ID: " + myRental.getId().toString());
          checkIn.setValue(myRental.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
          checkOut.setValue(myRental.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
          cost.setValue(myRental.getCost().toString());
          customer.setValue(myRental.getCustomer().getFirstname() + " " + myRental.getCustomer().getLastname());
          car.setValue(myRental.getCar().getBrand() + " " + myRental.getCar().getModel());
      
          HorizontalLayout buttons = new HorizontalLayout();
          buttons.addComponents(cancel,update);
          formLayout.addComponents(id,customer,car,checkIn,checkOut,cost,buttons);
        
        myWindow.setCaption("Update Rental: " + myRental.getId().toString());
        myWindow.setContent(formLayout);
        return myWindow;
    }
    
}
