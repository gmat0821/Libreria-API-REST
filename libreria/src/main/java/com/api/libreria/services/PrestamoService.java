package com.api.libreria.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class PrestamoService {

    private final IPrestamoRepository prestamoRepository;
    private final ILibroRepository libroRepository;

    public PrestamoService(IPrestamoRepository prestamoRepository, ILibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
    }
    
    public ArrayList<PrestamoModel> obtenerPrestamos() {
        return (ArrayList<PrestamoModel>) prestamoRepository.findAll();
    }

    public Optional<PrestamoModel> obtenerPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public PrestamoModel registrarPrestamo(PrestamoModel prestamo) {
        // verificar inventario
        LibroModel libro = libroRepository.findById(prestamo.getLibro().getIsbn())
                .orElseThrow(() -> new RuntimeException("El libro especificado no existe."));
        if (libro.getNumeroEjemplares() <= 0) {
            throw new RuntimeException("No hay ejemplares disponibles para realizar el préstamo.");
        }

        // descontamos un ejemplar y guardamos el libro actualizado
        libro.setNumeroEjemplares(libro.getNumeroEjemplares() - 1);
        libroRepository.save(libro);

        // guardamos y retornamos el préstamo
        return prestamoRepository.save(prestamo);
    }

    public PrestamoModel registrarDevolucion(Long id) {
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El préstamo no existe."));

        if (prestamo.getFechaDevolucion() != null) {
            throw new RuntimeException("Este préstamo ya fue devuelto previamente.");
        }

        // ponemos la fecha actual de devolución
        prestamo.setFechaDevolucion(LocalDateTime.now());

        // devolvemos el libro al inventario
        LibroModel libro = prestamo.getLibro();
        libro.setNumeroEjemplares(libro.getNumeroEjemplares() + 1);
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    public boolean eliminarPrestamo(Long id) {
        try {
            prestamoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}