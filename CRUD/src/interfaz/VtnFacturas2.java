package interfaz;

import clases.Producto;
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

import java.util.Calendar;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos
 */
public class VtnFacturas2 extends JInternalFrame {

    public VtnFacturas2() {
        initComponents();
        Dbconexiones db = new Dbconexiones();
        jSelectproducto.setModel(db.getProducto());
        //registro productos en la factura 
        tablafactura = new DefaultTableModel();

        //añadir columnas
        tablafactura.addColumn(" Nº ");
        tablafactura.addColumn("Producto");
        tablafactura.addColumn("Valor unitario");
        tablafactura.addColumn("Cantidad");
        tablafactura.addColumn("Total");
        tabDatosprod.setModel(tablafactura);
    }

    //tabla factura
    private void registroFactura(int num, String producto, double precio, int cantidad, double total) {
        Object[] filas = new Object[5];//para pasar las columnas 
        filas[0] = num;
        filas[1] = producto;
        filas[2] = precio;
        filas[3] = cantidad;
        filas[4] = total;

        tablafactura.addRow(filas);

    }

    private void guardar_Factura(int id, String fecha, double totalfact) {
        try {
            String consulta = "INSERT INTO facturas(id_cliente,fecha_emision,monto)  values(?,?,?)";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consulta);
            pstm.setInt(1, id);
            pstm.setString(2, fecha);
            pstm.setDouble(3, totalfact);
            pstm.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    //metodo para obtener cliente de la base de datos.
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

    //metodo para consultar el precio del producto
    public double consultaPrecio(String prod) {
        double val = 0;

        try {
            String consultatabla = "SELECT precio FROM Productos WHERE nombre=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, prod);
            ResultSet res = pstm.executeQuery();

            while (res.next()) {

                val = Double.parseDouble(res.getString("precio"));
                System.out.println("el precio es : " + val);

            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return val;
    }

    //validacion Stock
    private boolean validarStock(int cantidad, String prod) {
        int val = 0;
        try {
            String consultatabla = "SELECT cantidad FROM Productos WHERE nombre=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            pstm.setString(1, prod);
            System.out.println("producto: "+prod);
            ResultSet res = pstm.executeQuery();

            while (res.next()) {
              
                val = (res.getInt("cantidad"));
                System.out.println("el Stock que hay  es : " + val);
                if (cantidad > val) {

                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

        return true;
    }
    
    public void actuallizarStock(int cant,String pro){
         try {
            
            String consultatabla = "update  productos set   cantidad=cantidad-? where nombre=?";
            Connection conectar = Conexion.conectar();
            PreparedStatement pstm = conectar.prepareStatement(consultatabla);
            
         
            pstm.setInt(1, cant);
            pstm.setString(2, pro);
            

            pstm.executeUpdate();

           

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
    }; 
    
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
                        "select p.nombre,p.precio,p.cantidad,f.monto from clientes c ,productos p, facturas f,reg_fact_prod rfp where rfp.id_factura=f.id_factura and f.id_cliente=c.ID and p.ID_producto=rfp.id_producto and c.ID like ?");
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
        txt_Total = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        quitar = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        txt_fecha = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnimprimir = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        txt_Cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CantidadActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total");

        btnGuardar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar factura");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        quitar.setText("-");
        quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitarActionPerformed(evt);
            }
        });

        agregar.setText("+");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        jLabel9.setText("FECHA:");

        btnimprimir.setText("IMPRIMIR");
        btnimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(42, 42, 42)
                        .addComponent(btnimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txt_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(18, 18, 18)
                            .addComponent(jSelectproducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
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
                                        .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quitar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(156, 156, 156))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quitar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnimprimir))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        // tabDatosprod.setModel(db.getProducto2(seleccionprodu));
    }//GEN-LAST:event_jSelectproductoItemStateChanged

    private void jSelectproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSelectproductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        bandera=true;
        int idcliente = Integer.parseInt(txt_idcliente.getText());
        int valtotal2 = Integer.parseInt(txt_Total.getText());
        String fechafac = txt_fecha.getText();
        actuallizarStock(cantidad,seleccionproducto); 
        guardar_Factura(idcliente, fechafac, valtotal2);

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed

        int result = 0;
        cantidad = Integer.parseInt(txt_Cantidad.getText());
        seleccionproducto = jSelectproducto.getSelectedItem().toString();
        
        if (validarStock(cantidad, seleccionproducto)) {
            System.out.println("VALIDACION EXITOSA");
            
                  
            
        
            double precio = consultaPrecio(seleccionproducto);
            double valor_total = Math.round(cantidad * precio);
            LocalDate fecha = LocalDate.now();
            fecha2 = fecha.toString();
            txt_fecha.setText(fecha2);

            registroFactura(conteo, seleccionproducto, precio, cantidad, valor_total);
            int fil = tabDatosprod.getRowCount();
            System.out.println(".." + fil);
            for (int i = 0; i < fil; i++) {
                System.out.println("prueba 1");
                String a = tabDatosprod.getValueAt(i, 4) + "";
                System.out.println("Valor en fila " + i + ": " + a);
                double b = Double.parseDouble(a);
                result += b;

            }

            String resultado = result + "";
            txt_Total.setText(resultado);

            conteo++;
        } else {
            JOptionPane.showMessageDialog(null, "no hay suficientes productos");
        }


    }//GEN-LAST:event_agregarActionPerformed

    private void txt_idapellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idapellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idapellidoActionPerformed

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void txt_idclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idclienteKeyTyped
        Dbconexiones db = new Dbconexiones();
        String idcliente = txt_idcliente.getText();
        // jTDatoCliente.setModel(db.getcliente(idcliente));

        obtenerCliente(idcliente);
    }//GEN-LAST:event_txt_idclienteKeyTyped

    private void txt_idclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idclienteActionPerformed

    private void txt_CantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_CantidadActionPerformed

    private void quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitarActionPerformed
    int fila = tabDatosprod.getSelectedRow();
        
    if (fila >= 0) {
            tablafactura.removeRow(fila);
        }  else{
        JOptionPane.showMessageDialog(null,"Debe seleccionar una fila para eliminar");
    }// TODO add your handling code here:
    }//GEN-LAST:event_quitarActionPerformed

    private void btnimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirActionPerformed
        String nombre=txt_nombre.getText();
        int id=Integer.parseInt(txt_idcliente.getText());
        imprimirPdf3(id, nombre) ;       //        // TODO add your handling code here:
    }//GEN-LAST:event_btnimprimirActionPerformed
    private DefaultTableModel tablafactura;
    public int conteo = 1;
    public String fecha2;
    public double valtotal;
    boolean bandera=false;
      int cantidad;
      String seleccionproducto;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> jSelectproducto;
    private javax.swing.JButton quitar;
    private javax.swing.JTable tabDatosprod;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_Total;
    private javax.swing.JTextField txt_correo;
    private javax.swing.JLabel txt_fecha;
    private javax.swing.JTextField txt_idapellido;
    private javax.swing.JTextField txt_idcliente;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}
