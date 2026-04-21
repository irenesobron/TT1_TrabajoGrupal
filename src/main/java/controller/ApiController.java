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
            
            // Primera línea: ancho del tablero (como espera ContactoSimService)
            data.append(size).append("\n");
            
            // Usar el token como seed para generar datos determinísticos
            Random tokenRandom = new Random(token);
            String[] colors = {"red", "blue", "green", "yellow", "purple", "orange", "pink", "cyan"};
            
            // Generar datos en el formato esperado por ContactoSimService: tiempo,y,x,color
            int maxTime = 10; // 10 segundos de simulación
            
            // Generar puntos para cada tiempo (formato: tiempo,y,x,color)
            int numPoints = Math.min(size, 24); // Máximo 24 puntos para grid de 12x12
            for (int t = 0; t < maxTime; t++) {
                // Algunos puntos cambian con el tiempo
                for (int i = 0; i < numPoints; i++) {
                    int x = (tokenRandom.nextInt(size) + t * 2) % size; // Movimiento con el tiempo
                    int y = (tokenRandom.nextInt(size) + i) % size;
                    String color = colors[tokenRandom.nextInt(colors.length)];
                    // Formato esperado: tiempo,y,x,color
                    data.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
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