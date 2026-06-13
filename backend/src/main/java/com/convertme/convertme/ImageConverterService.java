package com.convertme.convertme;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service  // Le dice a Spring que esta clase es un "servicio"
public class ImageConverterService {

    public byte[] convertImage(MultipartFile file, String targetFormat) throws IOException {

        // 1. Leer la imagen que nos llega
        BufferedImage imagen = ImageIO.read(file.getInputStream());

        if (imagen == null) {
            throw new IOException("No se pudo leer la imagen");
        }

        // 2. JPG no soporta transparencia, así que convertimos a fondo blanco
        if (targetFormat.equalsIgnoreCase("jpg") || targetFormat.equalsIgnoreCase("jpeg")) {
            BufferedImage sinTransparencia = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g = sinTransparencia.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
            g.drawImage(imagen, 0, 0, null);
            g.dispose();
            imagen = sinTransparencia;
        }

        // 3. Escribir la imagen en el nuevo formato
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(imagen, targetFormat, output);

        return output.toByteArray(); 
    }
}