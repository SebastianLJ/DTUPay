package org.example;

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
    public boolean pay(String cid, String mid, int amount) throws CustomerDoesNotExist, MerchantDoesNotExist, BankServiceException_Exception {
        bank.transferMoneyFromTo(cid,mid,new BigDecimal(amount),"DTUPay");
        Payment payment = new Payment(mid, cid, amount);
        Response response = r.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
            if (response.getStatus() == 201) {
                return true;
            }
            else if (response.getStatus() == 400) {
                String message = response.readEntity(String.class);
                if (message.contains("customer")) {
                    throw new CustomerDoesNotExist(message);
                }
                else if (message.contains("merchant")) {
                    throw new MerchantDoesNotExist(message);
                }
            }
            return false;
        }

        //cast response to payment
        //if response is successful return true


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

    public boolean createDTUPayCustomerAccount(String id){
        Response response = r.path("/registration/customers").request().post(Entity.entity(id, MediaType.APPLICATION_JSON));
        return response.getStatus() == 201;
    }

    public boolean createDTUPayMerchantAccount(String id){
        Response response = r.path("/registration/merchants").request().post(Entity.entity(id, MediaType.APPLICATION_JSON));
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
