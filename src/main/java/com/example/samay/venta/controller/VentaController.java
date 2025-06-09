package com.example.samay.venta.controller;

import com.example.samay.venta.service.VentaService;
import com.example.samay.venta.model.Venta;
import com.example.samay.ventaProducto.model.VentaProducto;
import com.example.samay.ventaProducto.service.VentaProductoService;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    private final VentaService ventaService;
    private final VentaProductoService ventaProductoService;

    @Autowired
    public VentaController(VentaService ventaService, VentaProductoService ventaProductoService) {
        this.ventaService = ventaService;
        this.ventaProductoService = ventaProductoService;
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.obtenerTodas();
    }

    @GetMapping("/buscar/{id}")
    public Venta obtenerPorId(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    @PostMapping("/agregarVenta")
    public ResponseEntity<Map<String, Object>> guardarVenta(@RequestBody Venta venta) {
        try {
            Venta ventaGuardada = ventaService.guardarVenta(venta);
            Map<String, Object> response = new HashMap<>();
            response.put("id", ventaGuardada.getVenta_id());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al guardar venta: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al guardar la venta: " + e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @PostMapping("/create-preference")
    public ResponseEntity<Map<String, String>> createPreference(@RequestBody Venta venta) {
        try {
            // Validar el usuario
            if (venta.getUsuario() == null || venta.getUsuario().getUsuario_id() == null) {
                logger.error("Usuario inválido en la solicitud de venta");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El campo usuario es requerido y debe incluir un usuario_id válido");
                return ResponseEntity.status(400).body(errorResponse);
            }

            // Validar el total de la venta
            if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Total de la venta inválido: {}", venta.getTotal());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El total de la venta debe ser mayor a cero");
                return ResponseEntity.status(400).body(errorResponse);
            }

            // Guardar la venta
            logger.info("Guardando venta para usuario_id: {}", venta.getUsuario().getUsuario_id());
            Venta ventaGuardada = ventaService.guardarVenta(venta);
            logger.info("Venta guardada con ID: {}", ventaGuardada.getVenta_id());

            // Obtener los productos asociados
            List<VentaProducto> ventaProductos = ventaProductoService.obtenerPorVentaId(ventaGuardada.getVenta_id());
            logger.info("Productos encontrados para venta_id {}: {}", ventaGuardada.getVenta_id(), ventaProductos.size());

            // Crear la preferencia de Mercado Pago
            PreferenceClient client = new PreferenceClient();
            List<PreferenceItemRequest> items = new ArrayList<>();

            // Iterar sobre los productos
            for (VentaProducto ventaProducto : ventaProductos) {
                if (ventaProducto.getProducto() == null || ventaProducto.getProducto().getProductName() == null || ventaProducto.getProducto().getProductName().trim().isEmpty()) {
                    logger.error("Producto inválido en VentaProducto ID: {}", ventaProducto.getVenta_producto_id());
                    throw new IllegalArgumentException("Producto inválido o nombre vacío en VentaProducto ID: " + ventaProducto.getVenta_producto_id());
                }
                if (ventaProducto.getPrecio_unitario() == null || ventaProducto.getPrecio_unitario().compareTo(BigDecimal.ZERO) <= 0) {
                    logger.error("Precio unitario inválido en VentaProducto ID: {}", ventaProducto.getVenta_producto_id());
                    throw new IllegalArgumentException("Precio unitario inválido en VentaProducto ID: " + ventaProducto.getVenta_producto_id());
                }
                if (ventaProducto.getCantidad() == null || ventaProducto.getCantidad() <= 0) {
                    logger.error("Cantidad inválida en VentaProducto ID: {}", ventaProducto.getVenta_producto_id());
                    throw new IllegalArgumentException("Cantidad inválida en VentaProducto ID: " + ventaProducto.getVenta_producto_id());
                }
                String itemTitle = ventaProducto.getProducto().getProductName().trim();
                if (itemTitle.length() > 256) {
                    itemTitle = itemTitle.substring(0, 256);
                    logger.warn("Título del producto truncado a 256 caracteres: {}", itemTitle);
                }
                // Convertir unitPrice a entero (sin decimales)
                BigDecimal unitPrice = ventaProducto.getPrecio_unitario().setScale(0, BigDecimal.ROUND_DOWN);
                PreferenceItemRequest item = PreferenceItemRequest.builder()
                        .title(itemTitle)
                        .quantity(ventaProducto.getCantidad())
                        .unitPrice(unitPrice)
                        .currencyId("COP")
                        .build();
                items.add(item);
                logger.debug("Ítem añadido: title={}, quantity={}, unitPrice={}", itemTitle, ventaProducto.getCantidad(), unitPrice);
            }

            // Si no hay productos, crear un ítem genérico
            if (items.isEmpty()) {
                logger.info("No se encontraron productos, usando ítem genérico para venta_id: {}", ventaGuardada.getVenta_id());
                String genericTitle = "Compra general ID: " + ventaGuardada.getVenta_id();
                if (genericTitle.length() > 256) {
                    genericTitle = genericTitle.substring(0, 256);
                    logger.warn("Título genérico truncado a 256 caracteres: {}", genericTitle);
                }
                // Convertir unitPrice a entero (sin decimales)
                BigDecimal unitPrice = venta.getTotal().setScale(0, BigDecimal.ROUND_DOWN);
                PreferenceItemRequest item = PreferenceItemRequest.builder()
                        .title(genericTitle)
                        .quantity(1)
                        .unitPrice(unitPrice)
                        .currencyId("COP")
                        .build();
                items.add(item);
                logger.debug("Ítem genérico añadido: title={}, quantity=1, unitPrice={}", genericTitle, unitPrice);
            }

            // Crear la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(PreferenceBackUrlsRequest.builder()
                            .success("www.google.com")
                            .pending("www.google.com")
                            .failure("www.google.com")
                            .build())
                    .autoReturn("approved")
                    .externalReference(ventaGuardada.getVenta_id().toString())
                    .build();


            logger.debug("PreferenceRequest creado: items={}, externalReference={}", items.size(), ventaGuardada.getVenta_id());
            Preference preference = client.create(preferenceRequest);
            logger.info("Preferencia creada con ID: {}", preference.getId());

            Map<String, String> response = new HashMap<>();
            response.put("preferenceId", preference.getId());
            response.put("initPoint", preference.getInitPoint());
            response.put("message", "Preferencia de pago creada correctamente");
            return ResponseEntity.ok(response);

        } catch (MPApiException e) {
            logger.error("Error de la API de Mercado Pago: {}. Status code: {}", e.getApiResponse().getContent(), e.getApiResponse().getStatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error de la API de Mercado Pago: " + e.getApiResponse().getContent());
            errorResponse.put("statusCode", String.valueOf(e.getApiResponse().getStatusCode()));
            return ResponseEntity.status(500).body(errorResponse);
        } catch (MPException e) {
            logger.error("Error de Mercado Pago: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error de Mercado Pago: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        } catch (IllegalArgumentException e) {
            logger.error("Error en los datos: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en los datos: " + e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error general al crear preferencia: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error general: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


      @PutMapping("/actualizarEstado/{id}")

    public ResponseEntity<String> actualizarDatos(@PathVariable Long id, @RequestBody Venta venta){
        ventaService.actualizarEstadoPago(id,venta);
        return ResponseEntity.ok("Actualizado correctamente");

    }


}