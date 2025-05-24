package com.example.edutech.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edutech.Repository.NotificacionRepository;
import com.example.edutech.Model.Notificacion;


@Service
public class NotificacionService {
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    public NotificacionService(NotificacionRepository notificacionRepository){
        this.notificacionRepository = notificacionRepository;
    }

    public Notificacion crear(Notificacion notificacion){
        return notificacionRepository.save(notificacion);
    }
}
