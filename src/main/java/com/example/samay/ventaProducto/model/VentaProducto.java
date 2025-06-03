package com.example.samay.ventaProducto.model;

import com.example.samay.producto.model.Producto;
import com.example.samay.venta.model.Venta;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class VentaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venta_producto_id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_unitario;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public VentaProducto() {
    }

    public VentaProducto(Long venta_producto_id, BigDecimal precio_unitario, Integer cantidad, Venta venta, Producto producto) {
        this.venta_producto_id = venta_producto_id;
        this.precio_unitario = precio_unitario;
        this.cantidad = cantidad;
        this.venta = venta;
        this.producto = producto;
    }

    public Long getVenta_producto_id() {
        return venta_producto_id;
    }

    public void setVenta_producto_id(Long venta_producto_id) {
        this.venta_producto_id = venta_producto_id;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
