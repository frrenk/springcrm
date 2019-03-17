package pl.piaseckif.springrest.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.piaseckif.springrest.entity.Customer;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @Autowired
    private SessionFactory sf;


    @Override
    public List<Customer> getCustomers() {

        Session cs = sf.getCurrentSession();

        Query<Customer> query = cs.createQuery("from Customer order by lastName", Customer.class);

        List<Customer> customers = query.getResultList();

        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {

        Session cs = sf.getCurrentSession();

        cs.saveOrUpdate(customer);

    }

    @Override
    public Customer getCustomer(int id) {

        Session cs = sf.getCurrentSession();

        Customer customer = cs.get(Customer.class, id);

        return customer;
    }

    @Override
    public void deleteCustomer(int id) {

        Session cs = sf.getCurrentSession();

        cs.delete(cs.get(Customer.class, id));

    }
}
