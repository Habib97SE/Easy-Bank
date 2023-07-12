import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test
{
    public Test()
    {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new FlowLayout());

        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("Item 1");
        comboBox.addItem("Item 2");
        comboBox.addItem("Item 3");

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Selected: " + comboBox.getSelectedItem());
            }
        });

        frame.add(comboBox);
        frame.add(selectButton);


        frame.setVisible(true);
    }
}