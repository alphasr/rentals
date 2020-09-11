package cz.fit.barannil.rentalsserver;

import cz.fit.barannil.rentalsserver.Car;
import cz.fit.barannil.rentalsserver.Customer;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-01-18T18:05:48")
@StaticMetamodel(Rental.class)
public class Rental_ { 

    public static volatile SingularAttribute<Rental, Date> checkIn;
    public static volatile SingularAttribute<Rental, Integer> cost;
    public static volatile SingularAttribute<Rental, Car> car;
    public static volatile SingularAttribute<Rental, Long> id;
    public static volatile SingularAttribute<Rental, Date> checkOut;
    public static volatile SingularAttribute<Rental, Customer> customer;

}