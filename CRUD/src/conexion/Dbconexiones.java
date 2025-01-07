

package conexion;

import javax.swing.*;
import java.sql.*;

import javax.swing.table.DefaultTableModel;


public class Dbconexiones {

    public void AgregarCliente(String nombre, String apellido, String telefono, String correo) {
        try {

            String consultatabla = "INSERT INTO Clientes(nombre,apellido,telefono,correo) values(?,?,?,?)";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            
            pstm.setString(1, nombre);
            pstm.setString(2, apellido);
            pstm.setString(3, telefono);
            pstm.setString(4, correo);
          

            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

    }
    
 

    public DefaultTableModel mostrarCliente() {
        
           
        DefaultTableModel dtt = new DefaultTableModel();
        dtt.addColumn("ID");
        dtt.addColumn("nombre");
        dtt.addColumn("apellido");
        dtt.addColumn("telefono");
        dtt.addColumn("correo");
      
        try {
            String consultatabla = "SELECT * FROM Clientes";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);

            ResultSet res = pstm.executeQuery();

            while (res.next()) {
                Object[] filas = new Object[5];//para pasar las columnas 
                filas[0] = res.getString("ID");
                filas[1] = res.getString("nombre");
                filas[2] = res.getString("apellido");
                filas[3] = res.getString("telefono");
                filas[4] = res.getString("correo");
             
                dtt.addRow(filas);

            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return dtt;
    }
    
    

    public void Agregarproducto(String nombre, double precio, int cantidad) {

        try {
            String consultatabla = "INSERT INTO Productos(nombre,precio,cantidad) values(?,?,?)";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, nombre);
            pstm.setDouble(2, precio);
            pstm.setInt(3, cantidad);

            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    public DefaultComboBoxModel getProducto() {
        DefaultComboBoxModel dt = new DefaultComboBoxModel();
        try {
            Connection cn = Conexion.conectar();
            Statement pstm = cn.createStatement();
            ResultSet res = pstm.executeQuery(
                    "SELECT * FROM Productos ");

            while (res.next()) {
                dt.addElement(res.getString("nombre"));

            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return dt;
    }

    public DefaultTableModel getProducto2(String producto) {
        DefaultTableModel dtt = new DefaultTableModel();
        dtt.addColumn("IDproducto");
        dtt.addColumn("nombre");
        dtt.addColumn("precio");
        dtt.addColumn("cantidad");

        try {
            String consultatabla = "SELECT * FROM Productos WHERE nombre=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, producto);
            ResultSet res = pstm.executeQuery();

            while (res.next()) {
                Object[] filas = new Object[8];//para pasar las columnas 
                filas[0] = res.getString("IDproducto");
                filas[1] = res.getString("nombre");
                filas[2] =  
                filas[3] = res.getString("cantidad");
                dtt.addRow(filas);

            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return dtt;
    }

    public DefaultTableModel mostrarProducto() {
        DefaultTableModel dtt = new DefaultTableModel();
        dtt.addColumn("IDproducto");
        dtt.addColumn("nombre");
        dtt.addColumn("precio");
        dtt.addColumn("cantidad");

        try {
            String consultatabla = "SELECT * FROM productos";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);

            ResultSet res = pstm.executeQuery();

            while (res.next()) {
                Object[] filas = new Object[8];//para pasar las columnas 
                filas[0] = res.getString("ID_producto");
                filas[1] = res.getString("nombre");
                filas[2] = res.getString("precio");
                filas[3] = res.getString("cantidad");
                dtt.addRow(filas);

            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return dtt;
    }
}
