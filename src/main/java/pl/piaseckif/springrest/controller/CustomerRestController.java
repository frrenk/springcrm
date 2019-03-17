package pl.piaseckif.springrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.piaseckif.springrest.entity.Customer;
import pl.piaseckif.springrest.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    //autowire the customerService
    @Autowired
    private CustomerService customerService;

    //add mapping for get /customers

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId) {

        Customer customer = customerService.getCustomer(customerId);

        if (customer==null) {
            throw new CustomerNotFoundException("Customer id not found: "+customerId);
        }

        return customer;
    }


    //add mapping for POST /customers
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {


        //set id to 0 to force a save of a new item
        customer.setId(0);
        customerService.saveCustomer(customer);

        return customer;
    }

    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return customer;
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {

        Customer customer = customerService.getCustomer(customerId);

        if (customer==null) {
            throw new CustomerNotFoundException("Customer Id not found: "+customerId);
        }

        customerService.deleteCustomer(customerId);
        return "Deleted customer id: "+customerId;
    }


}
