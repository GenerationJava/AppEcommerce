package com.generation.ecommerce.storage;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


@Service
public class GoogleCloudStorageService {

    private String bucketName = "ecommerce-backend";

    private String keyPath = "C:\\Users\\alesh\\OneDrive\\Escritorio\\Documentos\\Bootcamp - Generation\\Empleabilidad\\Proyectos\\GCP\\ecommerce-key.json";

    private final Storage storage;

    //Inicializamos la instancia del storage
    {
        try {
            //Acá se crea la instancia del storage tomando en cuenta las credenciales de la key.json
            storage = StorageOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath))).build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String updloadImagenProducto(MultipartFile archivoImagen) throws IOException {

        /*Validamos el tipo de archivo
        if(!archivoImagen.getContentType().startsWith("imagen/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen");
        }*/

        System.out.println(archivoImagen.getOriginalFilename());

        //Generar un nombre único para el archivo de manera dinámica
        String extension = FilenameUtils.getExtension(archivoImagen.getOriginalFilename());
        String nombreArchivo = "productos/" + UUID.randomUUID() + "." + extension;

        //Subir a Google Cloud Storage
        BlobId blobId = BlobId.of(bucketName, nombreArchivo);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(archivoImagen.getContentType())
                .build();
        storage.create(blobInfo, archivoImagen.getBytes());

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, nombreArchivo);
    }




}
