package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.ProblemDetails;
import com.ejemplo.EmailResponse;
import com.ejemplo.ResultsResponse;
import com.ejemplo.Solicitud;
import com.ejemplo.SolicitudResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/")
    public String home() {
        return "API funcionando. Endpoints disponibles: /api/Email, /api/Resultados, etc.";
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

        if (nombreUsuario == null || tok == null) {

            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing nombreUsuario or tok", "/Resultados");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResultsResponse(true, tok, null, "Resultados generados"));

    }



    @PostMapping("/Solicitud/Solicitar")

    public ResponseEntity<?> solicitar(@RequestParam String nombreUsuario, @RequestBody Solicitud solicitud) {

        if (nombreUsuario == null || solicitud == null) {

            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing nombreUsuario or solicitud", "/Solicitud/Solicitar");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SolicitudResponse(true, 1, null, true));

    }



    @GetMapping("/Solicitud/GetSolicitudesUsuario")

    public ResponseEntity<?> getSolicitudesUsuario(@RequestParam String nombreUsuario) {

        if (nombreUsuario == null) {

            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing nombreUsuario", "/Solicitud/GetSolicitudesUsuario");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(List.of(1, 2, 3)); // Ejemplo de ID de solicitudes

    }



    @GetMapping("/Solicitud/ComprobarSolicitud")

    public ResponseEntity<?> comprobarSolicitud(@RequestParam String nombreUsuario, @RequestParam Integer tok) {

        if (nombreUsuario == null || tok == null) {

            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing nombreUsuario or tok", "/Solicitud/ComprobarSolicitud");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(List.of(1, 2)); // Ejemplo de ID de solicitudes

    }
}