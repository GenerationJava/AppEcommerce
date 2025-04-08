package com.generation.ecommerce.api;

import com.generation.ecommerce.dto.OrdenDTO;
import com.generation.ecommerce.exception.OrdenException;
import com.generation.ecommerce.exception.StockInsuficienteException;
import com.generation.ecommerce.service.OrdenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenRestController {

    //Inyectamos dependencia del servicio
    private final OrdenServiceImpl ordenService;

    //Endpoint para método post que lleva un DTO para la solicitud de la orden
    @PostMapping("/checkout")
    public ResponseEntity<?> saverOrden(@RequestBody OrdenDTO solicitudDTO) {
        try {
            System.out.println(solicitudDTO);
            OrdenDTO respuesta = ordenService.saveOrden(solicitudDTO);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (OrdenException e) {
            //Si sucede alguna excepcion de tipo orden, devuelve el mensaje de la excepción y un status de petición errónea
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (StockInsuficienteException e) {
            //Si sucede alguna excepcion de tipo stock, devuelve el mensaje de la excepción y un status de conflicto
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }


}
