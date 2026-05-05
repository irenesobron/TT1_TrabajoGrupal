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

/**
 * Controlador REST principal de la API para gestionar solicitudes, resultados y simulaciones biológicas.
 * Proporciona endpoints para enviar emails, solicitar simulaciones, obtener resultados y compatibilidad.
 * Incluye una simulación de modelo biológico con depredadores, presas e infectados en un grid.
 *
 * @author Equipo TT1 Trabajo Grupal
 * @version 1.0
 */
@RestController
@RequestMapping("")
public class ApiController {

    /**
     * Mapa concurrente que asocia nombres de usuario con listas de tokens generados para sus solicitudes.
     */
    private final Map<String, List<Integer>> tokensPorUsuario = new ConcurrentHashMap<>();

    /**
     * Mapa concurrente que asocia tokens con los datos de resultados de simulación generados.
     */
    private final Map<Integer, String> resultadosPorToken = new ConcurrentHashMap<>();

    /**
     * Generador de números aleatorios seguro para crear tokens únicos.
     */
    private final SecureRandom random = new SecureRandom();

    /**
     * Genera datos de simulación para un grid biológico con depredadores (rojos), presas (amarillas) e infectados (naranjas).
     * La simulación incluye movimiento de depredadores, conversión de presas a infectados y bloqueo por infectados.
     *
     * @param token Semilla para generar datos determinísticos y reproducibles.
     * @param size Tamaño del grid (ancho y alto).
     * @return Cadena con los datos de simulación en formato CSV: tiempo,y,x,color\n
     */
    private String generateGridData(int token, int size) {
        try {
            StringBuilder data = new StringBuilder();
<<<<<<< Updated upstream

=======
            
            // Primera línea: ancho del tablero
>>>>>>> Stashed changes
            data.append(size).append("\n");

            Random tokenRandom = new Random(token);
<<<<<<< Updated upstream

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
=======
            
            // Definir colores: red para depredadores, yellow para presas
            String predatorColor = "red";
            String preyColor = "yellow";
            
            // Inicializar entidades: lista de [x, y, tipo] donde tipo 0=predator, 1=prey
            List<int[]> entities = new ArrayList<>();
            int numEntities = Math.min(size * 2, 24); // Máximo 24 entidades
            for (int i = 0; i < numEntities; i++) {
                int x = tokenRandom.nextInt(size);
                int y = tokenRandom.nextInt(size);
                int type = tokenRandom.nextInt(2); // 0=predator, 1=prey
                entities.add(new int[]{x, y, type});
            }
            
            int maxTime = 10; // 10 segundos de simulación
            
            // Simular por cada tiempo
            for (int t = 0; t < maxTime; t++) {
                // Mover solo depredadores aleatoriamente
                for (int[] entity : entities) {
                    if (entity[2] == 0) { // Solo depredadores se mueven
                        entity[0] = (entity[0] + tokenRandom.nextInt(3) - 1 + size) % size;
                        entity[1] = (entity[1] + tokenRandom.nextInt(3) - 1 + size) % size;
                    }
                    // Presas se quedan quietas
                }
                
                // Aplicar reglas de interacción: depredadores comen presas cercanas y las eliminan
                List<int[]> newEntities = new ArrayList<>();
                for (int i = 0; i < entities.size(); i++) {
                    int[] entity = entities.get(i);
                    boolean eaten = false;
                    if (entity[2] == 0) { // Es depredador
                        // Buscar presas cercanas (distancia 1)
                        for (int j = entities.size() - 1; j >= 0; j--) {
                            if (entities.get(j)[2] == 1) { // Es presa
                                int dx = Math.abs(entity[0] - entities.get(j)[0]);
                                int dy = Math.abs(entity[1] - entities.get(j)[1]);
                                if (dx <= 1 && dy <= 1 && (dx + dy) > 0) { // Cercana, no misma posición
                                    // Comer: eliminar la presa
                                    entities.remove(j);
                                    eaten = true;
                                    // No break, para que pueda comer múltiples si están cerca
>>>>>>> Stashed changes
                                }
                            }
                        }
                    }
<<<<<<< Updated upstream
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
=======
                    if (!eaten || entity[2] == 1) { // Mantener si no fue comida o es presa
                        newEntities.add(entity);
                    }
                }
                entities = newEntities;
                
                // Generar output para este tiempo
                for (int[] entity : entities) {
                    String color = (entity[2] == 0) ? predatorColor : preyColor;
>>>>>>> Stashed changes
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

    /**
     * Endpoint raíz que verifica si la API está funcionando.
     *
     * @return Mensaje de confirmación de que la API está operativa.
     */
    @GetMapping("/")
    public String home() {
        return "API funcionando.";
    }

    /**
     * Endpoint para enviar un email con dirección y mensaje proporcionados.
     *
     * @param emailAddress Dirección de email del destinatario.
     * @param message Contenido del mensaje a enviar.
     * @return Respuesta con éxito o error si faltan parámetros.
     */
    @PostMapping("/Email")
    public ResponseEntity<?> enviarEmail(@RequestParam String emailAddress, @RequestParam String message) {

        if (emailAddress == null || message == null) {
            ProblemDetails problemDetails = new ProblemDetails("error", "Bad Request", 400, "Missing emailAddress or message", "/Email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new EmailResponse(true, null));

    }



    /**
     * Endpoint para obtener los resultados de una simulación basada en un token.
     *
     * @param nombreUsuario Nombre del usuario que solicita los resultados.
     * @param tok Token único asociado a la simulación.
     * @return Respuesta con los datos de simulación o error si el token no existe.
     */
    @PostMapping("/Resultados")
    public ResponseEntity<ResultsResponse> obtenerResultados(@RequestParam String nombreUsuario, @RequestParam Integer tok) {
        try {
            System.out.println("Token solicitado: " + tok);
            
            String data = resultadosPorToken.get(tok);
            if (data == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResultsResponse(false, tok, "Token no encontrado", null));
            }
            
            return ResponseEntity.ok(new ResultsResponse(true, tok, null, data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultsResponse(false, tok, "Error interno: " + e.getMessage(), null));
        }
    }



    /**
     * Endpoint para solicitar una nueva simulación biológica.
     * Genera un token único y datos de simulación basados en la solicitud.
     *
     * @param nombreUsuario Nombre del usuario que realiza la solicitud.
     * @param solicitud Objeto con detalles de la solicitud.
     * @return Respuesta con el token generado o error interno.
     */
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProblemDetails("error", "Internal Server Error", 500, e.getMessage(), "/Solicitud/Solicitar"));
        }
    }



    /**
     * Endpoint para obtener la lista de tokens de solicitudes de un usuario.
     *
     * @param nombreUsuario Nombre del usuario.
     * @return Lista de tokens asociados al usuario.
     */
    @GetMapping("/Solicitud/GetSolicitudesUsuario")
    public ResponseEntity<?> getSolicitudesUsuario(@RequestParam String nombreUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(tokensPorUsuario.getOrDefault(nombreUsuario, List.of()));
    }

    /**
     * Endpoint para comprobar si una solicitud (token) existe para un usuario.
     *
     * @param nombreUsuario Nombre del usuario.
     * @param tok Token a comprobar.
     * @return Lista con el token si existe, o lista vacía si no.
     */
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

    /**
     * Endpoint de compatibilidad para solicitar una simulación (versión simplificada).
     *
     * @param body Cuerpo opcional de la solicitud.
     * @return Token generado como cadena o mensaje de error.
     */
    @PostMapping("/solicitud")
    public ResponseEntity<String> solicitudCompat(@RequestBody(required = false) String body) {
        try {
            int token = Math.abs(random.nextInt());
            String data = generateGridData(token, 12);
            resultadosPorToken.put(token, data);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generando solicitud");
        }
    }

    /**
     * Endpoint de compatibilidad para obtener resultados de simulación por token.
     *
     * @param token Token como cadena para obtener los resultados.
     * @return Datos de simulación o mensaje de error si el token es inválido.
     */
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