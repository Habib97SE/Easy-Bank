import habhez0.BankLogic;

import javax.swing.*;

public class Main
{
    public static void main (String[] args)
    {
        //new Test();
        BankLogic bankLogic = new BankLogic();
        new GUI("Easy Bank", "yyyy-MM-dd HH:mm:ss", bankLogic);
    }
}