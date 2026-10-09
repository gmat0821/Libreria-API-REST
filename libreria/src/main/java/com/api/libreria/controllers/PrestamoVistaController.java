package com.api.libreria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.LibroService;
import com.api.libreria.services.PrestamoService;
import com.api.libreria.services.UsuarioService;

@Controller
public class PrestamoVistaController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public PrestamoVistaController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/vista/prestamos")
    public String mostrarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerPrestamos());
        model.addAttribute("libros", libroService.obtenerLibros());
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());

        if (!model.containsAttribute("prestamo")) {
            model.addAttribute("prestamo", new PrestamoModel());
        }
        return "prestamos";
    }

    @PostMapping("/vista/prestamos/guardar")
    public String guardarPrestamo(@ModelAttribute PrestamoModel prestamo, RedirectAttributes atributos) {
        try {
            prestamoService.registrarPrestamo(prestamo);
            atributos.addFlashAttribute("mensaje", "Préstamo registrado correctamente.");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/devolver")
    public String registrarDevolucion(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            prestamoService.registrarDevolucion(id);
            atributos.addFlashAttribute("mensaje", "Libro devuelto correctamente.");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/eliminar")
    public String eliminarPrestamo(@PathVariable Long id, RedirectAttributes atributos) {
        if (prestamoService.eliminarPrestamo(id)) {
            atributos.addFlashAttribute("mensaje", "Registro de préstamo eliminado.");
        } else {
            atributos.addFlashAttribute("error", "No se pudo eliminar el préstamo.");
        }
        return "redirect:/vista/prestamos";
    }
}