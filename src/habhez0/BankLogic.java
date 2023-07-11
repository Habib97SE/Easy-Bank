/**
 * BankLogic class hanterar bankens logik. T ex hanterar konton, kunder, insättningar och uttag.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.util.*;

public class BankLogic
{
    private ArrayList<Customer> allCustomers = new ArrayList<>();

    /**
     * Check if customer with given pNo already exists in our system.
     *
     * @param pNo String which is the personal number that should be checked.
     * @return If customer exists with given pNo returns true else returns false.
     */
    public boolean customerExists (String pNo)
    {
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                return true;
            }
        }
        return false;
    }

    private Customer findCustomerByPersonalNumber (String pNo)
    {
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
                return customer;
        }
        return null;
    }

    private Account findCustomerAccount (Customer customer, int accountNumber)
    {
        ArrayList<Account> accounts = new ArrayList<Account>();
        for (int i = 0; i < accounts.size(); i++)
        {
            if (accounts.get(i).getCustomerAccountNumber() == accountNumber)
                return accounts.get(i);
        }
        return null;
    }

    /**
     * This method will return all customers personal info (personalNumber firstName
     * lastName)
     * in an arrayList.
     *
     * @return : An arrayList of all customers personal info
     */
    public ArrayList<String> getAllCustomers ()
    {
        ArrayList<String> allCustomersStr = new ArrayList<String>();
        for (Customer allCustomer : allCustomers)
            allCustomersStr.add(allCustomer.toString());

        return allCustomersStr;
    }

    /**
     * Create a new customer with given info and add it to allCustomers arrayList,
     *
     * @param name    String : the first name of the customer.
     * @param surname String : The last name of the customer.
     * @param pNo     String : The personal number of the customer.
     * @return if customer is created successfully returns true otherwise returns
     * false.
     */
    public boolean createCustomer (String name, String surname, String pNo)
    {
        if (!customerExists(pNo))
        {
            Customer customer1 = new Customer(pNo, name, surname);
            allCustomers.add(customer1);
            return true;
        }
        return false;
    }

    /**
     * get a customer's info (personal details and accounts)
     *
     * @param pNo String : personal number of chosen customer
     * @return : an arraylist of all data we have for that specidic customer
     */
    public ArrayList<String> getCustomer (String pNo)
    {
        ArrayList<String> customerInfo = new ArrayList<String>();
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                customerInfo.add(customer.toString());
                customerInfo.addAll(customer.getAccounts());
            }
        }
        return customerInfo;
    }

    public boolean changeCustomerName (String name, String surname, String pNo)
    {
        // By default, result is false because if the name and surname are empty and if
        // pNo is not found then we return false
        boolean result = false;
        if (!customerExists(pNo))
            return result;
        for (Customer allCustomer : allCustomers)
        {
            // if a customer with given pNo found
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                // check if name is not empty
                if (!Objects.equals(name, ""))
                { // if not empty change the name and change the result var
                    allCustomer.setFirstName(name);
                    result = true;
                }
                if (!Objects.equals(surname, ""))
                {
                    allCustomer.setLastName(surname);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Create a new savings account for a customer with given pNo
     * @param pNo : personal number of the customer who wants to create a new savings account
     * @return: account number of the new savings account or -1 if the customer does not exist
     */
    public int createSavingsAccount (String pNo)
    {
        if (!customerExists(pNo))
            return -1;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.createNewAccount(0.0, 1.2, "Sparkonto");
        }
        return -1;
    }

    /**
     * Create a new credit account for a customer
     * @param pNo: personal number of the customer who wants to create a new credit account
     * @return: account number of the new credit account or -1 if the customer does not exist
     */
    public int createCreditAccount (String pNo)
    {
        if (!customerExists(pNo))
            return -1;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                int accountNumber = customer.createNewAccount(0.0, 0.5, "Kreditkonto");
                if (accountNumber != 0)
                {
                    return accountNumber;
                }
            }
        }
        return -1;
    }

    /**
     * Get all transactions for a specific account
     *
     * @param pNo       : personal number of the customer
     * @param accountId : account id of the account
     * @return : an arraylist of all transactions for that account
     */
    public ArrayList<String> getTransactions (String pNo, int accountId)
    {
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {

                ArrayList<String> transactions = new ArrayList<String>();
                transactions = customer.getTransactions(accountId);
                return transactions;
            }
        }
        return null;
    }

    public String getAccount (String pNo, int accountId)
    {
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                return customer.getAccount(accountId);
            }
        }
        return null;
    }

    public boolean deposit (String pNo, int accountId, int amount)
    {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.depositIntoAccount(accountId, amount);
        }
        return false;
    }

    public boolean withdraw (String pNo, int accountId, int amount)
    {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                return allCustomer.withdrawFromAccount(accountId, amount);
            }
        }
        return false;
    }

    public String closeAccount (String pNr, int accountId)
    {
        if (!customerExists(pNr))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNr))
            {
                return customer.closeAccount(accountId);
            }
        }
        return null;
    }

    public ArrayList<String> deleteCustomer (String pNo)
    {
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                ArrayList<String> customerInfo = new ArrayList<String>();
                customerInfo.add(customer.toString());
                customerInfo.addAll(customer.getCustomerAccounts());
                allCustomers.remove(customer);
                return customerInfo;
            }
        }
        return null;
    }

    boolean checkPersonalNumber (String pNo)
    {
        // check if the personl number is 10 or 12 digits
        if (pNo.length() != 10 && pNo.length() != 12)
            return false;
        // check if the personal number contains only digits
        for (int i = 0; i < pNo.length(); i++)
        {
            if (!Character.isDigit(pNo.charAt(i)))
                return false;
        }
        return true;
    }

    ArrayList<String> getCustomerAccounts (String pNo)
    {
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                return customer.getCustomerAccounts();
            }
        }
        return null;
    }

    public int extractAccountNumber (String accountInfo)
    {
        String[] accountInfoArray = accountInfo.split(" ");
        return Integer.parseInt(accountInfoArray[0]);
    }

    public String getFullName (String pNo)
    {
        String customerName = "";
        if (!customerExists(pNo))
            return null;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                customerName = customer.getFirstName() + " " + customer.getLastName();
            }
        }
        return customerName;
    }
}
