package com.convertme.convertme;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.nio.file.Files;

@Service
public class VideoConverterService {

    public byte[] convertVideo(MultipartFile file, String targetFormat) throws Exception {

        // 1. Guardamos el archivo subido en un fichero temporal
        String extension = getExtension(file.getOriginalFilename());
        File inputFile = File.createTempFile("video-input", "." + extension);
        file.transferTo(inputFile);

        // 2. Fichero temporal para el resultado
        File outputFile = File.createTempFile("video-output", "." + targetFormat);

        try {
            // 3. Configuramos audio y video del archivo de salida
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec(getAudioCodec(targetFormat));

            VideoAttributes video = new VideoAttributes();
            video.setCodec(getVideoCodec(targetFormat));

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat(targetFormat);
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            // 4. Convertimos
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, attrs);

            // 5. Devolvemos los bytes
            return Files.readAllBytes(outputFile.toPath());

        } finally {
            // 6. Limpiamos siempre
            inputFile.delete();
            outputFile.delete();
        }
    }

    // Codec de video según formato
    private String getVideoCodec(String format) {
        return switch (format.toLowerCase()) {
            case "mp4"  -> "libx264";
            case "avi"  -> "mpeg4";
            case "mov"  -> "libx264";
            case "mkv"  -> "libx264";
            case "webm" -> "libvpx";
            default -> throw new IllegalArgumentException("Formato no soportado: " + format);
        };
    }

    // Codec de audio según formato
    private String getAudioCodec(String format) {
        return switch (format.toLowerCase()) {
            case "mp4", "mov", "mkv" -> "aac";
            case "avi"               -> "mp3";
            case "webm"              -> "libvorbis";
            default -> throw new IllegalArgumentException("Formato no soportado: " + format);
        };
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "tmp";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public String getMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "mp4"  -> "video/mp4";
            case "avi"  -> "video/x-msvideo";
            case "mov"  -> "video/quicktime";
            case "mkv"  -> "video/x-matroska";
            case "webm" -> "video/webm";
            default     -> "application/octet-stream";
        };
    }
}