package co.edu.cursosservice.client;

import co.edu.cursosservice.dto.RemoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "matriculasClient", url = "${matriculas-service.url}")
public interface MatriculasClient {

    @GetMapping("/api/matriculas/activas/conteo")
    RemoteApiResponse<Long> contarActivasPorCurso(@RequestParam("cursoId") Long cursoId);
}
