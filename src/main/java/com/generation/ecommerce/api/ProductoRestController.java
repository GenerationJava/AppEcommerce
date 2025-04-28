package com.generation.ecommerce.api;


import com.generation.ecommerce.dto.ProductoDTO;
import com.generation.ecommerce.model.Categoria;
import com.generation.ecommerce.model.ECategoria;
import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.service.CategoriaServiceImpl;
import com.generation.ecommerce.service.ProductoServiceImpl;
import com.generation.ecommerce.storage.GoogleCloudStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController//Anotación que indica que el controlador sigue patrón REST, es decir, define endpoints y entrega la respuesta en formato JSON
@RequiredArgsConstructor
@RequestMapping("/api/productos")//Ruta base
@Tag(name = "Controlador Rest de Producto", description = "Este controlador disponibiliza métodos de lectura, escritura y eliminación de productos")
@CrossOrigin("*")
public class ProductoRestController {

    //Inyección de dependencia
    private final ProductoServiceImpl productoService;
    private final GoogleCloudStorageService storageService;
    private final CategoriaServiceImpl categoriaService;


    //Creamos endpoints que permitan realizar el CRUD
    //Endpoint para buscar Producto por ID
    @Operation(summary = "Endpoint para buscar un producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado",
                content = @Content(schema = @Schema(implementation = Producto.class)))
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/producto/{id}")
    public Producto findById(@PathVariable Long id) {
        //@PathVariable es una anotación que permite recibir datos como variable a través de la URL
        return productoService.findById(id);
    }

    //Endpoint para buscar la lista de productos
    //Usamos la clase ResponseEntity, que permite manipular el cuerpo, la cabecera y status code de la respuesta HTTP
    @Operation(summary = "Endpoint para buscar la lista de productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos recuperada con éxito",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> findAllProducto() {
        //Al retornar un ResponseEntity, indicamos el status de respuesta y dentro el cuerpo de la respuesta HTTP
        return ResponseEntity.ok(productoService.findAllProducto());
    }

    //Endpoint para crear un nuevo producto
    @Operation(summary = "Este endpoint permite crear un nuevo producto", description = "Se crea un nuevo producto a través de un DTO y recibe también, una imagen como archivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                     content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del producto son inválidos")
    })
    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> saveProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO de Producto que será creado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductoDTO.class))
            )
            @RequestPart("producto") ProductoDTO nuevoProducto,
            @RequestPart("imagen") MultipartFile imagen) throws IOException {


        //Subimos imagen si esta existe
        String imagenUrl = null;
        if (!imagen.isEmpty()) {
            imagenUrl = storageService.updloadImagenProducto(imagen);
        }

        //Buscamos la categoria por el ID
        Categoria categoria = categoriaService.findById(nuevoProducto.getCategoriaId());


        //Convertimos el DTO a modelo
        Producto producto = toModel(nuevoProducto);
        //Le seteamos la url de la imagen
        producto.setImagenUrl(imagenUrl);
        nuevoProducto.setImagen(imagenUrl);
        //Le mandamos el modelo al service para que lo envíe al repo
        producto.setCategoria(categoria);
        System.out.println(nuevoProducto.getNombre());
        productoService.saveProducto(producto);

        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }


    //Endpoint para eliminar un producto por ID
    @Operation(summary = "Endpoint para eliminar un producto por ID", description = "Recibe un parámetro ID a tráves de la ruta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> deleteProductoById(
            @Parameter(description = "ID del producto a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        productoService.deleteProductoById(id);
        return new ResponseEntity<>("El producto ha sido eliminado", HttpStatus.NO_CONTENT);//Status 204
    }

    @Operation(summary = "Este endpoint permite editar un producto", description = "Recibe un ID de producto y lo actualiza a través de un DTO y recibe también, una imagen como archivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto editado exitosamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del producto son inválidos")
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<Producto> updateProductoById(
            @Parameter(description = "ID de producto a editar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO de Producto que será editado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class)))
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
    @Operation(summary = "Endpoint para buscar un producto por nombre", description = "Recibe un String del nombre del producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/producto")
    public ResponseEntity<Producto> findProductoByNombre(
            @Parameter(description = "Nombre de producto", example = "Camiseta de algodón", required = true)
            @RequestParam(name = "nombre") String nombre) {
        System.out.println(nombre);
        return new ResponseEntity<>(productoService.findProductoByNombre(nombre), HttpStatus.OK);
    }

    //Endpoint para buscar lista de productos por categoría
    @Operation(summary = "Endpoint para buscar la lista de productos por categoria", description = "Recibe un String del nombre de la categoría y entrega una lista de productos asociados")
    @ApiResponse(responseCode = "200", description = "Lista de productos recuperada con éxito",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping("/categoria")
    public ResponseEntity<List<Producto>> findAllProductoByCategoria(
            @Parameter(description = "Nombre de categoría", example = "MUJER", required = true)
            @RequestParam(name = "categoria") ECategoria categoria) {
        return new ResponseEntity<>(productoService.findAllProductoByCategoria(categoria), HttpStatus.OK);
    }

    //Endpoint para buscar lista de productos por rango de precio
    @Operation(summary = "Endpoint para buscar la lista de productos, por rango de precio", description = "Recibe dos enteros para indicar el precio máximo y mínimo")
    @ApiResponse(responseCode = "200", description = "Lista de productos recuperada con éxito",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping("/por-precio")
    public ResponseEntity<List<Producto>> findAllProductoByRangoPrecio(
            @Parameter(name = "min", description = "Precio mínimo del rango", example = "10000.00", required = true)
            @RequestParam(name = "min") Double min,
            @Parameter(name = "max", description = "Precio máximo del rango", example = "50000.00", required = true)@RequestParam(name = "max") Double max) {
        return new ResponseEntity<>(productoService.findAllProductoByRangoPrecio(min, max), HttpStatus.OK);
    }

    //Método que mapea la conversión de un DTO a Modelo Producto
    public Producto toModel(ProductoDTO nuevoProducto) {
        Producto producto = new Producto();
        producto.setStock(nuevoProducto.getStock());
        producto.setPrecio(nuevoProducto.getPrecio());
        producto.setNombre(nuevoProducto.getNombre());
        return producto;
    }

}
