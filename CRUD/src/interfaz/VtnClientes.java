/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import conexion.Conexion;
import conexion.Dbconexiones;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Ricardo S
 */
public class VtnClientes extends javax.swing.JFrame {

    /**
     * Creates new form VtnClientes
     */
    public VtnClientes() {
        initComponents();
        setBounds(220, 100, 970, 730);
         Dbconexiones db = new Dbconexiones();
         tabDatosprod.setModel(db.mostrarCliente());
    }
    
    
    public void modificarCliente(String id,String nombre,String apellido, String telefono,String correo) {
        System.out.println("Esto es una prueba");
        
        try {
            String consultatabla = "update  clientes set  nombre=?, apellido=?, telefono=? ,correo=? where ID=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            
            pstm.setString(1, nombre);
            pstm.setString(2, apellido);
            pstm.setString(3, telefono);
             pstm.setString(4, correo);
            pstm.setString(5, id);
            

            pstm.executeUpdate();

           

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
    }
    
    
     public void EliminarCliente() {
        System.out.println("Esto es una prueba");

        String  idcliente = txt_idcliente.getText();
        try {
            String consultatabla = "delete from Clientes where ID=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, idcliente);

            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

    }
     
     /////////////////////*******************************************************************************
     //PDF
      public void imprimirPdf(String producto, String nombre) {
        Document documento = new Document();
        try {
            String ruta = System.getProperty("user.home") + "\\Desktop\\'" + nombre + "'.pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance("src/images/banner.jpg");
            header.scaleToFit(500, 600);
            header.setAlignment(Chunk.ALIGN_CENTER);
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Información Cliente. \n\n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);
            PdfPTable tablapelis = new PdfPTable(3);
            tablapelis.addCell("ID");
            tablapelis.addCell("NOMBRE");
            tablapelis.addCell("APELLIDO");
            //tablaCliente.addCell("Genero");
            //tablaCliente.addCell("Pais");
            //tablaCliente.addCell("Lenguage");

            com.mysql.jdbc.Connection cn = null;
            try {
                cn = (com.mysql.jdbc.Connection) Conexion.conectar();
                com.mysql.jdbc.PreparedStatement pst = (com.mysql.jdbc.PreparedStatement) cn.prepareStatement(
                        "select ID,nombre,apellido  from clientes where nombre=?");

                pst.setString(1, nombre);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    do {
                        tablapelis.addCell(rs.getString(1));
                        tablapelis.addCell(rs.getString(2));
                        tablapelis.addCell(rs.getString(3));
                    } while (rs.next());

                    documento.add(tablapelis);
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener los datos.. " + e);
            } finally {
                if (cn != null) {
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        System.err.println("Error al cerrar la conexión " + e);
                    }
                }
            }

            Paragraph parrafo2 = new Paragraph();
            parrafo2.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo2.add("\n\n PRODUCTOS DEL CLIENTE \n\n");
            parrafo2.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.DARK_GRAY));

            documento.add(parrafo2);

            PdfPTable tablaPelisporgenero = new PdfPTable(3);

            tablaPelisporgenero.addCell("nombre");
            tablaPelisporgenero.addCell("precio");
            tablaPelisporgenero.addCell("cantidad");

