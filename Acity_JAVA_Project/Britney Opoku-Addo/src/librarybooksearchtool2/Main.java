/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package librarybooksearchtool2;

/**
 *
 * @author HP
 */
public class Main {
    public static void main(String[] args) {
        try {
            BookDatabase db = new BookDatabase();
            new BookUI(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

