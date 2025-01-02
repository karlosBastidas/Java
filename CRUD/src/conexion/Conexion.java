/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;


import java.sql.*;

public class Conexion {
    

    
     public  static Connection conectar(){
         try {
         Connection cn = DriverManager.getConnection(
                 "jdbc:mysql://localhost/registrousuarios", "root", "root");
         return cn;
         } catch (SQLException e) {
         System.err.println("Error en la conexión local " + e);
         }
         return (null);
         }
       
        
}
    
   
    
    

