package com.empresa.gestionproductos;

import java.util.Date;

public class Producto {
    private String codigoProducto;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidadProducto;
    private Date fechaVencimiento;

    public Producto(String codigoProducto, String nombreProducto, double precioUnitario, int cantidadProducto, Date fechaVencimiento) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidadProducto = cantidadProducto;
        this.fechaVencimiento = fechaVencimiento;
    }

    // Getters y setters
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidadProducto() { return cantidadProducto; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
}
