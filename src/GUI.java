import habhez0.BankLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GUI
{
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private String personlNumber;
    private BankLogic bankLogic;
    private JFrame mainFrame;
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel footerPanel;

    public GUI (String programName)
    {
        bankLogic = new BankLogic();
        mainFrame = new JFrame(programName);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        URL imageUrl = null;
        try {
            imageUrl = GUI.class.getResource("/habhez0_files/icon.jpg");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        mainFrame.setIconImage(imageIcon.getImage());

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

        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 300);
        scrollBar.setPreferredSize(new Dimension(30, 0));


        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized (ComponentEvent e)
            {
                super.componentResized(e);
                //resizeFontSize();
            }
        });
    }

    public void cleanPanels ()
    {
        // remove all elements from panels
        headerPanel.removeAll();
        sidebarPanel.removeAll();
        contentPanel.removeAll();
        footerPanel.removeAll();

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
        JMenuItem editCustomer = new JMenuItem("Ändra kund");
        JMenuItem deleteCustomer = new JMenuItem("Ta bort kund");
        JMenuItem showCustomer = new JMenuItem("Visa kund");

        editCustomer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                handleEditCustomer();
            }
        });


        menu.add(newCustomer);
        menu.add(new JSeparator());
        menu.add(editCustomer);
        menu.add(deleteCustomer);
        menu.add(showCustomer);
        return menu;
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
                }
                else
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
        return personlNumber != null;
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
                String personalNumber = personalNumberTextfield.getText();
                String firstName = firstNameTextfield.getText();
                String lastName = lastNameTextfield.getText();
                if (personalNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty())
                {
                    JOptionPane.showMessageDialog(mainFrame, "Du måste fylla i alla fält");
                    return;
                }
                try
                {
                    if (bankLogic.customerExists(personalNumber))
                    {
                        JOptionPane.showMessageDialog(mainFrame, "Kunden finns redan");
                        return;
                    } else
                    {
                        bankLogic.createCustomer(personalNumber, firstName, lastName);
                        JOptionPane.showMessageDialog(mainFrame, "Kunden skapades");
                        cleanPanels();
                        updateHeader("Välkommen till banken", "Välj en funktion i menyn till vänster");
                        createSidebar();
                        // remove all elements from contentPanel
                        contentPanel.removeAll();
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        setPersonlNumber(personalNumber);

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
        JMenuItem newSavingAccount = new JMenuItem("Nytt sparkonto");
        JMenuItem newCreditAccount = new JMenuItem("Nytt kreditkonto");
        JMenuItem showAccount = new JMenuItem("Visa konto");
        JMenuItem closeAccount = new JMenuItem("Stäng konto");
        menu.add(newSavingAccount);
        menu.add(newCreditAccount);
        menu.add(showAccount);
        menu.add(closeAccount);
        return menu;
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
            }
        });
        timer.start();

        mainFrame.add(footerPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

    private void resizeFontSize ()
    {
        float windowWidth = mainFrame.getWidth();
        float windowHeight = mainFrame.getHeight();
        float size = Math.min(windowWidth, windowHeight) / 10f;

        Component[] components = mainFrame.getContentPane().getComponents();

        // Loop through all components
        for (Component component : components)
        {
            if (component instanceof JPanel)
            {
                // Iterate through components within the panel
                Component[] innerComponents = ((JPanel) component).getComponents();
                for (Component innerComponent : innerComponents)
                {
                    // Resize the font size of all labels
                    if (innerComponent instanceof JLabel)
                    {
                        System.out.println("Label");
                        JLabel label = (JLabel) innerComponent;
                        Font font = label.getFont();
                        label.setFont(font.deriveFont(size + 5.0f));
                    }
                    // Resize the font size of all buttons
                    else if (innerComponent instanceof JButton)
                    {
                        JButton button = (JButton) innerComponent;
                        Font font = button.getFont();

                    }
                    // Resize the font size of all text fields
                    else if (innerComponent instanceof JTextField)
                    {
                        JTextField textField = (JTextField) innerComponent;
                        Font font = textField.getFont();
                        textField.setFont(font.deriveFont(size + 5.0f));
                    }
                }
            } else
            {
                // Resize the font size of the component itself
                if (component instanceof JLabel)
                {
                    JLabel label = (JLabel) component;
                    Font font = label.getFont();
                    size = font.getSize2D() + 5.0f;
                    label.setFont(font.deriveFont(size));
                } else if (component instanceof JButton)
                {
                    JButton button = (JButton) component;
                    Font font = button.getFont();
                    size = font.getSize2D() + 5.0f;
                    button.setFont(font.deriveFont(size));
                } else if (component instanceof JTextField)
                {
                    JTextField textField = (JTextField) component;
                    Font font = textField.getFont();
                    size = font.getSize2D() + 5.0f;
                    textField.setFont(font.deriveFont(size));
                }
            }
        }
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
        if (isCustomerSet())
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