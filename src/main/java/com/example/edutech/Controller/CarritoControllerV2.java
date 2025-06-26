package com.example.edutech.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CarritoService;
import com.example.edutech.Service.CursoService;
import com.example.edutech.assemblers.CarritoItemModelAssembler;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; 

@RestController
@RequestMapping("/api/v2/carrito")
@CrossOrigin
@Tag(name = "Carrito", description = "Operaciones relacionadas con el carrito de compras")
public class CarritoControllerV2 {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CarritoItemModelAssembler carritoItemModelAssembler;

    @Operation(summary = "Punto de entrada del carrito", description = "Provee enlaces a las operaciones principales del carrito, indicando los parámetros necesarios.")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRootCarritoInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Bienvenido al servicio de Carrito de Compras. Aquí tienes las operaciones disponibles:");
        
        Map<String, Link> links = new HashMap<>();
        links.put("verCarrito", linkTo(methodOn(CarritoControllerV2.class).verCarrito(0)).withRel("verCarrito").withTitle("Ver el carrito de un usuario. Requiere parametro 'usuarioId'"));
        links.put("agregarCurso", linkTo(methodOn(CarritoControllerV2.class).agregarCurso(0, 0)).withRel("agregarCurso").withTitle("Agregar un curso al carrito. Requiere '{id}' (cursoId) y parametro 'usuarioId'"));
        links.put("eliminarItem", linkTo(methodOn(CarritoControllerV2.class).eliminarItem(0, 0)).withRel("eliminarItem").withTitle("Eliminar un curso del carrito. Requiere '{id}' (cursoId) y parametro 'usuarioId'"));
        links.put("actualizarCantidad", linkTo(methodOn(CarritoControllerV2.class).actualizarCantidad(0, 0, new HashMap<>())).withRel("actualizarCantidad").withTitle("Actualizar cantidad de un curso. Requiere '{id}' (cursoId), parametro 'usuarioId' y un cuerpo JSON con 'cantidad'"));
        links.put("vaciarCarrito", linkTo(methodOn(CarritoControllerV2.class).vaciarCarrito(0)).withRel("vaciarCarrito").withTitle("Vaciar el carrito de un usuario. Requiere parametro 'usuarioId'"));
        links.put("finalizarCompra", linkTo(methodOn(CarritoControllerV2.class).finalizarCompra(0)).withRel("finalizarCompra").withTitle("Finalizar la compra. Requiere parametro 'usuarioId'"));
        links.put("obtenerTotal", linkTo(methodOn(CarritoControllerV2.class).obtenerTotal(0)).withRel("obtenerTotal").withTitle("Obtener el total del carrito. Requiere parametro 'usuarioId'"));
        links.put("getSingleCarritoItem", linkTo(methodOn(CarritoControllerV2.class).getSingleCarritoItem(0,0)).withRel("getSingleCarritoItem").withTitle("Obtener un item específico del carrito. Requiere '{cursoId}' y parametro 'usuarioId'"));

