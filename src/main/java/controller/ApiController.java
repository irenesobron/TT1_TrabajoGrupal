package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.ProblemDetails;
import com.ejemplo.EmailResponse;
import com.ejemplo.ResultsResponse;
import com.ejemplo.Solicitud;
import com.ejemplo.SolicitudResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

@RestController
@RequestMapping("")
public class ApiController {

    private final Map<String, List<Integer>> tokensPorUsuario = new ConcurrentHashMap<>();
    private final Map<Integer, String> resultadosPorToken = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private String generateGridData(int token, int size) {
        try {
            StringBuilder data = new StringBuilder();
            data.append(size).append("\n");

            String[] colors = {"blue", "green", "yellow", "purple"};
            Random tokenRandom = new Random(token);
            int numEntities = Math.min(size * 2, 20);

            int[] ex        = new int[numEntities];
            int[] ey        = new int[numEntities];
            int[] dx        = new int[numEntities];
            int[] dy        = new int[numEntities];
            String[] ecolor = new String[numEntities];

            // Direcciones ortogonales para los rojos: arriba, abajo, izq, der
            int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

            for (int i = 0; i < numEntities; i++) {
                ex[i]     = tokenRandom.nextInt(size);
                ey[i]     = tokenRandom.nextInt(size);

                // Los primeros 4 son rojos, el resto colores estáticos
                if (i < 4) {
                    ecolor[i] = "red";
                    int[] dir = dirs[tokenRandom.nextInt(4)];
                    dx[i] = dir[0];
                    dy[i] = dir[1];
                } else {
                    ecolor[i] = colors[i % colors.length];
                    dx[i] = 0; // estáticos
                    dy[i] = 0;
                }
            }

            int maxTime = 10;

            for (int t = 0; t < maxTime; t++) {

                // Solo mover las entidades rojas
                if (t > 0) {
                    for (int i = 0; i < numEntities; i++) {
                        if ("red".equals(ecolor[i])) {
                            ex[i] = (ex[i] + dx[i] + size) % size;
                            ey[i] = (ey[i] + dy[i] + size) % size;
                        }
                    }
                }

                // Colisiones: un rojo convierte a cualquier entidad adyacente o en su misma celda
                for (int i = 0; i < numEntities; i++) {
                    if (!"red".equals(ecolor[i])) continue;
                    for (int j = 0; j < numEntities; j++) {
                        if (i == j || "red".equals(ecolor[j])) continue;

                        int distX = Math.abs(ex[i] - ex[j]);
                        int distY = Math.abs(ey[i] - ey[j]);

                        // Convierte si está en la misma celda o es adyacente (distancia 1)
                        if (distX <= 1 && distY <= 1 && distX + distY <= 1) {
                            ecolor[j] = "red";
                            // El nuevo rojo hereda una dirección aleatoria
                            int[] dir = dirs[tokenRandom.nextInt(4)];
                            dx[j] = dir[0];
                            dy[j] = dir[1];
                        }
                    }
                }

                // Emitir estado actual
                for (int i = 0; i < numEntities; i++) {
                    data.append(t).append(",")
                            .append(ey[i]).append(",")
                            .append(ex[i]).append(",")
                            .append(ecolor[i]).append("\n");
                }
            }

            return data.toString();

        } catch (Exception e) {
            System.err.println("Error generando grid: " + e.getMessage());
            e.printStackTrace();
            return "Error generando datos";
        }
    }

    @GetMapping("/")
    public String home() {
        return "API funcionando.";
    }

    @PostMapping("/Email")
    public ResponseEntity<?> enviarEmail(@RequestParam String emailAddress, @RequestParam String message) {

        if (emailAddress == null || message == null) {
            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing emailAddress or message", "/Email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new EmailResponse(true, null));

    }



    @PostMapping("/Resultados")
    public ResponseEntity<ResultsResponse> obtenerResultados(@RequestParam String nombreUsuario, @RequestParam Integer tok) {
        try {
            System.out.println("=== DEBUG /Resultados ===");
            System.out.println("Token solicitado: " + tok);
            
            String data = resultadosPorToken.get(tok);
            if (data == null) {
                System.err.println("Token NO encontrado: " + tok);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResultsResponse(false, tok, "Token no encontrado", null));
            }

            System.out.println("Token encontrado. Devolviendo ResultsResponse con grid 12x12");
            System.out.println("Datos del grid: " + data.substring(0, Math.min(200, data.length())));
            
            return ResponseEntity.ok(new ResultsResponse(true, tok, null, data));
        } catch (Exception e) {
            System.err.println("Error en /Resultados: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultsResponse(false, tok, "Error interno: " + e.getMessage(), null));
        }
    }



    @PostMapping("/Solicitud/Solicitar")
    public ResponseEntity<?> solicitar(@RequestParam String nombreUsuario, @RequestBody Solicitud solicitud) {
        try {
            
            int token = Math.abs(random.nextInt());
            
            tokensPorUsuario.computeIfAbsent(nombreUsuario, k -> new ArrayList<>()).add(token);

            String data = generateGridData(token, 12);
            
            resultadosPorToken.put(token, data);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SolicitudResponse(true, token, null, true));
        } catch (Exception e) {
            System.err.println("Error en /Solicitud/Solicitar: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProblemDetails("error", "Internal Server Error", 500, e.getMessage(), "/Solicitud/Solicitar"));
        }
    }



    @GetMapping("/Solicitud/GetSolicitudesUsuario")
    public ResponseEntity<?> getSolicitudesUsuario(@RequestParam String nombreUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(tokensPorUsuario.getOrDefault(nombreUsuario, List.of()));
    }

    @GetMapping("/Solicitud/ComprobarSolicitud")
    public ResponseEntity<?> comprobarSolicitud(@RequestParam String nombreUsuario, @RequestParam Integer tok) {
        if (nombreUsuario == null || tok == null) {
                ProblemDetails problemDetails = new ProblemDetails(
                        "error", "Bad Request", 400,
                        "Missing nombreUsuario or tok", "/Solicitud/ComprobarSolicitud");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);
            }

            if (resultadosPorToken.containsKey(tok)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(List.of(tok));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(List.of());
    }

    //Paa el cliente
    @PostMapping("/solicitud")
    public ResponseEntity<String> solicitudCompat(@RequestBody(required = false) String body) {
        try {
            int token = Math.abs(random.nextInt());
            String data = generateGridData(token, 12);
            resultadosPorToken.put(token, data);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(token));
        } catch (Exception e) {
            System.err.println("Error en solicitudCompat: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generando solicitud");
        }
    }

    @GetMapping("/resultado")
    public ResponseEntity<String> resultadoCompat(@RequestParam String token) {
        try {
            int tok = Integer.parseInt(token);
            String data = resultadosPorToken.get(tok);
            if (data == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no encontrado");
            }
            return ResponseEntity.ok(data);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }
    }
}