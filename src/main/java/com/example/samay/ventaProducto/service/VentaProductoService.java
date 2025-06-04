package com.example.samay.ventaProducto.service;

import com.example.samay.producto.model.Producto;
import com.example.samay.venta.model.Venta;
import com.example.samay.producto.repository.IproductoRepository;
import com.example.samay.venta.repository.IventaRepository;
import com.example.samay.ventaProducto.model.VentaConProductosDTO;
import com.example.samay.ventaProducto.model.VentaProducto;
import com.example.samay.ventaProducto.repository.IventaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaProductoService implements IventaProductoService {

    private final IventaProductoRepository ventaProductoRepository;
    private final IventaRepository ventaRepository;
    private final IproductoRepository productoRepository;

    @Autowired
    public VentaProductoService(IventaProductoRepository ventaProductoRepository, IventaRepository ventaRepository, IproductoRepository productoRepository) {
        this.ventaProductoRepository = ventaProductoRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<VentaProducto> obtenerTodas() {
        return ventaProductoRepository.findAll();
    }

    @Override
    public VentaProducto obtenerPorId(Long id) {
        return ventaProductoRepository.findById(id).orElse(null);
    }

    @Override
    public void guardarVentaProducto(VentaProducto ventaProducto) {

        Venta venta = ventaRepository.findById(ventaProducto.getVenta().getVenta_id())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Producto producto = productoRepository.findById(ventaProducto.getProducto().getProducto_id())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int stockDisponible = producto.getQuanty();
        int cantidadSolicitada = ventaProducto.getCantidad();

        if (cantidadSolicitada <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        if (stockDisponible < cantidadSolicitada) {
            throw new RuntimeException("Stock insuficiente: disponible " + stockDisponible);
        }

        // Asignar relaciones reales
        ventaProducto.setVenta(venta);
        ventaProducto.setProducto(producto);

        // Guardar venta-producto
        ventaProductoRepository.save(ventaProducto);

        // Actualizar stock del producto
        producto.setQuanty(stockDisponible - cantidadSolicitada);
        productoRepository.save(producto);
    }


    public void guardarVentaConProductos(VentaConProductosDTO dto) {
        Venta venta = ventaRepository.findById(dto.getVentaId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        for (VentaConProductosDTO.ProductoCantidadDTO prodDto : dto.getProductos()) {
            Producto producto = productoRepository.findById(prodDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + prodDto.getProductoId()));

            if (prodDto.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a cero");
            }

            if (producto.getQuanty() < prodDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto: " + prodDto.getProductoId());
            }

            VentaProducto ventaProducto = new VentaProducto();
            ventaProducto.setVenta(venta);
            ventaProducto.setProducto(producto);
            ventaProducto.setCantidad(prodDto.getCantidad());
            ventaProducto.setPrecio_unitario(prodDto.getPrecioUnitario());

            ventaProductoRepository.save(ventaProducto);

            producto.setQuanty(producto.getQuanty() - prodDto.getCantidad());
            productoRepository.save(producto);
        }
    }

}

