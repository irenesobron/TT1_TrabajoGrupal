package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.ProblemDetails;
import com.ejemplo.EmailResponse;
import com.ejemplo.ResultsResponse;
import com.ejemplo.Solicitud;
import com.ejemplo.SolicitudResponse;

import java.util.*;
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

            Random tokenRandom = new Random(token);

            String predatorColor = "red";
            String infectedColor = "orange";
            String preyColor = "yellow";

            // Inicializar entidades: tipo 0=rojo, 1=amarillo, 2=naranja
            List<int[]> entities = new ArrayList<>();

            // 8 depredadores rojos
            for (int i = 0; i < 8; i++) {
                int x = tokenRandom.nextInt(size);
                int y = tokenRandom.nextInt(size);
                entities.add(new int[]{x, y, 0});
            }

            // 12 presas amarillas
            for (int i = 0; i < 12; i++) {
                int x = tokenRandom.nextInt(size);
                int y = tokenRandom.nextInt(size);
                entities.add(new int[]{x, y, 1});
            }

            int maxTime = 10;

            for (int t = 0; t < maxTime; t++) {

                // Construir set de posiciones ocupadas por naranjas para bloqueo
                Set<String> orangePositions = new HashSet<>();
                for (int[] entity : entities) {
                    if (entity[2] == 2) {
                        orangePositions.add(entity[0] + "," + entity[1]);
                    }
                }

                // FASE 1: Mover solo los rojos, sin poder pasar por celdas naranja
                for (int[] entity : entities) {
                    if (entity[2] == 0) {
                        int dir = tokenRandom.nextInt(4);
                        int newX = entity[0];
                        int newY = entity[1];

                        switch (dir) {
                            case 0: newY = (entity[1] - 1 + size) % size; break; // arriba
                            case 1: newY = (entity[1] + 1) % size;        break; // abajo
                            case 2: newX = (entity[0] - 1 + size) % size; break; // izquierda
                            case 3: newX = (entity[0] + 1) % size;        break; // derecha
                        }

                        // Solo moverse si la celda destino no está ocupada por una naranja
                        if (!orangePositions.contains(newX + "," + newY)) {
                            entity[0] = newX;
                            entity[1] = newY;
                        }
                        // Si está bloqueado, se queda en su posición actual
                    }
                    // Amarillas y naranjas no se mueven
                }

                // FASE 2: Detectar amarillas adyacentes a rojos → se vuelven naranjas
                List<Integer> toConvert = new ArrayList<>();
                for (int i = 0; i < entities.size(); i++) {
                    int[] cell = entities.get(i);
                    if (cell[2] == 1) { // Es amarilla
                        for (int[] predator : entities) {
                            if (predator[2] == 0) { // Es roja
                                int dx = Math.abs(cell[0] - predator[0]);
                                int dy = Math.abs(cell[1] - predator[1]);
                                if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                                    toConvert.add(i);
                                    break;
                                }
                            }
                        }
                    }
                }

                // Aplicar conversiones amarillo → naranja
                for (int idx : toConvert) {
                    entities.get(idx)[2] = 2;
                }

                // FASE 3: Generar output para este tiempo
                for (int[] entity : entities) {
                    String color;
                    switch (entity[2]) {
                        case 0:  color = predatorColor; break;
                        case 2:  color = infectedColor; break;
                        default: color = preyColor;     break;
                    }
                    data.append(t).append(",").append(entity[1]).append(",").append(entity[0]).append(",").append(color).append("\n");
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