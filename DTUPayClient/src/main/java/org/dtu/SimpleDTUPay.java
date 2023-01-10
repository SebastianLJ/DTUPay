package org.dtu;

import aggregate.Name;
import dtu.ws.fastmoney.*;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

public class SimpleDTUPay {
    BankService bank = new BankServiceService().getBankServicePort();
    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080/");
    public boolean pay(String cid, String mid, int amount) throws CustomerDoesNotExist, MerchantDoesNotExist, PaymentAlreadyExists, BankServiceException_Exception {
        Payment payment = new Payment(mid, cid, amount);
        Response response = r.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
            if (response.getStatus() == 201) {
                bank.transferMoneyFromTo(cid,mid,new BigDecimal(amount),"DTUPay");
                return true;
            }
            else if (response.getStatus() == 400) {
                String message = response.readEntity(String.class);
                //if response contains "payment", payment already exists
                if (message.contains("payment")) {
                    throw new PaymentAlreadyExists(message);
                }

              //if response contains "customer" then throw CustomerDoesNotExist
                else if (message.contains("customer")) {
                    throw new CustomerDoesNotExist(message);
                }
                //if response contains "merchant" then throw MerchantDoesNotExist
                else if (message.contains("merchant")) {
                    throw new MerchantDoesNotExist(message);
                }
                //if response contains "BankServiceException" then throw BankServiceException
                else if (message.contains("BankServiceException")) {
                    throw new BankServiceException_Exception(message, new BankServiceException());
                }

            }
            return false;
        }



    public Payment getPayment(String id) throws PaymentDoesNotExist {
        try {
            return r.path("payment/" + id)
                    .request().accept(MediaType.APPLICATION_JSON)
                    .get(Payment.class);
        }
        catch (NotFoundException e) {
            throw new PaymentDoesNotExist();
        }
    }

    public List<Payment> getPayments() {
        return r.path("payments")
                .request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Payment>>() {});
    }

    public boolean createDTUPayCustomerAccount(String firstName, String lastName){
        Response response = r.path("/registration/customers").request().post(Entity.entity(new Name(firstName, lastName), MediaType.APPLICATION_JSON));
        return response.getStatus() == 201;
    }

    public boolean createDTUPayMerchantAccount(String firstName, String lastName){
        Response response = r.path("/registration/merchants").request().post(Entity.entity(new Name(firstName, lastName), MediaType.APPLICATION_JSON));
        return response.getStatus() == 201;
    }

    public String createBankAccountWithBalance(User user, BigDecimal balance) throws BankServiceException_Exception {
        String message =  bank.createAccountWithBalance(user, balance);
        return message;
    }


    public List<AccountInfo> getBankAccounts() {
        return bank.getAccounts();
    }

    public int getBalance(String id) throws BankServiceException_Exception {
        return bank.getAccount(id).getBalance().intValue();
    }


    public void retireBankAccount(String accountId) throws BankServiceException_Exception {
        bank.retireAccount(accountId);
    }
}
