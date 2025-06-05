package com.example.edutech.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;
import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Repository.CarritoRepository;
import com.example.edutech.Repository.HistorialCompraRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CursoService cursoService;
    private final HistorialCompraService historialCompraService;
    private final HistorialCompraRepository historialCompraRepository;
    @Autowired
    public CarritoService(CarritoRepository carritoRepository, 
                         CursoService cursoService,
                         HistorialCompraService historialCompraService,
                         HistorialCompraRepository historialCompraRepository) {
        this.carritoRepository = carritoRepository;
        this.cursoService = cursoService;
        this.historialCompraService = historialCompraService;
        this.historialCompraRepository = historialCompraRepository;
        }
    
    

    
    public List<CarritoItem> obtenerItems(int usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public double calcularTotal(int usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId).stream()
            .mapToDouble(CarritoItem::getSubtotal)
            .sum();
    }

    @Transactional
    public void agregarCurso(Curso curso, int usuarioId) {
        
        int cursoId = curso.getId();
        Curso cursoActual = cursoService.buscarPorId(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        Optional<CarritoItem> existente = carritoRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId);
        int cantidadActual = existente.map(CarritoItem::getCantidad).orElse(0);

        if (cantidadActual >= cursoActual.getCupos()) {
            throw new IllegalStateException("No hay suficientes cupos disponibles");
        }

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            item.setCantidad(cantidadActual + 1);
            item.setSubtotal(item.getCantidad() * item.getPrecio());
            carritoRepository.save(item);
        } else {
            CarritoItem nuevo = new CarritoItem(cursoId, curso.getNombre(), 1, curso.getPrecio());
            nuevo.setUsuarioId(usuarioId);
            nuevo.setSubtotal(curso.getPrecio());
            carritoRepository.save(nuevo);
        }

        cursoActual.setCupos(cursoActual.getCupos() - 1);
        cursoService.guardarCurso(cursoActual);
    }

    @Transactional
    public void eliminarCurso(int cursoId, int usuarioId) {
        Optional<CarritoItem> itemOpt = carritoRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId);
        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();

            // Devolver cupo
            Curso curso = cursoService.buscarPorId(cursoId).orElse(null);
            if (curso != null) {
                curso.setCupos(curso.getCupos() + item.getCantidad());
                cursoService.guardarCurso(curso);
            }

            carritoRepository.delete(item);
        }
    }

    @Transactional
    public void actualizarCantidad(int cursoId, int nuevaCantidad, int usuarioId) {
        CarritoItem item = carritoRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
            .orElseThrow(() -> new NoSuchElementException("El curso no está en el carrito"));

        Curso curso = cursoService.buscarPorId(cursoId)
            .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        int diferencia = nuevaCantidad - item.getCantidad();

        if (diferencia > 0 && diferencia > curso.getCupos()) {
            throw new IllegalStateException("No hay suficientes cupos disponibles");
        }

        curso.setCupos(curso.getCupos() - diferencia);
        cursoService.guardarCurso(curso);

        item.setCantidad(nuevaCantidad);
        item.setSubtotal(nuevaCantidad * item.getPrecio());
        carritoRepository.save(item);
    }

    @Transactional
    public void vaciarCarrito(int usuarioId) {
        // Implementación existente se mantiene igual
        List<CarritoItem> items = carritoRepository.findByUsuarioId(usuarioId);
        for (CarritoItem item : items) {
            Curso curso = cursoService.buscarPorId(item.getCursoId()).orElse(null);
            if (curso != null) {
                curso.setCupos(curso.getCupos() + item.getCantidad());
                cursoService.guardarCurso(curso);
            }
        }
            carritoRepository.deleteByUsuarioId(usuarioId);
        }
    
    

    // Método modificado para incluir historial
    @Transactional
    public void finalizarCompra(int usuarioId) {
        List<CarritoItem> items = carritoRepository.findByUsuarioId(usuarioId);
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // Validar stock antes de proceder
        for (CarritoItem item : items) {
            Curso curso = cursoService.buscarPorId(item.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
            if (curso.getCupos() < item.getCantidad()) {
                throw new IllegalStateException("No hay suficientes cupos para: " + curso.getNombre());
            }
        }

        // Convertir items del carrito a historial de compras
        List<HistorialCompra> compras = items.stream()
            .map(item -> new HistorialCompra(
                null,
                usuarioId,
                item.getCursoId(),
                item.getNombre(),
                item.getPrecio(),
                item.getCantidad(),
                item.getSubtotal(),
                null
            ))
            .collect(Collectors.toList());

        // Guardar en historial
        historialCompraService.guardarCompras(compras);

        // Vaciar carrito
        carritoRepository.deleteByUsuarioId(usuarioId);
    }

}