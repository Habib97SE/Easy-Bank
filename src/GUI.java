import habhez0.BankLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class GUI
{
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private String personlNumber = "";
    private boolean loggedIn = false;
    private BankLogic bankLogic;
    private JFrame mainFrame;
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel footerPanel;
    private JMenuBar menuBar;

    public GUI (String programName)
    {
        this.bankLogic = new BankLogic();
        mainFrame = new JFrame(programName);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        mainFrame.setJMenuBar(createMenuBar());
        mainFrame.setVisible(true);

        headerPanel = new JPanel();
        sidebarPanel = new JPanel();
        contentPanel = new JPanel();
        footerPanel = new JPanel();

        createHeader("Välkommen", "This is a test");

        headerPanel.setPreferredSize(new Dimension(WIDTH, 100));
        sidebarPanel.setPreferredSize(new Dimension(200, HEIGHT - 100));
        contentPanel.setPreferredSize(new Dimension(WIDTH - 200, HEIGHT - 100));
        footerPanel.setPreferredSize(new Dimension(WIDTH, 100));

        createSidebar();

        footerPanel.setBackground(Color.white);
        headerPanel.setBackground(Color.white);

        // give sidebar margin from left
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(sidebarPanel, BorderLayout.WEST);
        mainFrame.add(contentPanel, BorderLayout.CENTER);
        mainFrame.add(footerPanel, BorderLayout.SOUTH);

        updateTimer();

        mainFrame.setBackground(Color.white);


        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);

    }

    public void cleanPanels ()
    {
        // remove all elements from panels
        headerPanel.removeAll();
        sidebarPanel.removeAll();
        contentPanel.removeAll();
        footerPanel.removeAll();

        // repaint panels
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
        } else {
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
            cleanPanels();
            createHeader("Välkommen", "This is a test");
            createSidebar();
            mainFrame.setJMenuBar(createMenuBar());
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
                    }
                    else
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
        cleanPanels();
        createHeader("Visa kund", "Du kan visa kundens information här");
        createSidebar();
        JPanel formPanel = new JPanel(new BorderLayout());
        JLabel personalNumberLabel = new JLabel("Personnummer: " + getPersonlNumber());
        JLabel firstNameLabel = new JLabel("Förnamn: " + bankLogic.getFullName(personlNumber).split(" ")[0]);
        JLabel lastNameLabel = new JLabel("Efternamn: " + bankLogic.getFullName(personlNumber).split(" ")[1]);

        personalNumberLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 0));
        firstNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 0));
        lastNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 0));

        GridBagLayout gridBagLayout = new GridBagLayout();
        formPanel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        formPanel.add(personalNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        formPanel.add(firstNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        formPanel.add(lastNameLabel, gridBagConstraints);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.setBackground(Color.white);

        mainFrame.pack();


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
        cleanPanels();
        createHeader("Välkommen", "Välkommen till banken");
        createSidebar();
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();
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

    private void createSidebar ()
    {
        sidebarPanel.removeAll();
        JLabel sideBarTitle = new JLabel("Funktioner:");

        sideBarTitle.setHorizontalAlignment(JLabel.CENTER);
        sideBarTitle.setPreferredSize(new Dimension(sidebarPanel.getWidth(), 50));
        sidebarPanel.add(sideBarTitle);

        // this sidebar is a menu in vertical orientation
        JButton withdrawButton = new JButton("Ta ut pengar");
        JButton depositButton = new JButton("Sätt in pengar");
        JButton showBalanceButton = new JButton("Visa saldo");
        JButton showTransactionsButton = new JButton("Visa transaktioner");
        JButton transferButton = new JButton("Överföring");

        // add all buttons to sidebar
        sidebarPanel.add(withdrawButton);
        sidebarPanel.add(depositButton);
        sidebarPanel.add(showBalanceButton);
        sidebarPanel.add(showTransactionsButton);
        sidebarPanel.add(transferButton);

        // set the buttons width to the same as the sidebar - 15px
        Dimension buttonDimension = new Dimension(sidebarPanel.getWidth() - 15, 50);
        withdrawButton.setPreferredSize(buttonDimension);
        depositButton.setPreferredSize(buttonDimension);
        showBalanceButton.setPreferredSize(buttonDimension);
        showTransactionsButton.setPreferredSize(buttonDimension);
        transferButton.setPreferredSize(buttonDimension);

        // add sidebar to mainframe
        mainFrame.add(sidebarPanel, BorderLayout.WEST);
        mainFrame.pack();
    }

    private void handleNewCustomer ()
    {
        cleanPanels();
        createHeader("Ny kund", "Fyll i uppgifterna nedan");
        createSidebar();

        // create the form
        JPanel formPanel = new JPanel(new BorderLayout());

        JLabel personalNumberLabel = new JLabel("Personnummer:");
        JLabel firstNameLabel = new JLabel("Förnamn:");
        JLabel lastNameLabel = new JLabel("Efternamn:");

        JTextField personalNumberTextfield = new JTextField(20);
        JTextField firstNameTextfield = new JTextField(20);
        JTextField lastNameTextfield = new JTextField(20);

        JPanel personalNumber = new JPanel();
        personalNumber.add(personalNumberLabel);
        personalNumber.add(personalNumberTextfield);
        personalNumber.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel firstName = new JPanel();
        firstName.add(firstNameLabel);
        firstName.add(firstNameTextfield);
        firstName.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel lastName = new JPanel();
        lastName.add(lastNameLabel);
        lastName.add(lastNameTextfield);
        lastName.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));


        // create the submit button
        JButton submitButton = new JButton("Skapa kund");
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                String personalNumberStr = personalNumberTextfield.getText();
                String firstName = firstNameTextfield.getText();
                String lastName = lastNameTextfield.getText();

                if (!bankLogic.isPersonalNumberValid(personalNumberStr))
                {
                    JOptionPane.showMessageDialog(mainFrame, "Personnumret är inte giltigt");
                    return;
                }

                if (personalNumberStr.isEmpty() || firstName.isEmpty() || lastName.isEmpty())
                {
                    JOptionPane.showMessageDialog(mainFrame, "Du måste fylla i alla fält");
                    return;
                }
                try
                {
                    if (bankLogic.customerExists(personalNumberStr))
                    {
                        JOptionPane.showMessageDialog(mainFrame, "Kunden finns redan");
                        return;
                    } else
                    {
                        boolean result = bankLogic.createCustomer(firstName, lastName, personalNumberStr);

                        if (result)
                        {
                            loggedIn = true;
                            setPersonlNumber(personalNumberStr);
                            JOptionPane.showMessageDialog(mainFrame, "Kunden skapades");
                            cleanPanels();
                            createHeader("Välkommen", "Välkommen till banken");
                            createSidebar();
                            mainFrame.setJMenuBar(createMenuBar());
                            mainFrame.revalidate();
                            mainFrame.repaint();
                            mainFrame.pack();
                        } else
                        {
                            JOptionPane.showMessageDialog(mainFrame, "Kunden kunde inte skapas");
                        }

                    }
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage());
                }
            }
        });

        JButton cancelButton = new JButton("Avbryt");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                cleanPanels();
                updateHeader("Välkommen till banken", "Välj en funktion i menyn till vänster");
                createSidebar();
                contentPanel.removeAll();
                contentPanel.revalidate();
                contentPanel.repaint();
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
        JMenuItem closeAccount = new JMenuItem("Stäng konto");
        menu.add(newSavingAccount);
        menu.add(newCreditAccount);
        menu.add(showAccount);
        menu.add(closeAccount);
        return menu;
    }

    private void handleShowAccount ()
    {
        if (!loggedIn)
        {
            JOptionPane.showMessageDialog(mainFrame, "Du måste sätta en kund först");
            return;
        }
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

    private void handleNewAccount (String accountType)
    {
        accountType = accountType.toLowerCase(Locale.ROOT);
        if (accountType.equals("savings") || accountType.equals("saving"))
        {
            if (!loggedIn)
            {
                JOptionPane.showMessageDialog(mainFrame, "Du måste sätta en kund först");
                return;
            }
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
    public void updateTimer ()
    {
        // update the timer every second for the footer
        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = formatter.format(date);

                // show timer in the center of footer

                footerPanel.removeAll();
                footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                footerPanel.add(new JLabel(strDate));
                footerPanel.revalidate();
                footerPanel.repaint();
                footerPanel.setBackground(new Color(0, 153, 0));
            }
        });
        timer.start();

        mainFrame.add(footerPanel, BorderLayout.SOUTH);
        mainFrame.pack();
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

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
        headerPanel.add(createCustomerData(), BorderLayout.EAST);
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
}