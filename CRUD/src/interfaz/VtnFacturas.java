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
import javax.swing.JTextField;

/**
 *
 * @author Ricardo S
 */
public class VtnFacturas extends javax.swing.JFrame {

    /**
     * Creates new form VtnFacturas
     */
    public VtnFacturas() {
        initComponents();
        setBounds(220, 100, 970, 730);
        Dbconexiones db = new Dbconexiones();
        jSelectproducto.setModel(db.getProducto());
    }

    public void obtenerCliente(String id) {
        try {
            String consultatabla = "SELECT * FROM Clientes where ID=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, id);

            ResultSet res = pstm.executeQuery();

            while (res.next()) {
                Object[] filas = new Object[5];//para pasar las columnas 
                filas[0] = res.getString("ID");
                filas[1] = res.getString("nombre");
                filas[2] = res.getString("apellido");
                filas[3] = res.getString("telefono");
                filas[4] = res.getString("correo");

                String nombre = filas[1].toString();
                String apellido = filas[2].toString();
                String telefono = filas[3].toString();
                String correo = filas[4].toString();

                txt_nombre.setText(nombre);
                txt_idapellido.setText(apellido);
                txt_telefono.setText(telefono);
                txt_correo.setText(correo);

            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    //IMPRIMIR FACTURA
    public void imprimirPdf3(int id, String nombre) {
        Document documento = new Document();
        try {
            String ruta = System.getProperty("user.home") + "\\Desktop\\'" + nombre + "'.pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance("src/images/banner.jpg");
            header.scaleToFit(500, 600);
            header.setAlignment(Chunk.ALIGN_CENTER);
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("FACTURA Cliente. \n\n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);
            PdfPTable tablafac = new PdfPTable(4);
            tablafac.addCell("ID");
            tablafac.addCell("NOMBRE");
            tablafac.addCell("APELLIDO");
            tablafac.addCell("TELEFONO");
            //tablaCliente.addCell("Genero");
            //tablaCliente.addCell("Pais");
            //tablaCliente.addCell("Lenguage");

            com.mysql.jdbc.Connection cn = null;
            try {
                cn = (com.mysql.jdbc.Connection) Conexion.conectar();
                com.mysql.jdbc.PreparedStatement pst = (com.mysql.jdbc.PreparedStatement) cn.prepareStatement(
                        "select ID,nombre,apellido,telefono  from clientes where nombre=?");

                pst.setString(1, nombre);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    do {
                        tablafac.addCell(rs.getString(1));
                        tablafac.addCell(rs.getString(2));
                        tablafac.addCell(rs.getString(3));
                        tablafac.addCell(rs.getString(4));
                    } while (rs.next());

                    documento.add(tablafac);
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
            parrafo2.add("\n\n DESCRIPCION \n\n");
            parrafo2.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.DARK_GRAY));

            documento.add(parrafo2);

            PdfPTable tablafactura = new PdfPTable(3);

            tablafactura.addCell("producto");
            tablafactura.addCell("valor unitario");
            tablafactura.addCell("cantidad");
          //  tablafactura.addCell("valor Total");

            com.mysql.jdbc.Connection cn2 = null;
            try {
                cn2 = (com.mysql.jdbc.Connection) Conexion.conectar();
                com.mysql.jdbc.PreparedStatement pst2 = (com.mysql.jdbc.PreparedStatement) cn2.prepareStatement(
                        "select p.nombre,p.precio,p.cantidad,f.monto from clientes c ,productos p, facturas f,reg_fact_prod rfp where rfp.id_factura=f.id_factura and f.id_cliente=c.ID and p.ID_producto=rfp.id_producto and c.id_cliente like ?");
                 pst2.setString(1,"%"+id+"%");

                ResultSet rs2 = pst2.executeQuery();

                if (rs2.next()) {
                    do {
                        tablafactura.addCell(rs2.getString(1));
                        tablafactura.addCell(rs2.getString(2));
                        tablafactura.addCell(rs2.getString(3));

                    } while (rs2.next());

                    documento.add(tablafactura);
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
        jLabel1 = new javax.swing.JLabel();
        txt_idcliente = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_idapellido = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_correo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabDatosprod = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jSelectproducto = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txt_Cantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_Cantidad1 = new javax.swing.JTextField();
        btnimprimir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jLabel1.setText("ID Cliente");

        txt_idcliente.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_idcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idclienteActionPerformed(evt);
            }
        });
        txt_idcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idclienteKeyTyped(evt);
            }
        });

        txt_nombre.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre");

        txt_idapellido.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_idapellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idapellidoActionPerformed(evt);
            }
        });

        jLabel3.setText("Apellido");

        txt_telefono.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_telefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telefonoActionPerformed(evt);
            }
        });

        jLabel4.setText("Teléfono");

        txt_correo.setSelectionColor(new java.awt.Color(255, 0, 0));
        txt_correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_correoActionPerformed(evt);
            }
        });

        jLabel5.setText("Correo");

        tabDatosprod.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 204, 0)));
        tabDatosprod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Varlor unitario", "Cantidad", "Valor total"
            }
        ));
        tabDatosprod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabDatosprodMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabDatosprod);

        jLabel6.setText("Producto");

        jSelectproducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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

        jLabel7.setText("Cantidad");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total");

        btnimprimir.setText("IMPRIMIR");
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar factura");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(164, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(18, 18, 18)
                            .addComponent(jSelectproducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_idcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(12, 12, 12)
                                    .addComponent(txt_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(24, 24, 24)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(8, 8, 8)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_idapellido)
                                .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnimprimir)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(303, 303, 303))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txt_Cantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(221, 221, 221))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_idcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txt_idapellido, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txt_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSelectproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Cantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnimprimir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_idclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idclienteActionPerformed

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void txt_idapellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idapellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idapellidoActionPerformed

    private void txt_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoActionPerformed

    private void txt_correoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_correoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_correoActionPerformed

    private void tabDatosprodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabDatosprodMouseClicked
        int dat = tabDatosprod.getSelectedRow();
        String datotab = (String) tabDatosprod.getValueAt(dat, 0).toString();
        String nombreprod = (String) tabDatosprod.getValueAt(dat, 1);
        String precio = (String) tabDatosprod.getValueAt(dat, 2);
        String cantidad = (String) tabDatosprod.getValueAt(dat, 3);
        txt_idcliente.setText(datotab);

        txt_Cantidad.setText(cantidad);
    }//GEN-LAST:event_tabDatosprodMouseClicked

    private void jSelectproductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSelectproductoItemStateChanged
        Dbconexiones db = new Dbconexiones();
        String seleccionprodu = jSelectproducto.getSelectedItem().toString();
        tabDatosprod.setModel(db.getProducto2(seleccionprodu));
    }//GEN-LAST:event_jSelectproductoItemStateChanged

    private void jSelectproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSelectproductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String idcliente = txt_idcliente.getText();

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txt_idclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idclienteKeyTyped
        Dbconexiones db = new Dbconexiones();
        String idcliente = txt_idcliente.getText();
        // jTDatoCliente.setModel(db.getcliente(idcliente));

        obtenerCliente(idcliente);
    }//GEN-LAST:event_txt_idclienteKeyTyped

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
  String nombre=txt_nombre.getText();
        int id=Integer.parseInt(txt_idcliente.getText());
        imprimirPdf3(id, nombre) ;       //        // TODO add your handling code here:
    }//GEN-LAST:event_btnimprimirActionPerformed

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
            java.util.logging.Logger.getLogger(VtnFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VtnFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VtnFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VtnFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VtnFacturas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnimprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> jSelectproducto;
    private javax.swing.JTable tabDatosprod;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_Cantidad1;
    private javax.swing.JTextField txt_correo;
    private javax.swing.JTextField txt_idapellido;
    private javax.swing.JTextField txt_idcliente;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}