        response.put("_links", links);

        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener un item específico del carrito", description = "Devuelve un item específico del carrito por su ID de curso y usuario")
    @GetMapping(value = "/items/{cursoId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CarritoItem>> getSingleCarritoItem(
            @PathVariable int cursoId,
            @RequestParam int usuarioId) {
        Optional<CarritoItem> itemOpt = carritoService.obtenerItems(usuarioId).stream()
                .filter(item -> item.getCursoId() == cursoId)
                .findFirst();

        if (itemOpt.isPresent()) {
            EntityModel<CarritoItem> itemModel = carritoItemModelAssembler.toModel(itemOpt.get());
            return ResponseEntity.ok(itemModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Finalizar compra", description = "Permite finalizar la compra de los cursos en el carrito del usuario")
    @PostMapping(value = "/finalizar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> finalizarCompra(@RequestParam int usuarioId) {
        try {
            List<CarritoItem> items = carritoService.obtenerItems(usuarioId);
            if (items.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "El carrito está vacío");
                response.put("total", 0.0);
                response.put("carrito", Collections.emptyList());
                return ResponseEntity.badRequest().body(response);
            }

            double total = carritoService.calcularTotal(usuarioId);
            carritoService.finalizarCompra(usuarioId);

            List<EntityModel<CarritoItem>> itemModels = items.stream()
                    .map(carritoItemModelAssembler::toModel)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Compra realizada con éxito. ¡Gracias por su compra!");
            response.put("total", total);
            response.put("carrito", itemModels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error: " + e.getMessage());
            response.put("total", 0.0);
            response.put("carrito", Collections.emptyList());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Agregar curso al carrito", description = "Permite agregar un curso al carrito de compras del usuario")
    @PostMapping(value = "/agregar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id, @RequestParam int usuarioId) {
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            carritoService.agregarCurso(curso, usuarioId);

            List<CarritoItem> items = carritoService.obtenerItems(usuarioId);

            List<EntityModel<CarritoItem>> itemModels = items.stream()
                    .map(carritoItemModelAssembler::toModel)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Curso agregado al carrito");
            response.put("carrito", itemModels);
            response.put("total", carritoService.calcularTotal(usuarioId));
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No hay cupos disponibles: " + e.getMessage());
            response.put("total", carritoService.calcularTotal(usuarioId));
            response.put("carrito", Collections.emptyList());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Ver carrito", description = "Permite ver los cursos agregados al carrito de compras del usuario")
    @GetMapping(value = "/ver", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> verCarrito(@RequestParam int usuarioId) {
        List<CarritoItem> items = carritoService.obtenerItems(usuarioId);
        double total = carritoService.calcularTotal(usuarioId);

        List<EntityModel<CarritoItem>> itemModels = items.stream()
                .map(carritoItemModelAssembler::toModel)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("items", itemModels);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar curso del carrito", description = "Permite eliminar un curso del carrito de compras del usuario")
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarItem(@PathVariable Integer id, @RequestParam int usuarioId) {
        carritoService.eliminarCurso(id, usuarioId);

        List<CarritoItem> items = carritoService.obtenerItems(usuarioId);

        List<EntityModel<CarritoItem>> itemModels = items.stream()
                .map(carritoItemModelAssembler::toModel)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso eliminado del carrito");
        response.put("carrito", itemModels);
        response.put("total", carritoService.calcularTotal(usuarioId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener total del carrito", description = "Permite obtener el total de los cursos en el carrito de compras del usuario")
    @GetMapping(value = "/total", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Double> obtenerTotal(@RequestParam int usuarioId) {
        return ResponseEntity.ok(carritoService.calcularTotal(usuarioId));
    }

    @Operation(summary = "Vaciar carrito", description = "Permite vaciar todos los cursos del carrito de compras del usuario")
    @DeleteMapping(value = "/vaciar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> vaciarCarrito(@RequestParam int usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Carrito vaciado correctamente");
        response.put("carrito", Collections.emptyList());
        response.put("total", 0.0);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar cantidad de curso en el carrito", description = "Permite actualizar la cantidad de un curso en el carrito de compras del usuario")
    @PutMapping(value = "/actualizar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Integer id,
            @RequestParam int usuarioId,
            @RequestBody Map<String, Integer> payload
    ) {
        int cantidad = payload.get("cantidad");
        carritoService.actualizarCantidad(id, cantidad, usuarioId);

        Optional<CarritoItem> updatedItemOpt = carritoService.obtenerItems(usuarioId)
                .stream()
                .filter(item -> item.getCursoId() == id)
                .findFirst();

        if (updatedItemOpt.isPresent()) {
            EntityModel<CarritoItem> itemModel = carritoItemModelAssembler.toModel(updatedItemOpt.get());
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Cantidad actualizada correctamente");
            response.put("item", itemModel);
            response.put("total", carritoService.calcularTotal(usuarioId));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
