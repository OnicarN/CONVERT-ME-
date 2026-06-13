package com.convertme.convertme;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Files;

@Service
public class AudioConverterService {

    public byte[] convertAudio(MultipartFile file, String targetFormat) throws Exception {

        // 1. Guardamos el archivo en un fichero temporal
        //    JAVE2 necesita ficheros reales, no streams
        String extension = getExtension(file.getOriginalFilename());
        File inputFile = File.createTempFile("audio-input", "." + extension);
        file.transferTo(inputFile);

        // 2. Fichero temporal para el resultado
        File outputFile = File.createTempFile("audio-output", "." + targetFormat);

        try {
            // 3. Configuramos el codec según el formato destino
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec(getCodec(targetFormat));

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat(targetFormat);
            attrs.setAudioAttributes(audio);

            // 4. Convertimos
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, attrs);

            // 5. Devolvemos los bytes del archivo convertido
            return Files.readAllBytes(outputFile.toPath());

        } finally {
            // 6. Borramos los temporales siempre, aunque falle
            inputFile.delete();
            outputFile.delete();
        }
    }

    // Cada formato usa un codec distinto
    private String getCodec(String format) {
        return switch (format.toLowerCase()) {
            case "mp3"  -> "libmp3lame";
            case "ogg"  -> "libvorbis";
            case "wav"  -> "pcm_s16le";
            case "aac"  -> "aac";
            case "flac" -> "flac";
            default -> throw new IllegalArgumentException("Formato no soportado: " + format);
        };
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "tmp";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public String getMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "mp3"  -> "audio/mpeg";
            case "ogg"  -> "audio/ogg";
            case "wav"  -> "audio/wav";
            case "aac"  -> "audio/aac";
            case "flac" -> "audio/flac";
            default     -> "application/octet-stream";
        };
    }
}