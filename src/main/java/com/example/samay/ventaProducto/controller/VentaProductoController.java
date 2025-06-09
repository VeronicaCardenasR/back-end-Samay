package com.example.samay.ventaProducto.controller;

import com.example.samay.ventaProducto.model.VentaConProductosDTO;
import com.example.samay.ventaProducto.service.VentaProductoService;
import com.example.samay.ventaProducto.model.VentaProducto;
import com.example.samay.venta.model.Venta;
import com.example.samay.venta.service.VentaService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/venta-productos")
@CrossOrigin(origins = "*")
public class VentaProductoController {

    private static final Logger logger = LoggerFactory.getLogger(VentaProductoController.class);

    private final VentaProductoService ventaProductoService;
    private final VentaService ventaService;

    @Autowired
    public VentaProductoController(VentaProductoService ventaProductoService, VentaService ventaService) {
        this.ventaProductoService = ventaProductoService;
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<VentaProducto> listarVentas() {
        return ventaProductoService.obtenerTodas();
    }

    @GetMapping("/buscar/{id}")
    public VentaProducto obtenerPorId(@PathVariable Long id) {
        return ventaProductoService.obtenerPorId(id);
    }

    @PostMapping("/agregarVentaProducto")
    public ResponseEntity<Map<String, String>> guardarVentaProducto(@RequestBody VentaProducto ventaProducto) {
        try {
            ventaProductoService.guardarVentaProducto(ventaProducto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto agregado a la venta correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al guardar venta-producto: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/agregar-multiples")
    public ResponseEntity<Map<String, String>> agregarProductosAVenta(@RequestBody VentaConProductosDTO dto) {
        try {
            logger.info("Recibiendo solicitud para agregar múltiples productos: ventaId={}", dto.getVentaId());

            // Guardar los productos
            ventaProductoService.guardarVentaConProductos(dto);
            logger.info("Productos guardados para ventaId={}", dto.getVentaId());

            // Obtener la venta asociada
            Venta venta = ventaService.obtenerPorId(dto.getVentaId());
            if (venta == null) {
                logger.error("Venta no encontrada: ventaId={}", dto.getVentaId());
                throw new IllegalArgumentException("Venta no encontrada con ID: " + dto.getVentaId());
            }

            // Actualizar detalleEnvio si se proporciona
            if (dto.getDetalleEnvio() != null && !dto.getDetalleEnvio().isEmpty()) {
                venta.setDetalleEnvio(dto.getDetalleEnvio());
                ventaService.guardarVenta(venta);
            }

            // Obtener los productos asociados
            List<VentaProducto> ventaProductos = ventaProductoService.obtenerPorVentaId(dto.getVentaId());
            logger.info("Productos encontrados para ventaId {}: {}", dto.getVentaId(), ventaProductos.size());

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
                logger.info("No se encontraron productos, usando ítem genérico para ventaId: {}", dto.getVentaId());
                String genericTitle = "Compra general ID: " + dto.getVentaId();
                if (genericTitle.length() > 256) {
                    genericTitle = genericTitle.substring(0, 256);
                    logger.warn("Título genérico truncado a 256 caracteres: {}", genericTitle);
                }
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
                    .externalReference(dto.getVentaId().toString())
                    .notificationUrl("https://754a-2800-486-1080-1700-c93-3f8-e5c0-8710.ngrok-free.app/venta-productos/webhooks")
                    .build();

            logger.debug("PreferenceRequest creado: items={}, externalReference={}", items.size(), dto.getVentaId());
            Preference preference = client.create(preferenceRequest);
            logger.info("Preferencia creada con ID: {}", preference.getId());

            // Devolver respuesta con preferenceId
            Map<String, String> response = new HashMap<>();
            response.put("message", "Productos agregados a la venta correctamente");
            response.put("preferenceId", preference.getId());
            return ResponseEntity.ok(response);

        } catch (MPApiException e) {
            logger.error("Error de la API de Mercado Pago: {}. Status code: {}", e.getApiResponse().getContent(), e.getApiResponse().getStatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error de la API de Mercado Pago: " + e.getApiResponse().getContent());
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
            logger.error("Error general al procesar la venta: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error general: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/venta/{ventaId}")
    public List<VentaProducto> obtenerPorVenta(@PathVariable Long ventaId) {
        return ventaProductoService.obtenerPorVentaId(ventaId);
    }
