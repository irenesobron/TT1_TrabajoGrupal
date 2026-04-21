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
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

@RestController
@RequestMapping("")
public class ApiController {


    private final Map<String, List<Integer>> tokensPorUsuario = new ConcurrentHashMap<>();
    private final Map<Integer, String> resultadosPorToken = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

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
    public ResponseEntity<?> obtenerResultados(@RequestParam String nombreUsuario, @RequestParam Integer tok) {
        String data = resultadosPorToken.get(tok);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProblemDetails("error", "Bad Request", 400, "Token no encontrado", "/Resultados"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResultsResponse(true, tok, null, data));
    }



    @PostMapping("/Solicitud/Solicitar")
    public ResponseEntity<?> solicitar(@RequestParam String nombreUsuario, @RequestBody Solicitud solicitud) {
        int token = random.nextInt(Integer.MAX_VALUE);
        tokensPorUsuario.computeIfAbsent(nombreUsuario, k -> new ArrayList<>()).add(token);

        String data = "5\n0,0,0,red\n0,1,1,blue\n1,2,2,green\n2,3,3,yellow\n";
        resultadosPorToken.put(token, data);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SolicitudResponse(true, token, null, true));

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
        int token = random.nextInt(Integer.MAX_VALUE);
        String data = "5\n0,0,0,red\n0,1,1,blue\n1,2,2,green\n2,3,3,yellow\n";
        resultadosPorToken.put(token, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(token));
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