            com.mysql.jdbc.Connection cn2 = null;
            try {
                cn2 = (com.mysql.jdbc.Connection) Conexion.conectar();
                com.mysql.jdbc.PreparedStatement pst2 = (com.mysql.jdbc.PreparedStatement) cn2.prepareStatement(
                        "select nombre,precio,cantidad from Productos");
                // pst2.setString(1,"1");

                ResultSet rs2 = pst2.executeQuery();

                if (rs2.next()) {
                    do {
                        tablaPelisporgenero.addCell(rs2.getString(1));
                        tablaPelisporgenero.addCell(rs2.getString(2));
                        tablaPelisporgenero.addCell(rs2.getString(3));

                    } while (rs2.next());

                    documento.add(tablaPelisporgenero);
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener los datos  " + e);
            } finally {
                if (cn2 != null) {
                    try {
                        cn2.close();
                    } catch (SQLException e) {
                        System.err.println("Error al cerrar la conexión  " + e);
                    }
                }
            }

            documento.close();
            JOptionPane.showMessageDialog(null, "PDF del cliente creado correctamente");

        } catch (DocumentException | IOException e) {
            System.err.println("Error en PDF o ruta de imagen: " + e);
            JOptionPane.showMessageDialog(null, "¡¡¡Error en PDF!!!, contacte con el administrador");
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txt_idcliente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_nombreCliente = new javax.swing.JTextField();
        txt_apellidoCliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        BtnAgregar = new javax.swing.JButton();
        txt_correo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabDatosprod = new javax.swing.JTable();
        BtnModificar = new javax.swing.JButton();
        BtnEliminar = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 500));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_idcliente.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_idcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idclienteActionPerformed(evt);
            }
        });
        jPanel2.add(txt_idcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 100, 26));

        jLabel7.setText("NOMBRE");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 56, -1, 30));

        jLabel5.setText("ID");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 20, 30, 30));

        jLabel6.setText("APELLIDO");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 92, -1, 30));

        txt_nombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreClienteActionPerformed(evt);
            }
        });
        jPanel2.add(txt_nombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 56, 210, 30));

        txt_apellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_apellidoClienteActionPerformed(evt);
            }
        });
        jPanel2.add(txt_apellidoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 92, 210, 30));

        jLabel3.setText("CORREO");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 128, -1, 30));
        jPanel2.add(txt_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 164, 100, 30));

        jLabel1.setText("TELÉFONO");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 171, -1, -1));

        BtnAgregar.setBackground(new java.awt.Color(51, 51, 51));
        BtnAgregar.setForeground(new java.awt.Color(0, 204, 51));
        BtnAgregar.setText("AGREGAR");
        BtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarActionPerformed(evt);
            }
        });
        jPanel2.add(BtnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(169, 242, -1, 30));
        jPanel2.add(txt_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 128, 210, 30));

        tabDatosprod.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 204, 0)));
        tabDatosprod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE", "APELLIDO", "CORREO", "TELÉFONO"
            }
        ));
        tabDatosprod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabDatosprodMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabDatosprod);

        BtnModificar.setBackground(new java.awt.Color(51, 51, 51));
        BtnModificar.setForeground(new java.awt.Color(0, 204, 51));
        BtnModificar.setText("MODIFICAR");
        BtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnModificarActionPerformed(evt);
            }
        });

        BtnEliminar.setBackground(new java.awt.Color(51, 51, 51));
        BtnEliminar.setForeground(new java.awt.Color(0, 204, 51));
        BtnEliminar.setText("ELIMINAR");
        BtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarActionPerformed(evt);
            }
        });

        btn_imprimir.setBackground(new java.awt.Color(51, 51, 51));
        btn_imprimir.setForeground(new java.awt.Color(0, 204, 51));
        btn_imprimir.setText("IMPRIMIR");
        btn_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BtnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(250, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 970, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabDatosprodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabDatosprodMouseClicked
        int dat = tabDatosprod.getSelectedRow();
        String datotab = (String) tabDatosprod.getValueAt(dat, 0).toString();
        String nombrecliente = (String) tabDatosprod.getValueAt(dat, 1);
        String apellido= (String) tabDatosprod.getValueAt(dat, 2);
        String telefono = (String) tabDatosprod.getValueAt(dat, 3);
         String correo = (String) tabDatosprod.getValueAt(dat, 4);
        txt_idcliente.setText(datotab);
        txt_nombreCliente.setText(nombrecliente);
        txt_apellidoCliente.setText(apellido);
        txt_telefono.setText(telefono);
        txt_correo.setText(correo);
    }//GEN-LAST:event_tabDatosprodMouseClicked

    private void BtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnModificarActionPerformed
        Dbconexiones db = new Dbconexiones();
        String id=txt_idcliente.getText();
         String nombreCliente = txt_nombreCliente.getText();
        String apellidoCliente = txt_apellidoCliente.getText();
          String correoCliente = txt_correo.getText();
            String telefonoCliente = txt_telefono.getText();
        modificarCliente(id,nombreCliente,apellidoCliente,telefonoCliente,correoCliente);
        tabDatosprod.setModel(db.mostrarCliente());
    }//GEN-LAST:event_BtnModificarActionPerformed

    private void BtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarActionPerformed
        Dbconexiones db = new Dbconexiones();
        EliminarCliente();
        tabDatosprod.setModel(db.mostrarCliente());
    }//GEN-LAST:event_BtnEliminarActionPerformed

    private void txt_idclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idclienteActionPerformed

    private void txt_nombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreClienteActionPerformed

    private void txt_apellidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_apellidoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_apellidoClienteActionPerformed

    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarActionPerformed
        Dbconexiones db = new Dbconexiones();
        String nombreCliente = txt_nombreCliente.getText();
        String apellidoCliente = txt_apellidoCliente.getText();
          String correoCliente = txt_correo.getText();
            String TelefonoCliente = txt_telefono.getText();
      
        db.AgregarCliente(nombreCliente,apellidoCliente,TelefonoCliente,correoCliente);
        tabDatosprod.setModel(db.mostrarCliente());
    }//GEN-LAST:event_BtnAgregarActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        String nombre=txt_nombreCliente.getText();
        String producto=txt_telefono.getText();
        imprimirPdf(producto, nombre) ;       // TODO add your handling code here:
    }//GEN-LAST:event_btn_imprimirActionPerformed

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
            java.util.logging.Logger.getLogger(VtnClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VtnClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VtnClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VtnClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VtnClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JButton BtnEliminar;
    private javax.swing.JButton BtnModificar;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabDatosprod;
    private javax.swing.JTextField txt_apellidoCliente;
    private javax.swing.JTextField txt_correo;
    private javax.swing.JTextField txt_idcliente;
    private javax.swing.JTextField txt_nombreCliente;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}
