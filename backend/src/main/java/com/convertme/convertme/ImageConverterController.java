package com.convertme.convertme;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageConverterController {

    private final ImageConverterService imageConverterService;
    private final AudioConverterService audioConverterService;
    private final VideoConverterService videoConverterService;

    public ImageConverterController(ImageConverterService imageConverterService,
                                    AudioConverterService audioConverterService,
                                    VideoConverterService videoConverterService) {
        this.imageConverterService = imageConverterService;
        this.audioConverterService = audioConverterService;
        this.videoConverterService = videoConverterService;
    }

    // ── Imágenes ──────────────────────────────────────────────
    @PostMapping("/convert/image")
    public ResponseEntity<byte[]> convertImage(
            @RequestParam("file")   MultipartFile file,
            @RequestParam("format") String targetFormat) {
        try {
            byte[] resultado = imageConverterService.convertImage(file, targetFormat);
            String nombreFinal = getNombreBase(file) + "." + targetFormat.toLowerCase();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreFinal + "\"")
                    .body(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ── Audio ─────────────────────────────────────────────────
    @PostMapping("/convert/audio")
    public ResponseEntity<byte[]> convertAudio(
            @RequestParam("file")   MultipartFile file,
            @RequestParam("format") String targetFormat) {
        try {
            byte[] resultado = audioConverterService.convertAudio(file, targetFormat);
            String mime = audioConverterService.getMimeType(targetFormat);
            String nombreFinal = getNombreBase(file) + "." + targetFormat.toLowerCase();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mime))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreFinal + "\"")
                    .body(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ── Vídeo ─────────────────────────────────────────────────
    @PostMapping("/convert/video")
    public ResponseEntity<byte[]> convertVideo(
            @RequestParam("file")   MultipartFile file,
            @RequestParam("format") String targetFormat) {
        try {
            byte[] resultado = videoConverterService.convertVideo(file, targetFormat);
            String mime = videoConverterService.getMimeType(targetFormat);
            String nombreFinal = getNombreBase(file) + "." + targetFormat.toLowerCase();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mime))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreFinal + "\"")
                    .body(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getNombreBase(MultipartFile file) {
        String nombre = file.getOriginalFilename();
        if (nombre == null || !nombre.contains(".")) return "archivo";
        return nombre.substring(0, nombre.lastIndexOf('.'));
    }
}