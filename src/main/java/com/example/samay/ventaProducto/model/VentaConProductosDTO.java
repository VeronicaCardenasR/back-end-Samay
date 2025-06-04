package com.example.samay.ventaProducto.model;

import java.math.BigDecimal;
import java.util.List;

public class VentaConProductosDTO {

    private Long ventaId;

    private List<ProductoCantidadDTO> productos;

    // Getters y setters

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public List<ProductoCantidadDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCantidadDTO> productos) {
        this.productos = productos;
    }

    public static class ProductoCantidadDTO {
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        // Getters y setters

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
}
