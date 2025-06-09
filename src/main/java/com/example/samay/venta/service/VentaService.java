package com.example.samay.venta.service;

import com.example.samay.usuario.model.Usuario;
import com.example.samay.usuario.repository.IusuarioRepository;
import com.example.samay.venta.model.Venta;
import com.example.samay.venta.repository.IventaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService implements IventaService {

    private final IventaRepository ventaRepository;
    private final IusuarioRepository usuarioRepository;

    @Autowired
    public VentaService(IventaRepository ventaRepository, IusuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Venta> obtenerTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta guardarVenta(Venta venta) {
        // Validar y asignar el usuario
        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getUsuario_id())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        venta.setUsuario(usuario);

        // Inicializar pagoAprobado como "pendiente" si no está seteado

            venta.setPagoAprobado("Aprobado");


        // Guardar la venta
        Venta ventaGuardada = ventaRepository.save(venta);
        return ventaGuardada;
    }

    @Override
    public void actualizarEstadoPago(Long ventaId, Venta estado) {
        Venta venta = ventaRepository.findById(ventaId).orElse(null);

        venta.setDetalleEnvio(estado.getDetalleEnvio());

        ventaRepository.save(venta);

    }

