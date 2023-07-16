import habhez0.BankLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GUI
{
    private final String FONT_NAME = "Arial";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private String personlNumber = "";
    private boolean loggedIn = false;
    private BankLogic bankLogic;
    private JFrame mainFrame;
    private JPanel headerPanel = new JPanel();
    private JPanel sidebarPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private JPanel footerPanel = new JPanel();
    private JMenuBar menuBar;
    private String dateFormat;

    public GUI (String programName, String dateFormat, BankLogic bankLogic)
    {
        this.dateFormat = dateFormat;
        this.bankLogic = bankLogic;

        initializeFrame(programName);
        initializePanels();
        configureUI();
        createMainView();
        showMainFrame();
    }

    private void initializeFrame (String programName)
    {
        mainFrame = new JFrame(programName);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("icon.jpg"))).getImage());
        mainFrame.setJMenuBar(createMenuBar());
    }

    private void initializePanels ()
    {
        headerPanel = createPanel(Color.WHITE, new Dimension(FRAME_WIDTH, 100));
        sidebarPanel = createPanel(Color.WHITE, new Dimension(200, FRAME_HEIGHT - 100));
        contentPanel = createPanel(Color.WHITE, new Dimension(FRAME_WIDTH - 200, FRAME_HEIGHT - 100));
        footerPanel = createPanel(Color.WHITE, new Dimension(FRAME_WIDTH, 100));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    private void configureUI ()
    {
        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(sidebarPanel, BorderLayout.WEST);
        mainFrame.add(contentPanel, BorderLayout.CENTER);
        mainFrame.add(footerPanel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JPanel createPanel (Color background, Dimension dimension)
    {
        JPanel panel = new JPanel();
        panel.setBackground(background);
        panel.setPreferredSize(dimension);
        return panel;
    }

    private void showMainFrame ()
    {
        // Code for showing the main frame goes here
        mainFrame.setVisible(true);
    }

    public void createMainView ()
    {
        mainFrame.setJMenuBar(createMenuBar());
        cleanPanels();
        createHeader("Välkommen", "Vänligen sätt en kund eller skapa en ny");
        createSidebar();
        createFooter();
        mainFrame.pack();
    }

    public void cleanPanels ()
    {
        // remove all elements from panels
        headerPanel.removeAll();
        sidebarPanel.removeAll();
        contentPanel.removeAll();
        footerPanel.removeAll();
        headerPanel.revalidate();
        sidebarPanel.revalidate();
        contentPanel.revalidate();
        footerPanel.revalidate();
        headerPanel.repaint();
        sidebarPanel.repaint();
        contentPanel.repaint();
        footerPanel.repaint();
    }


    public JMenuBar createMenuBar ()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createCustomerMenu());
        menuBar.add(createAccountMenu());
        JMenuItem exit = new JMenuItem("Avsluta");
        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                System.exit(0);
            }
        });
        menuBar.add(exit);
        return menuBar;
    }

    private JMenuItem createMenuItem (String name, ActionListener actionListener)
    {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setUI(new javax.swing.plaf.basic.BasicMenuItemUI()
        {
            @Override
            protected void installDefaults ()
            {
                super.installDefaults();
                selectionBackground = Color.red;
                selectionForeground = Color.white;
            }

        });
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    public JMenu createCustomerMenu ()
    {
        JMenu menu = new JMenu("Kund");
        JMenuItem newCustomer = createMenuItem("Ny kund", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleNewCustomer();
            }
        });
        JMenuItem editCustomer = createMenuItem("Ändra kund", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleEditCustomer();
            }
        });
        JMenuItem deleteCustomer = createMenuItem("Ta bort kund", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleDeleteCustomer();
            }
        });
        JMenuItem showCustomer = createMenuItem("Visa kund", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleShowCustomer();
            }
        });
        JMenuItem setCustomer = null;
        if (loggedIn)
        {
            setCustomer = createMenuItem("Nollställ kund", new ActionListener()
            {
                @Override
                public void actionPerformed (ActionEvent e)
                {
                    handleRemoveCustomer();
                }
            });
        } else
        {
            setCustomer = createMenuItem("Sätt kund", new ActionListener()
            {
                @Override
                public void actionPerformed (ActionEvent e)
                {
                    handleSetCustomer();
                }
            });
        }
        menu.add(setCustomer);
        menu.add(new JSeparator());
        menu.add(newCustomer);
        menu.add(editCustomer);
        menu.add(deleteCustomer);
        menu.add(showCustomer);
        return menu;
    }

    private void handleRemoveCustomer ()
    {

        if (loggedIn)
        {
            // re-render the menu bar
            personlNumber = "";
            loggedIn = false;
            createMainView();

        }
    }

    private void handleSetCustomer ()
    {
        if (!loggedIn)
        {
            JLabel personalNumberLabel = new JLabel("Personnummer: ");
            JTextField personalNumberField = new JTextField(20);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(personalNumberLabel, BorderLayout.WEST);
            panel.add(personalNumberField, BorderLayout.CENTER);
            JButton setCustomer = new JButton("Sätt kund");
            setCustomer.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed (ActionEvent e)
                {
                    String personalNumber = personalNumberField.getText();
                    if (bankLogic.customerExists(personalNumber))
                    {
                        setPersonlNumber(personalNumber);
                        loggedIn = true;
                        handleShowCustomer();
                        mainFrame.setJMenuBar(createMenuBar());
                    } else
                    {
                        JOptionPane.showMessageDialog(mainFrame, "Kunden finns inte");
                    }
                }
            });
            cleanPanels();
            createHeader("Sätt kund", "Du kan sätta kund här");
            createSidebar();
            contentPanel.add(panel, BorderLayout.NORTH);
            contentPanel.add(setCustomer, BorderLayout.SOUTH);
            mainFrame.pack();
        }
    }

    private void handleShowCustomer ()
    {
        if (!checkIfCustomerIsSet())
        {
            return;
        }

        cleanPanels();
        createHeader("Visa kund", "Du kan visa kundens information här");
        createSidebar();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 10, 20, 0);

        // Create labels and add them to the form panel
        addCustomerInfoLabel(formPanel, "Personnummer:", getPersonlNumber(), gridBagConstraints, 0);
        addCustomerInfoLabel(formPanel, "Förnamn:", bankLogic.getFullName(personlNumber).split(" ")[0], gridBagConstraints, 1);
        addCustomerInfoLabel(formPanel, "Efternamn:", bankLogic.getFullName(personlNumber).split(" ")[1], gridBagConstraints, 2);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    private void addCustomerInfoLabel (JPanel panel, String labelText, String value, GridBagConstraints constraints, int gridY)
    {
        JLabel label = new JLabel(labelText + " " + value);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 0));
        constraints.gridy = gridY;
        panel.add(label, constraints);
    }


    private void handleDeleteCustomer ()
    {
        if (personlNumber.equals(""))
        {
            JOptionPane.showMessageDialog(mainFrame, "Du måste välja en kund först");
            return;
        }
        int result = JOptionPane.showConfirmDialog(mainFrame, "Är du säker på att du vill ta bort kunden?");
        if (result == JOptionPane.YES_OPTION)
        {
            bankLogic.deleteCustomer(personlNumber);
            JOptionPane.showMessageDialog(mainFrame, "Kunden har tagits bort");
            cleanPanels();
            createHeader("Välkommen", "This is a test");
            createSidebar();
            loggedIn = false;
            setPersonlNumber("");
            mainFrame.pack();
        }
    }

    private void handleEditCustomer ()
    {
        if (!checkIfCustomerIsSet())
            return;
        cleanPanels();
        createHeader("Ändra kund", "Du kan ändra kundens namn här");
        createSidebar();
        JPanel formPanel = new JPanel(new BorderLayout());
        JLabel personalNumberLabel = new JLabel("Personnummer:");
        JLabel firstNameLabel = new JLabel("Förnamn:");
        JLabel lastNameLabel = new JLabel("Efternamn:");
        JTextField personalNumberTextfield = new JTextField(20);
        personalNumberTextfield.setText(personlNumber);
        personalNumberLabel.setEnabled(false);
        JTextField firstNameTextfield = new JTextField(20);
        JTextField lastNameTextfield = new JTextField(20);
        JPanel personalNumberPanel = new JPanel();
        personalNumberPanel.add(personalNumberLabel);
        personalNumberPanel.add(personalNumberTextfield);
        personalNumberPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel firstNamePanel = new JPanel();
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameTextfield);
        firstNamePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel lastNamePanel = new JPanel();
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameTextfield);
        lastNamePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton cancelButton = new JButton("Avbryt");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleCancel();
            }
        });

        JButton submitButton = new JButton("Spara");
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                String firstName = firstNameTextfield.getText();
                String lastName = lastNameTextfield.getText();
                boolean result = bankLogic.changeCustomerName(firstName, lastName, personalNumberTextfield.getText());
                if (result)
                {
                    JOptionPane.showMessageDialog(mainFrame, "Kundens namn har ändrats", "Ändra kund", JOptionPane.INFORMATION_MESSAGE);
                    handleCancel();
                } else
                {
                    JOptionPane.showMessageDialog(mainFrame, "Kundens namn kunde inte ändras", "Ändra kund", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        GridBagLayout gridBagLayout = new GridBagLayout();
        formPanel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        formPanel.add(personalNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        formPanel.add(personalNumberTextfield, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        formPanel.add(firstNameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        formPanel.add(firstNameTextfield, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        formPanel.add(lastNameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        formPanel.add(lastNameTextfield, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        formPanel.add(buttonPanel, gridBagConstraints);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        // add the form and submit button to the mainframe
        mainFrame.pack();
    }

    private void handleCancel ()
    {
        createMainView();
    }

    private String getInputPopup (String message)
    {
        String input;
        try
        {
            input = JOptionPane.showInputDialog(message);
            if (input == null || input.isEmpty())
                return input;
        } catch (Exception e)
        {
            return getInputPopup(message);
        }
        return input;
    }

    public void setPersonlNumber (String personlNumber)
    {
        this.personlNumber = personlNumber;
    }

    public String getPersonlNumber ()
    {
        return personlNumber;
    }

    private boolean isCustomerSet ()
    {
        return personlNumber != null || personlNumber.equals("");
    }

    private void createLoggedInSidebar ()
    {
        sidebarPanel.removeAll();
        JLabel sideBarTitle = new JLabel("Funktioner:");
        sideBarTitle.setHorizontalAlignment(JLabel.CENTER);
        sideBarTitle.setPreferredSize(new Dimension(sidebarPanel.getWidth(), 50));
        sidebarPanel.add(sideBarTitle);
        JButton withdrawButton = new JButton("Ta ut pengar");
        JButton depositButton = new JButton("Sätt in pengar");
        JButton showBalanceButton = new JButton("Visa saldo");
        JButton showTransactionsButton = new JButton("Visa transaktioner");
        JButton transferButton = new JButton("Överföring");
        withdrawButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleWithdraw();
            }
        });

        depositButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleDeposit();
            }
        });

        showBalanceButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleShowBalance();
            }
        });
        showTransactionsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleShowTransactions();
            }
        });
        sidebarPanel.add(withdrawButton);
        sidebarPanel.add(depositButton);
        sidebarPanel.add(showBalanceButton);
        sidebarPanel.add(showTransactionsButton);
        sidebarPanel.add(transferButton);
        Dimension buttonDimension = new Dimension(sidebarPanel.getWidth() - 15, 50);
        withdrawButton.setPreferredSize(buttonDimension);
        depositButton.setPreferredSize(buttonDimension);
        showBalanceButton.setPreferredSize(buttonDimension);
        showTransactionsButton.setPreferredSize(buttonDimension);
        transferButton.setPreferredSize(buttonDimension);
        mainFrame.add(sidebarPanel, BorderLayout.WEST);
        mainFrame.pack();
    }

    private void handleShowTransactions ()
    {
        if (checkIfCustomerIsSet())
            return;
        cleanPanels();
        createHeader("Visa transaktioner", "Visa transaktioner för ett konto");
        createSidebar();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        JLabel selectAccount = new JLabel("Välj konto:");
        JComboBox accountList = new JComboBox();
        ArrayList<String> accounts = bankLogic.getCustomerAccounts(personlNumber);
        for (String account : accounts)
        {
            accountList.addItem(account);
        }
        JButton submitButton = new JButton("Visa transaktioner");
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                String accountNumber = (String) accountList.getSelectedItem();
                ArrayList<String> transactions = bankLogic.getTransactions(getPersonlNumber(),
                        Integer.parseInt(accountNumber));
                if (transactions == null)
                {
                    JOptionPane.showMessageDialog(mainFrame, "Kunde inte hitta några transaktioner");
                    return;
                }
                contentPanel.removeAll();
                // show transactions in a table
                String[] columnNames = {"Datum", "Belopp", "Saldo"};
                String[][] data = new String[transactions.size()][4];
                for (int i = 0; i < transactions.size(); i++)
                {
                    // transaction pattern: date amount Saldo: balance
                    String transaction = transactions.get(i);
                    data[i][0] = transaction.split(" ")[0];
                    data[i][1] = transaction.split(" ")[1];
                    // remove saldo: from the string and return the last element after split(" ")
                    data[i][2] = transaction.substring(transaction.indexOf("Saldo:") + 6).split(" ")[0];
                }
                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                table.setEnabled(false);
                formPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.add(formPanel, BorderLayout.CENTER);
                mainFrame.pack();
            }
        });
        formPanel.add(selectAccount, BorderLayout.NORTH);
        formPanel.add(accountList, BorderLayout.CENTER);
        formPanel.add(submitButton, BorderLayout.SOUTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }


    private void createSidebar ()
    {
        if (loggedIn)
            createLoggedInSidebar();
        else
            createMainViewSidebar();
    }

    private void handleShowBalance ()
    {
        if (checkIfCustomerIsSet())
            return;
        cleanPanels();
        createHeader("Visa saldo", "Visa saldo för ett konto");
        createSidebar();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 1));
        JPanel accountNumberPanel = new JPanel();
        JPanel balancePanel = new JPanel();
        JLabel accountNumberLabel = new JLabel("Kontonummer:");
        JLabel balanceLabel = new JLabel("Saldo:");
        JTextField accountNumberTextfield = new JTextField();
        JTextField balanceTextfield = new JTextField();
        accountNumberTextfield.setEditable(false);
        balanceTextfield.setEditable(false);
        accountNumberPanel.add(accountNumberLabel);
        accountNumberPanel.add(accountNumberTextfield);
        balancePanel.add(balanceLabel);
        balancePanel.add(balanceTextfield);
        formPanel.add(accountNumberPanel);
        formPanel.add(balancePanel);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    public int selectAccountNumber ()
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Välj ett konto från listan:");
        JComboBox comboBox = new JComboBox();
        ArrayList<String> accounts = bankLogic.getCustomerAccounts(personlNumber);
        for (String account : accounts)
        {
            comboBox.addItem(bankLogic.extractAccountNumber(account));
        }
        panel.add(label);
        panel.add(comboBox);
        String[] options = new String[]{"OK", "Avbryt"};
        int option = JOptionPane.showOptionDialog(null, panel, "Välj konto",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[1]);
        if (option == 0)
        {
            return Integer.parseInt(comboBox.getSelectedItem().toString());
        }
        return -1;
    }

    private void handleDeposit ()
    {
        if (!checkIfCustomerIsSet())
            return;
        cleanPanels();
        createHeader("Sätt in pengar", "Fyll i uppgifterna nedan");
        createSidebar();
        JPanel formPanel = new JPanel();
        JLabel selectAccount = new JLabel("Välj ett konto från lista: ");
        JComboBox accountList = new JComboBox();
        ArrayList<String> accounts = bankLogic.getCustomerAccounts(personlNumber);
        for (String account : accounts)
        {
            accountList.addItem(bankLogic.extractAccountNumber(account));
        }
        formPanel.add(selectAccount);
        formPanel.add(accountList);
        JButton submitButton = new JButton("Välj konto");
        formPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                int accountNumber = Integer.parseInt(accountList.getSelectedItem().toString());
                contentPanel.removeAll();
                formPanel.removeAll();
                JLabel amountLabel = new JLabel("Belopp:");
                JTextField amountTextfield = new JTextField(20);
                formPanel.add(amountLabel);
                formPanel.add(amountTextfield);
                JButton submitButton = new JButton("Sätt in pengar");
                formPanel.add(submitButton);
                submitButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed (ActionEvent e)
                    {
                        try
                        {
                            int amount = Integer.parseInt(amountTextfield.getText());
                            bankLogic.deposit(getPersonlNumber(), accountNumber, amount);
                            JOptionPane.showMessageDialog(null, String.format("Du satte in %d på ditt konto %s",
                                            amount, accountNumber), "Pengar insatta",
                                    JOptionPane.INFORMATION_MESSAGE);
                            handleCancel();
                        } catch (Exception ex)
                        {
                            JOptionPane.showMessageDialog(null, "Felaktigt belopp");
                            return;
                        }
                    }
                });
                contentPanel.add(formPanel);
                mainFrame.pack();
            }
        });
        contentPanel.add(formPanel);
        mainFrame.pack();
    }

    private void handleWithdraw ()
    {
        if (!checkIfCustomerIsSet())
        {
            return;
        }

        cleanPanels();
        createHeader("Ta ut pengar", "Fyll i uppgifterna nedan");
        createSidebar();
        JPanel formPanel = createWithdrawFormPanel();

        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    private JPanel createWithdrawFormPanel ()
    {
        JPanel formPanel = new JPanel(new BorderLayout());

        JPanel selectAccountPanel = createSelectAccountPanel();
        JPanel amountPanel = createAmountPanel();
        JPanel buttonPanel = createWithdrawButtonPanel();

        formPanel.add(selectAccountPanel, BorderLayout.NORTH);
        formPanel.add(amountPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private JPanel createSelectAccountPanel ()
    {
        JLabel selectAccountLabel = new JLabel("Välj ett konto från listan: ");
        JComboBox<Integer> accountListComboBox = createAccountListComboBox();
        JPanel selectAccountPanel = new JPanel();
        selectAccountPanel.add(selectAccountLabel);
        selectAccountPanel.add(accountListComboBox);

        return selectAccountPanel;
    }

    private JComboBox<Integer> createAccountListComboBox ()
    {
        JComboBox<Integer> accountListComboBox = new JComboBox<>();
        ArrayList<String> accounts = bankLogic.getCustomerAccounts(personlNumber);
        for (String account : accounts)
        {
            int accountNumber = bankLogic.extractAccountNumber(account);
            accountListComboBox.addItem(accountNumber);
        }

        return accountListComboBox;
    }

    private JPanel createAmountPanel ()
    {
        JLabel amountLabel = new JLabel("Belopp:");
        JTextField amountTextField = new JTextField(20);
        JPanel amountPanel = new JPanel();
        amountPanel.add(amountLabel);
        amountPanel.add(amountTextField);

        return amountPanel;
    }

    private JPanel createWithdrawButtonPanel ()
    {
        JButton cancelButton = new JButton("Avbryt");
        cancelButton.addActionListener(e -> handleCancel());

        JButton submitButton = new JButton("Ta ut");
        submitButton.addActionListener(e -> performWithdraw());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        return buttonPanel;
    }

    private void performWithdraw ()
    {
        int accountNumber = getSelectedAccountNumber();
        int amount = parseWithdrawAmount();
        try
        {
            boolean result = bankLogic.withdraw(getPersonlNumber(), accountNumber, amount);
            if (result)
            {
                JOptionPane.showMessageDialog(mainFrame, "Uttaget lyckades!", "Uttag", JOptionPane.INFORMATION_MESSAGE);
                handleCancel();
            } else
            {
                JOptionPane.showMessageDialog(mainFrame, "Uttaget misslyckades!", "Uttag", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(mainFrame, "Uttaget misslyckades!", "Uttag", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getSelectedAccountNumber ()
    {
        JComboBox<Integer> accountListComboBox = getAccountListComboBox();
        return (int) accountListComboBox.getSelectedItem();
    }

    private int parseWithdrawAmount ()
    {
        JTextField amountTextField = getAmountTextField();
        String amountText = amountTextField.getText();
        try
        {
            return Integer.parseInt(amountText);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

// Utility methods to access components

    private JComboBox<Integer> getAccountListComboBox ()
    {
        return (JComboBox<Integer>) findComponentByName("accountListComboBox");
    }

    private JTextField getAmountTextField ()
    {
        return (JTextField) findComponentByName("amountTextField");
    }

    private Component findComponentByName (String name)
    {
        for (Component component : contentPanel.getComponents())
        {
            if (name.equals(component.getName()))
            {
                return component;
            }
        }
        return null;
    }


    private void handleNewCustomer ()
    {
        cleanPanels();
        createHeader("Ny kund", "Fyll i uppgifterna nedan");
        createSidebar();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);

        JLabel personalNumberLabel = new JLabel("Personnummer:");
        JTextField personalNumberTextfield = new JTextField(20);

        JLabel firstNameLabel = new JLabel("Förnamn:");
        JTextField firstNameTextfield = new JTextField(20);

        JLabel lastNameLabel = new JLabel("Efternamn:");
        JTextField lastNameTextfield = new JTextField(20);

        JButton submitButton = new JButton("Skapa kund");
        JButton cancelButton = new JButton("Avbryt");

        submitButton.addActionListener(e -> handleNewCustomerAction(firstNameTextfield, lastNameTextfield, personalNumberTextfield));
        cancelButton.addActionListener(e -> handleCancel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addFormField(formPanel, personalNumberLabel, personalNumberTextfield, gridBagConstraints, 0);
        addFormField(formPanel, firstNameLabel, firstNameTextfield, gridBagConstraints, 1);
        addFormField(formPanel, lastNameLabel, lastNameTextfield, gridBagConstraints, 2);
        addFormField(formPanel, buttonPanel, gridBagConstraints, 3);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    private void addFormField (JPanel panel, JLabel label, JTextField textField, GridBagConstraints constraints, int gridY)
    {
        constraints.gridx = 0;
        constraints.gridy = gridY;
        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.gridy = gridY;
        panel.add(textField, constraints);
    }

    private void addFormField (JPanel panel, JPanel component, GridBagConstraints constraints, int gridY)
    {
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = gridY;
        panel.add(component, constraints);
    }

    private void handleNewCustomerAction (JTextField firstNameTextfield, JTextField lastNameTextfield,
                                          JTextField personalNumberTextfield)
    {
        if (isNewCustomerDataValid(firstNameTextfield, lastNameTextfield, personalNumberTextfield))
        {
            String firstName = firstNameTextfield.getText();
            String lastName = lastNameTextfield.getText();
            String personalNumber = personalNumberTextfield.getText();
            boolean result = bankLogic.createCustomer(firstName, lastName, personalNumber);
            if (result)
            {
                JOptionPane.showMessageDialog(null, "Kunden skapades");
                loggedIn = true;
                setPersonlNumber(personalNumber);
                menuBar = createMenuBar();
                mainFrame.setJMenuBar(menuBar);
                handleCancel();
            } else
            {
                JOptionPane.showMessageDialog(null, "Kunden kunde inte skapas");
                return;
            }
        } else
        {
            JOptionPane.showMessageDialog(null, "Felaktiga uppgifter");
            return;
        }
    }

    private boolean isNewCustomerDataValid (JTextField firstNameTextfield, JTextField lastNameTextfield,
                                            JTextField personalNumberTextfield)
    {
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String personalNumber = personalNumberTextfield.getText();
        return isNameValid(firstName) && isNameValid(lastName) && isPersonalNumberValid(personalNumber);
    }

    /**
     * Check if a give name is valid or not. The valid name has to pass the following conditions:
     * <ol>
     *     <li> Has a minimum length of 2 chars</li>
     *     <li> Contains only letters, spaces, hyphens and apostrophes</li>
     * </ol>
     *
     * @param name The name to be checked for validity
     * @return true if the name is valid, false otherwise
     */
    private boolean isNameValid (String name)
    {
        String regex = "[a-zA-Z- ']+";
        return name.matches(regex) && name.length() > 2;
    }

    /**
     * Check if a given personal number is valid or not. The valid personal number has to pass the following conditions:
     * <ol>
     *     <li> Has a length of 10 or 12 chars</li>
     *     <li> Contains only digits</li>
     *     <li> If the length is 12, the first 2 chars are 19 or 20</li>
     * </ol>
     *
     * @param personalNumber The personal number to be checked for validity
     * @return true if the personal number is valid, false otherwise
     */
    private boolean isPersonalNumberValid (String personalNumber)
    {
        String regex = "\\d+";
        return personalNumber.matches(regex) && (personalNumber.length() == 10 || personalNumber.length() == 12) &&
                (personalNumber.length() == 10 || personalNumber.substring(0, 2).equals("19") || personalNumber.substring(0, 2).equals("20"));
    }

    public JMenu createAccountMenu ()
    {
        JMenu menu = new JMenu("Account");
        JMenuItem newSavingAccount = createMenuItem("Nytt sparkonto", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleNewAccount("saving");
            }
        });
        JMenuItem newCreditAccount = createMenuItem("Nytt kreditkonto", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleNewAccount("credit");
            }
        });
        JMenuItem showAccount = createMenuItem("Visa konto", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleShowAccount();
            }
        });
        JMenuItem closeAccount = createMenuItem("Stäng konto", new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleCloseAccount();
            }
        });
        menu.add(newSavingAccount);
        menu.add(newCreditAccount);
        menu.add(showAccount);
        menu.add(closeAccount);
        return menu;
    }


    private void handleCloseAccount ()
    {
        if (checkIfCustomerIsNotSet())
        {
            JPanel formPanel = new JPanel(new BorderLayout());
            ArrayList<String> accounts = bankLogic.getCustomerAccounts(getPersonlNumber());

            if (accounts.isEmpty())
            {
                formPanel.add(new JLabel("Det finns inga konton"));
            } else
            {
                JLabel chooseAccount = new JLabel("Välj konto:");
                JComboBox<String> accountCombobox = new JComboBox<>(accounts.toArray(new String[0]));
                JButton submitButton = new JButton("Stäng konto");

                submitButton.addActionListener(e -> closeAccount(accountCombobox));

                formPanel.add(chooseAccount, BorderLayout.NORTH);
                formPanel.add(accountCombobox, BorderLayout.CENTER);
                formPanel.add(submitButton, BorderLayout.SOUTH);
            }

            contentPanel.removeAll();
            contentPanel.add(formPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            mainFrame.pack();
        }
    }

    /**
     * Check if a customer is set or not. If not, display an error message
     *
     * @return true if a customer is not set, false otherwise
     */
    private boolean checkIfCustomerIsNotSet ()
    {
        if (getPersonlNumber() == null)
        {
            JOptionPane.showMessageDialog(mainFrame, "Du måste välja en kund först!", "Fel", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Close the account with the given account number and display the result in the form panel
     *
     * @param accountCombobox The combobox containing the account number
     */
    private void closeAccount (JComboBox<String> accountCombobox)
    {
        String accountNumber = (String) accountCombobox.getSelectedItem();
        int accountNumberInt = Integer.parseInt(accountNumber);
        String result = bankLogic.closeAccount(getPersonlNumber(), accountNumberInt);

        JPanel formPanel = (JPanel) accountCombobox.getParent();
        formPanel.removeAll();

        if (result != null)
        {
            String text = String.format("Kontot stängdes. Här är sammanfattningen:\n%s", result);
            JLabel accountInfo = new JLabel(text);
            formPanel.add(accountInfo, BorderLayout.SOUTH);
        } else
        {
            JLabel warning = new JLabel("Kontot kunde inte stängas");
            warning.setForeground(Color.RED);
            formPanel.add(warning, BorderLayout.SOUTH);
        }

        formPanel.revalidate();
        formPanel.repaint();
        mainFrame.pack();
    }

    /**
     * Handles the action of creating a new account. It shows a form to the user to enter the account details
     */
    private void handleShowAccount ()
    {
        if (!checkIfCustomerIsSet())
            return;
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        ArrayList<String> accounts = bankLogic.getCustomerAccounts(getPersonlNumber());
        if (accounts.size() == 0)
        {
            formPanel.add(new JLabel("Det finns inga konton"));
            return;
        }
        for (String account : accounts)
        {
            formPanel.add(new JLabel(account), BorderLayout.CENTER);
        }
        contentPanel.removeAll();
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        mainFrame.pack();
    }

    /**
     * Checks if a customer is set, it checks whether the personal number is set or not
     *
     * @return true if a customer is set, false otherwise
     */
    private boolean checkIfCustomerIsSet ()
    {
        if (!loggedIn)
        {
            JOptionPane.showMessageDialog(mainFrame, "Du måste sätta en kund först");
            return false;
        }
        return true;
    }

    /**
     * Handles the creation of a new account
     *
     * @param accountType The type of account to be created
     */
    private void handleNewAccount (String accountType)
    {
        accountType = accountType.toLowerCase(Locale.ROOT);
        if (accountType.equals("savings") || accountType.equals("saving"))
        {
            if (!checkIfCustomerIsSet())
                return;
            int accountNumber = bankLogic.createSavingsAccount(getPersonlNumber());
            if (accountNumber > 0)
            {
                JOptionPane.showMessageDialog(mainFrame, "Sparkonto skapades \nKontonummer: " + accountNumber);
            } else
            {
                JOptionPane.showMessageDialog(mainFrame, "Sparkonto kunde inte skapas");
            }
        } else if (accountType.equals("credit") || accountType.equals("credits"))
        {
            if (!loggedIn)
            {
                JOptionPane.showMessageDialog(mainFrame, "Du måste sätta en kund först");
                return;
            }
            int accountNumber = bankLogic.createCreditAccount(getPersonlNumber());
            if (accountNumber > 0)
            {
                JOptionPane.showMessageDialog(mainFrame, "Kreditkonto skapades \nKontonummer: " + accountNumber);
            } else
            {
                JOptionPane.showMessageDialog(mainFrame, "Kreditkonto kunde inte skapas");
            }
        }
    }


    /**
     * Create and update the timer in the footer of the application with the current local date and time
     * The time format is: YYYY-MM-DD HH:MM:SS e.g. 2019-01-01 12:00:00
     * The timer will be updated every second.
     */
    public JPanel updateTimer ()
    {
        JPanel timerPanel = new JPanel();
        // transparent background
        timerPanel.setOpaque(false);
        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                String strDate = formatter.format(date);
                JLabel timerLabel = new JLabel(strDate);
                timerLabel.setForeground(Color.WHITE);
                timerLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
                timerPanel.removeAll();
                timerPanel.add(timerLabel);
                timerPanel.revalidate();
                timerPanel.repaint();

            }
        });
        timer.start();
        return timerPanel;
    }

    /**
     * Create the right side of the header which shows the customer data (name).
     * If no customer is set, the header will show "Okänd kund" in red color.
     *
     * @return JPanel with the customer data
     */
    private JPanel createCustomerData ()
    {
        JPanel customerData = new JPanel();
        customerData.setLayout(new BorderLayout());
        customerData.setBackground(Color.WHITE);
        customerData.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel customer = new JLabel();
        if (loggedIn)
        {
            customer.setText("Kund: " + bankLogic.getFullName(getPersonlNumber()));
        } else
        {
            JLabel unknownCustomerLabel = new JLabel("Okänd kund");
            unknownCustomerLabel.setForeground(Color.RED);
            customer.setText("Kund: ");
            customerData.add(unknownCustomerLabel, BorderLayout.CENTER);
        }
        customerData.add(customer, BorderLayout.WEST);
        return customerData;
    }

    /**
     * Create the left side of the header which shows the current view and a short description of the view.
     *
     * @param title       : the title of the application or current view
     * @param description : Additional description of the application or current view
     * @return the panel which contains the title and description of the application or current view
     */
    private JPanel createWelcomeMessageHeader (String title, String description)
    {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        JPanel welcomeMessagePanel = new JPanel();
        welcomeMessagePanel.setLayout(new BorderLayout());
        welcomeMessagePanel.add(titleLabel, BorderLayout.NORTH);
        welcomeMessagePanel.add(descriptionLabel, BorderLayout.SOUTH);
        welcomeMessagePanel.setBackground(Color.WHITE);
        welcomeMessagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return welcomeMessagePanel;
    }

    /**
     * Creates the header of the application with a title and a short description and adds it to the headerPanel
     * Then the customer's name will be shown in the right handside of the header.
     *
     * @param mainTitle:       the title of the application or current view
     * @param shortDescription : Additional description of the application or current view
     */
    public void createHeader (String mainTitle, String shortDescription)
    {
        headerPanel.removeAll();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(createWelcomeMessageHeader(mainTitle, shortDescription), BorderLayout.WEST);
        Icon icon = new ImageIcon(getClass().getResource("header.png"));
        JLabel headerImage = new JLabel(icon);
        headerPanel.add(createCustomerData(), BorderLayout.EAST);
        headerPanel.add(headerImage, BorderLayout.CENTER);
        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.pack();
    }

    public void updateHeader (String title, String description)
    {
        headerPanel.removeAll();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(createWelcomeMessageHeader(title, description), BorderLayout.WEST);
        headerPanel.add(createCustomerData(), BorderLayout.EAST);
    }

    /**
     * Create the sidebar for main view (first page) which includes some useful links to different web pages and/or
     * apps e.g. Outlook, Teams, OneDrive, etc.
     * The content area contains the different views of the application.
     */
    private void createMainViewSidebar ()
    {
        // create a label and put it into the sidebarPanel and rerender the frame
        JLabel newLabel = new JLabel("Sidebar");
        sidebarPanel.add(newLabel);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();

    }

    private JPanel createFooterNav ()
    {
        JPanel nav = new JPanel();
        nav.setName("footerNav");
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // set transparent nav panel
        nav.setOpaque(false);
        JLabel[] labels = new JLabel[4];
        String[] linkNames = {"Outlook", "Teams", "OneDrive", "Bankens nyheter"};
        String[] links = {"https://outlook.office.com/mail/inbox", "https://teams.microsoft.com/", "https://onedrive.live.com/about/sv-se/signin/", "https://www.test.test"};

        for (int i = 0; i < links.length; i++)
        {
            JLabel label = new JLabel(linkNames[i]);
            label.setForeground(Color.BLUE);
            label.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            labels[i] = label;
            labels[i].setForeground(Color.BLUE);
            labels[i].setFont(new Font(FONT_NAME, Font.PLAIN, 12));
            labels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            int finalI = i;
            labels[i].addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked (MouseEvent e)
                {
                    try
                    {
                        Desktop.getDesktop().browse(new URI(links[finalI]));
                    } catch (IOException | URISyntaxException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            });
            nav.add(labels[i]);
        }
        return nav;
    }


    private void createFooter ()
    {
        // Create footer navigation
        JPanel footerNav = createFooterNav();
        footerNav.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Create timer panel
        JPanel timer = updateTimer();

        // Set layout manager for footerPanel
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(new Color(0, 153, 0));

        // Add footerNav to the west side of footerPanel
        footerPanel.add(footerNav, BorderLayout.WEST);

        // Add timer to the east side of footerPanel
        footerPanel.add(timer, BorderLayout.CENTER);

        // Add footerPanel to the mainFrame
        mainFrame.add(footerPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

}