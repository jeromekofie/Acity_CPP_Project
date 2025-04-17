/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joshua_veve
 */
public class Loan {private int id;
    private double principal;
    private double rate;
    private int time;

    public Loan(int id, double principal, double rate, int time) {
        this.id = id;
        this.principal = principal;
        this.rate = rate;
        this.time = time;
    }

    public double calculateMonthlyPayment() {
        double monthlyRate = rate / 12 / 100;
        int months = time * 12;
        return (principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months));
    }

    public int getId() { return id; }
    public double getPrincipal() { return principal; }
    public double getRate() { return rate; }
    public int getTime() { return time; }
    
}
