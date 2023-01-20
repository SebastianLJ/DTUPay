package org.dtu;

import aggregate.Payment;
import aggregate.Token;
import aggregate.User;
import aggregate.UserId;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dtu.exceptions.PaymentDoesNotExist;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jákup Viljam Dam (s185095)
 */
public class ReportsSteps {

    private final BankService bankService = new BankServiceService().getBankServicePort();
    private final ReportsApp reportsApp = AppStart.getInstance().getReportsApp();
    private final CustomerApp customerApp = AppStart.getInstance().getCustomerApp();
    private final MerchantApp merchantApp = AppStart.getInstance().getMerchantApp();
    private final List<User> customers = new ArrayList<>();
    private final List<User> merchants = new ArrayList<>();
    private final Map<UserId, List<Token>> customerTokens = new HashMap<>();
    private final List<Payment> allPayments = new ArrayList<>();
    private List<Payment> fetchedReports = new ArrayList<>();
    private List<Payment> fetchedReportsFromCustomer = new ArrayList<>();
    private List<Payment> fetchedReportsFromMerchant = new ArrayList<>();

    @After
    public void cleanup()  {
        try {
            deleteBankUser(merchants);
            deleteBankUser(customers);
        } catch (BankServiceException_Exception ignored) { }
    }

    private void deleteBankUser(List<User> userList) throws BankServiceException_Exception {
        for (User user : userList) {
            bankService.retireAccount(user.getBankNumber());
        }
    }
    private void createBankUser(String firstName, String lastName, List<User> userList, boolean isMerchant) throws Exception {
        String cprNumber = UUID.randomUUID().toString();

        User dtuPayUser;
        dtu.ws.fastmoney.User bankUser = new dtu.ws.fastmoney.User();
        bankUser.setFirstName(firstName);
        bankUser.setLastName(lastName);
        bankUser.setCprNumber(cprNumber);
        String bankNumber = bankService.createAccountWithBalance(bankUser, BigDecimal.valueOf(2000));

        if (isMerchant) {
            dtuPayUser = merchantApp.register(firstName, lastName, bankNumber);
        } else {
            dtuPayUser = customerApp.register(firstName, lastName, bankNumber);
            List<Token> tokens = customerApp.generateTokens(dtuPayUser.getUserId(), 5);
            customerTokens.put(dtuPayUser.getUserId(), tokens);
        }
        dtuPayUser.setBankNumber(bankNumber);
        userList.add(dtuPayUser);
    }

    @Given("There are {int} customers and merchants in the system")
    public void thereAreCustomersAndMerchantsInTheSystem(int amount) throws Exception {
        System.out.println("ReportsServiceSteps");
        for (int i = 0; i < amount; i++) {
            createBankUser("Jane", "Doe", merchants, true);
            createBankUser("John", "Does", customers, false);
        }
    }

    @And("{int} payments have been done by {int} separate customers")
    public void paymentsHaveBeenDoneBySeparateCustomers(int payments, int amount) throws Exception {
        for (int i = 0; i < payments; i++) {
            for (int j = 0; j < amount; j++) {
                User merchant = merchants.get(i);
                User customer = customers.get(j);
                Payment payment = merchantApp.pay(merchant.getUserId(), customerTokens.get(customer.getUserId()).get(0), 100);
                customerTokens.get(customer.getUserId()).remove(0);
                allPayments.add(payment);
            }
        }
    }

    @When("Fetching all reports, it returns a list of payments for all customers and merchants")
    public void fetchingAllReportsItReturnsAListOfPaymentsForAllCustomersAndMerchants() throws PaymentDoesNotExist {
        fetchedReports = reportsApp.getAllReports();
    }

    @Then("The payments made beforehand should match the fetched payments")
    public void thePaymentsMadeBeforehandShouldMatchTheFetchedPayments() {
        assertTrue(allPayments.size() <= fetchedReports.size());
        int counter = 0;
        for (Payment fetchedReport : fetchedReports) {
            for (Payment allPayment : allPayments) {
                if (allPayment.getId().equals(fetchedReport.getId())) {
                    assertEquals(allPayment.getMid(), fetchedReport.getMid());
                    assertEquals(allPayment.getAmount(), fetchedReport.getAmount());
                    assertEquals(allPayment.getToken().getId(), fetchedReport.getToken().getId());
                    counter++;
                }
            }
        }
        assertEquals(allPayments.size(), counter);
    }

    @When("Fetching all reports from {int} customer")
    public void fetchingAllReportsFromCustomer(int amount) throws PaymentDoesNotExist {
        fetchedReportsFromCustomer = reportsApp.getCustomerReport(customers.get(0));
    }

    @Then("A list of payments for that customer are returned and contained within all DTPPay reports")
    public void aListOfPaymentsForThatCustomerAreReturnedAndContainedWithinAllDTPPayReports() {
        assertTrue(fetchedReportsFromCustomer.size() <= fetchedReports.size());
        int counter = 0;
        for (Payment fetchedReport : fetchedReports) {
            for (Payment allPayment : fetchedReportsFromCustomer) {
                if (allPayment.getId().equals(fetchedReport.getId())) {
                    assertEquals(allPayment.getMid(), fetchedReport.getMid());
                    assertEquals(allPayment.getAmount(), fetchedReport.getAmount());
                    assertEquals(allPayment.getToken().getId(), fetchedReport.getToken().getId());
                    counter++;
                }
            }
        }
        assertEquals(fetchedReportsFromCustomer.size(), counter);
    }

    @When("Fetching all reports from {int} merchant")
    public void fetchingAllReportsFromMerchant(int amount) throws PaymentDoesNotExist {
        fetchedReportsFromMerchant = reportsApp.getMerchantReport(merchants.get(0));
    }

    @Then("A list of payments for that merchant are returned")
    public void aListOfPaymentsForThatMerchantAreReturned() {
        assertTrue(fetchedReportsFromMerchant.size() <= fetchedReports.size());
        int counter = 0;
        for (Payment fetchedReport : fetchedReports) {
            for (Payment allPayment : fetchedReportsFromMerchant) {
                if (allPayment.getId().equals(fetchedReport.getId())) {
                    assertEquals(allPayment.getMid(), fetchedReport.getMid());
                    assertEquals(allPayment.getAmount(), fetchedReport.getAmount());
                    assertEquals(allPayment.getToken().getId(), fetchedReport.getToken().getId());
                    counter++;
                }
            }
        }
        assertEquals(fetchedReportsFromMerchant.size(), counter);
    }
}
