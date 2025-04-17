/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankaccountmanagement;

/**
 *
 * @author Michael
 */
public class User {
    public int id;
    public String username;
    public String email;
    public String password;
    public double balance;
    public String accountNumber;

    public User(int id, String username, String email, String password, double balance, String accountNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }
}