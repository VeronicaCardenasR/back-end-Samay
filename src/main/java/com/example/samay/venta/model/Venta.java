package com.example.samay.venta.model;

import com.example.samay.usuario.model.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class    Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venta_id;

    @Column( nullable = false)
    private LocalDateTime fecha_venta = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column(nullable = false)
    private String pagoAprobado; // Nuevo campo

    @Column
    private String detalleEnvio;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Venta() {
    }

    public Venta(Long venta_id, LocalDateTime fecha_venta, BigDecimal total, Usuario usuario, String pagoAprobado,String detalleEnvio) {
        this.venta_id = venta_id;
        this.fecha_venta = fecha_venta;
        this.total = total;
        this.usuario = usuario;
        this.pagoAprobado = pagoAprobado;
        this.detalleEnvio = detalleEnvio;
    }


    public String getPagoAprobado() {
        return pagoAprobado;
    }

    public void setPagoAprobado(String pagoAprobado) {
        this.pagoAprobado = pagoAprobado;
    }

    public String getDetalleEnvio() {
        return detalleEnvio;
    }

    public void setDetalleEnvio(String detalleEnvio) {
        this.detalleEnvio = detalleEnvio;
    }

    public Long getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(Long venta_id) {
        this.venta_id = venta_id;
    }

    public LocalDateTime getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(LocalDateTime fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
