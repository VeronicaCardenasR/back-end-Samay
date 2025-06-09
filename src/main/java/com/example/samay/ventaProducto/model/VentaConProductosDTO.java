package com.example.samay.ventaProducto.model;

import java.math.BigDecimal;
import java.util.List;

public class VentaConProductosDTO {
    private Long ventaId;
    private String detalleEnvio; // Nuevo campo
    private List<ProductoCantidadDTO> productos;

    public static class ProductoCantidadDTO {
        private Long productoId;
        private BigDecimal precioUnitario;
        private Integer cantidad;

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public String getDetalleEnvio() {
        return detalleEnvio;
    }

    public void setDetalleEnvio(String detalleEnvio) {
        this.detalleEnvio = detalleEnvio;
    }

    public List<ProductoCantidadDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCantidadDTO> productos) {
        this.productos = productos;
    }
}
