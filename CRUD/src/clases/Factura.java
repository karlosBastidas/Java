/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.Date;

/**
 *
 * @author Carlos
 */
public class Factura {
    
    private int id_factura;
    private Date fecha;

    public Factura(int id_factura, Date fecha) {
        this.id_factura = id_factura;
        this.fecha = fecha;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Factura{" + "id_factura=" + id_factura + ", fecha=" + fecha + '}';
    }
    
}
