/**
 * Customer klass hanterar kundens information, såsom för- och efternamn, konto osv.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.util.ArrayList;
import java.util.Objects;

public class Customer {
    private String firstName;
    private String lastName;
    final private String personalNumber;
    private ArrayList<Account> accounts = new ArrayList<>();

    public Customer(String personalNumber, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
    }

    public String checkAccountType(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.getAccountType();
        }
        return null;
    }

    /**
     * Skapar ett nytt konto för kunden
     * 
     * @param balance      - Saldo
     * @param interestRate - Ränta
     * @param accountType  - Sparkonto eller Kreditkonto
     * @return - Kontonummer
     */
    public int createNewAccount(double balance, double interestRate, String accountType) {
        if (accountType.equals("Sparkonto")) {
            SavingsAccount savingsAccount = new SavingsAccount(balance, interestRate, accountType);
            accounts.add(savingsAccount);
            return savingsAccount.getCustomerAccountNumber();
        } else if (accountType.equals("Kreditkonto")) {
            CreditAccount creditAccount = new CreditAccount(balance, interestRate, accountType, 5000, 7);
            accounts.add(creditAccount);
            return creditAccount.getCustomerAccountNumber();
        }
        return 0;
    }

    public ArrayList<String> getAccounts() {
        ArrayList<String> accountList = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getAccountIsActive())
                accountList.add(account.toString());
        }
        return accountList;
    }

    // getAccount() returns an string of the specific requested account
    public String getAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.toString();
        }
        return null;
    }

    public ArrayList<String> getCustomerAccounts() {
        ArrayList<String> customerAccounts = new ArrayList<>();
        for (Account account : accounts)
            if (account.getAccountIsActive()) {
                String finalResult = "";
                finalResult += account.getCustomerAccountNumber() + " ";
                finalResult += account.getAmountInLocalCurrency(account.getBalance()) + " ";
                finalResult += account.getAccountType() + " ";
                finalResult += account.getAmountInLocalCurrency(account.getBalance() * account.getInterestRate() / 100);
                customerAccounts.add(finalResult);
            }
        return customerAccounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public boolean depositIntoAccount(int accountNumber, int amount) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.deposit(amount);
        }
        return false;
    }

    public boolean withdrawFromAccount(int accountNumber, int amount) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.withdraw(amount);
        }
        return false;
    }

    public String removeAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber) {
                accounts.remove(account);
                return "Account removed";
            }
        }
        return "Account not found";
    }

    public ArrayList<SavingsAccount> getSavingsAccounts() {
        ArrayList<SavingsAccount> savingsAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getAccountType().equals("Savings"))
                savingsAccounts.add((SavingsAccount) account);
        }
        return savingsAccounts;
    }

    public ArrayList<CreditAccount> getCreditAccounts() {
        ArrayList<CreditAccount> creditAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getAccountType().equals("Credit"))
                creditAccounts.add((CreditAccount) account);
        }
        return creditAccounts;
    }

    public ArrayList<Account> getAccountsList() {
        return accounts;
    }

    public void setAccountsList(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public double getBalance(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.getBalance();
        }
        return 0;
    }

    public double getInterestRate(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.getInterestRate();
        }
        return 0;
    }

    public ArrayList<String> getTransactions(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.getTransactions();
        }
        return null;
    }

    public String closeAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return account.deactivateAccount();
        }
        return null;
    }

    @Override
    public String toString() {
        return this.personalNumber + " " + this.firstName + " " + this.lastName;
    }

}
