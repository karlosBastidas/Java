/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clases;

import conexion.Conexion;
import conexion.Dbconexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import javax.swing.*;
public class VtnProductos extends JFrame {

    public VtnProductos() {
        initComponents();
        setTitle("CONSULTA-DATOS");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(VtnProductos.EXIT_ON_CLOSE);
       
        Dbconexiones db = new Dbconexiones();
        jSelectproducto.setModel(db.getProducto());
        tabDatosprod.setModel(db.mostrarProducto());

    }

    public void modificarproducto(String idproducto,String nombre, double precio, int cantidad) {
        System.out.println("Esto es una prueba");
        
        try {
            String consultatabla = "update  productos set  nombre=?, precio=?, cantidad=? where ID_producto=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            
            pstm.setString(1, nombre);
            pstm.setDouble(2, precio);
            pstm.setInt(3, cantidad);
            pstm.setString(4, idproducto);
            

            pstm.executeUpdate();

           

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    public void Eliminarproducto() {
        System.out.println("Esto es una prueba");

        idproducto = txt_idprod.getText();
        try {
            String consultatabla = "delete from Productos where ID_producto=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, idproducto);

            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabDatosprod = new javax.swing.JTable();
        BtnModificar = new javax.swing.JButton();
        BtnEliminar = new javax.swing.JButton();
        BtnAgregar = new javax.swing.JButton();
        txt_precio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_nombreproducto1 = new javax.swing.JTextField();
        txt_Cantidad = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSelectproducto = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txt_idprod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CANTIDAD");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 100, 30));

        tabDatosprod.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 204, 0)));
        tabDatosprod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE", "PRECIO", "CANTIDAD"
            }
        ));
        tabDatosprod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabDatosprodMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabDatosprod);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 530, 190));

        BtnModificar.setBackground(new java.awt.Color(51, 51, 51));
        BtnModificar.setForeground(new java.awt.Color(0, 204, 51));
        BtnModificar.setText("MODIFICAR");
        BtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, -1, 30));

        BtnEliminar.setBackground(new java.awt.Color(51, 51, 51));
        BtnEliminar.setForeground(new java.awt.Color(0, 204, 51));
        BtnEliminar.setText("ELIMINAR");
        BtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, -1, 30));

        BtnAgregar.setBackground(new java.awt.Color(51, 51, 51));
        BtnAgregar.setForeground(new java.awt.Color(0, 204, 51));
        BtnAgregar.setText("AGREGAR");
        BtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, 30));

        txt_precio.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 204, 0), new java.awt.Color(51, 204, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        txt_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_precioActionPerformed(evt);
            }
        });
        jPanel1.add(txt_precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 240, 30));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ID");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 30, 30));

        txt_nombreproducto1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 204, 0), new java.awt.Color(51, 204, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        jPanel1.add(txt_nombreproducto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 240, 30));

        txt_Cantidad.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 204, 0), new java.awt.Color(51, 204, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        jPanel1.add(txt_Cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 90, 30));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PRECIO");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 100, 30));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SELECCIONAR");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 150, -1, -1));

        jSelectproducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSelectproducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 204, 0), new java.awt.Color(51, 204, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        jSelectproducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSelectproductoItemStateChanged(evt);
            }
        });
        jSelectproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectproductoActionPerformed(evt);
            }
        });
        jPanel1.add(jSelectproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 120, 30));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setForeground(new java.awt.Color(0, 204, 51));
        jButton1.setText("REGRESAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, -1, 30));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PRODUCTO");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 100, 30));

        txt_idprod.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 204, 0), new java.awt.Color(51, 204, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        jPanel1.add(txt_idprod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 60, -1));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PRODUCTOS");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 160, 40));

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 730, 10));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chip2.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-400, 0, -1, 560));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarActionPerformed
        Dbconexiones db = new Dbconexiones();
        Eliminarproducto();
        tabDatosprod.setModel(db.mostrarProducto());

    }//GEN-LAST:event_BtnEliminarActionPerformed

    private void txt_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_precioActionPerformed

    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarActionPerformed
        Dbconexiones db = new Dbconexiones();
        String nombreprod = txt_nombreproducto1.getText();
        double precio = Double.parseDouble(txt_precio.getText()) ;
        int cantidad = Integer.parseInt(txt_Cantidad.getText()) ;
        db.Agregarproducto(nombreprod, precio, cantidad);
        tabDatosprod.setModel(db.mostrarProducto());

    }//GEN-LAST:event_BtnAgregarActionPerformed

    private void BtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnModificarActionPerformed
        Dbconexiones db = new Dbconexiones();
         String   idproducto = txt_idprod.getText();
         String nombreprod = txt_nombreproducto1.getText();
        double precio = Double.parseDouble(txt_precio.getText());
        int cantidad =Integer.parseInt(txt_Cantidad.getText()) ;
        modificarproducto(idproducto,nombreprod,precio,cantidad);
        tabDatosprod.setModel(db.mostrarProducto());
    }//GEN-LAST:event_BtnModificarActionPerformed

    private void jSelectproductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSelectproductoItemStateChanged
        Dbconexiones db = new Dbconexiones();
        String seleccionprodu = jSelectproducto.getSelectedItem().toString();
        tabDatosprod.setModel(db.getProducto2(seleccionprodu));
    }//GEN-LAST:event_jSelectproductoItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Dbconexiones db = new Dbconexiones();
        tabDatosprod.setModel(db.mostrarProducto());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabDatosprodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabDatosprodMouseClicked
               //FORMA DE SELECCIONAR Y GUARDAR LA FILA DE UNA TABLA...
        int dat = tabDatosprod.getSelectedRow();
        String datotab = (String) tabDatosprod.getValueAt(dat, 0).toString();
        String nombreprod = (String) tabDatosprod.getValueAt(dat, 1);
        String precio = (String) tabDatosprod.getValueAt(dat, 2);
        String cantidad = (String) tabDatosprod.getValueAt(dat, 3);
        txt_idprod.setText(datotab);
        txt_nombreproducto1.setText(nombreprod);
        txt_precio.setText(precio);
        txt_Cantidad.setText(cantidad);

    }//GEN-LAST:event_tabDatosprodMouseClicked

    private void jSelectproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSelectproductoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VtnProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VtnProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VtnProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VtnProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VtnProductos().setVisible(true);
            }
        });
    }
    public String idproducto;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JButton BtnEliminar;
    private javax.swing.JButton BtnModificar;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jSelectproducto;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tabDatosprod;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_idprod;
    private javax.swing.JTextField txt_nombreproducto1;
    private javax.swing.JTextField txt_precio;
    // End of variables declaration//GEN-END:variables
}
