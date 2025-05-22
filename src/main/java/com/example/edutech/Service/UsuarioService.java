package com.example.edutech.Service;

import com.example.edutech.Model.Usuario;
import com.example.edutech.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Metodo para obtener todos los usuarios
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    //Metodo para buscar un usuario por id
    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepository.findById(id);
    }

    //Metodo para buscar un usuario por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    //Metodo para guardar un usuario
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    //Metodo para actualizar un usuario
    public Usuario actualizar(int id, Usuario usuarioActualizado) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuarioExistente = optionalUsuario.get();
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
            return usuarioRepository.save(usuarioExistente);
        } else {
            return null;
        }
    }

    //Metodo para eliminar un usuario
    public void eliminar(int id) {
        usuarioRepository.deleteById(id);
    }

    //Metodo que autentifica a los usuarios 
    public Optional<Usuario> autenticar(String email, String password){
        return usuarioRepository.findByEmail(email).filter(u -> u.getPassword().equals(password));
    }
}
