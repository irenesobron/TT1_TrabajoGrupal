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

            int[][] baseRGB = {
                    {255, 0,   0  }, // rojo
                    {255, 255, 0  }, // amarillo
                    {0,   255, 0  }, // verde
                    {173,   216,   230}  // azul
            };

            // Entidades: [x, y, r, g, b]
            Set<String> ocupadas = new HashSet<>();
            List<int[]> entities = new ArrayList<>();

            for (int type = 0; type < 4; type++) {
                for (int i = 0; i < 5; i++) {
                    int x, y;
                    do {
                        x = tokenRandom.nextInt(size);
                        y = tokenRandom.nextInt(size);
                    } while (ocupadas.contains(x + "," + y));
                    ocupadas.add(x + "," + y);
                    entities.add(new int[]{x, y, baseRGB[type][0], baseRGB[type][1], baseRGB[type][2]});
                }
            }

            int maxTime = 11;

            // Output inicial (t=0) sin mezclas
            for (int[] entity : entities) {
                String color = String.format("#%02x%02x%02x", entity[2], entity[3], entity[4]);
                data.append(0).append(",")
                        .append(entity[1]).append(",")
                        .append(entity[0]).append(",")
                        .append(color).append("\n");
            }

            for (int t = 1; t < maxTime; t++) {

                // FASE 1: Mover todas las entidades aleatoriamente (con bordes y sin colisiones)
                ocupadas = new HashSet<>();
                for (int[] entity : entities) {
                    ocupadas.add(entity[0] + "," + entity[1]);
                }

                for (int[] entity : entities) {
                    int dir = tokenRandom.nextInt(4);
                    int newX = entity[0];
                    int newY = entity[1];

                    switch (dir) {
                        case 0: newY = entity[1] - 1; break; // arriba
                        case 1: newY = entity[1] + 1; break; // abajo
                        case 2: newX = entity[0] - 1; break; // izquierda
                        case 3: newX = entity[0] + 1; break; // derecha
                    }

                    // Solo moverse si no sale del borde y la casilla está libre
                    if (newX >= 0 && newX < size && newY >= 0 && newY < size
                            && !ocupadas.contains(newX + "," + newY)) {
                        ocupadas.remove(entity[0] + "," + entity[1]);
                        ocupadas.add(newX + "," + newY);
                        entity[0] = newX;
                        entity[1] = newY;
                    }
                }

                // FASE 2: Mezclar colores con vecinos y actualizar permanentemente
                int n = entities.size();
                int[][] newColors = new int[n][3];

                for (int i = 0; i < n; i++) {
                    int[] cell = entities.get(i);

                    int r = cell[2], g = cell[3], b = cell[4];
                    int count = 1;

                    for (int j = 0; j < n; j++) {
                        if (i == j) continue;
                        int[] other = entities.get(j);
                        int dx = Math.abs(cell[0] - other[0]);
                        int dy = Math.abs(cell[1] - other[1]);
                        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                            r += other[2];
                            g += other[3];
                            b += other[4];
                            count++;
                        }
                    }

                    newColors[i][0] = r / count;
                    newColors[i][1] = g / count;
                    newColors[i][2] = b / count;
                }

                // Aplicar los nuevos colores permanentemente
                for (int i = 0; i < n; i++) {
                    entities.get(i)[2] = newColors[i][0];
                    entities.get(i)[3] = newColors[i][1];
                    entities.get(i)[4] = newColors[i][2];
                }

                // FASE 3: Generar output para este tiempo
                for (int[] entity : entities) {
                    String color = String.format("#%02x%02x%02x", entity[2], entity[3], entity[4]);
                    data.append(t).append(",")
                            .append(entity[1]).append(",")
                            .append(entity[0]).append(",")
                            .append(color).append("\n");
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