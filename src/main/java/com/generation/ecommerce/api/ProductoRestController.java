package com.generation.ecommerce.api;

import com.generation.ecommerce.model.ECategoria;
import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.service.ProductoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//Anotación que indica que el controlador sigue patrón REST, es decir, define endpoints y entrega la respuesta en formato JSON
@RequiredArgsConstructor
@RequestMapping("/api/productos")//Ruta base
public class ProductoRestController {

    //Inyección de dependencia
    private final ProductoServiceImpl productoService;

    //Creamos endpoints que permitan realizar el CRUD
    //Endpoint para buscar Producto por ID
    @GetMapping("/producto/{id}")
    public Producto findById(@PathVariable Long id) {
        //@PathVariable es una anotación que permite recibir datos como variable a través de la URL
        return productoService.findById(id);
    }

    //Endpoint para buscar la lista de productos
    //Usamos la clase ResponseEntity, que permite manipular el cuerpo, la cabecera y status code de la respuesta HTTP
    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> findAllProducto() {
        //Al retornar un ResponseEntity, indicamos el status de respuesta y dentro el cuerpo de la respuesta HTTP
        return ResponseEntity.ok(productoService.findAllProducto());
    }

    //Endpoint para crear un nuevo producto
    @PostMapping("/nuevo")
    public ResponseEntity<Producto> saveProducto(@RequestBody Producto nuevoProducto) {
        return new ResponseEntity<>(productoService.saveProducto(nuevoProducto), HttpStatus.CREATED);
    }

    //Endpoint para eliminar un producto por ID
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> deleteProductoById(@PathVariable Long id) {
        productoService.deleteProductoById(id);
        return new ResponseEntity<>("El producto ha sido eliminado", HttpStatus.OK);//Status 200
    }

    //Endpoint para editar un producto por ID
    @PutMapping("/editar/{id}")
    public ResponseEntity<Producto> updateProductoById(@PathVariable Long id,
                                                       @RequestBody Producto productoEditado) {
        Producto productoSeleccionado = productoService.findById(id);
        //Le seteamos al producto seleccionado, los valores de el prroducto editado por el usuario
        productoSeleccionado.setNombre(productoEditado.getNombre());
        productoSeleccionado.setPrecio(productoEditado.getPrecio());
        productoSeleccionado.setStock(productoEditado.getStock());
        productoSeleccionado.setCategoria(productoEditado.getCategoria());
        productoService.saveProducto(productoSeleccionado);

        return new ResponseEntity<>(productoSeleccionado, HttpStatus.OK);

    }

    //Métodos más específicos
    //Endpoint par buscar producto por nombre
    //Endpoint trabaja conn un parámetro de consulta
    @GetMapping("/producto")
    public ResponseEntity<Producto> findProductoByNombre(@RequestParam(name = "nombre") String nombre) {
        System.out.println(nombre);
        return new ResponseEntity<>(productoService.findProductoByNombre(nombre), HttpStatus.OK);
    }

    //Endpoint para buscar lista de productos por categoría
    @GetMapping("/categoria")
    public ResponseEntity<List<Producto>> findAllProductoByCategoria(@RequestParam(name = "categoria") ECategoria categoria) {

        return new ResponseEntity<>(productoService.findAllProductoByCategoria(categoria), HttpStatus.OK);
    }

    //Endpoint para buscar lista de productos por rango de precio
    @GetMapping("/por-precio")
    public ResponseEntity<List<Producto>> findAllProductoByRangoPrecio(@RequestParam(name = "min") Double min,
                                                                       @RequestParam(name = "max") Double max) {
        return new ResponseEntity<>(productoService.findAllProductoByRangoPrecio(min, max), HttpStatus.OK);
    }






}
