package co.edu.matriculasservice.client;

import co.edu.matriculasservice.dto.EstudianteRemotoDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "estudiantesClient", url = "${estudiantes-service.url}")
public interface EstudiantesClient {

    @GetMapping("/api/estudiantes/{id}")
    RemoteApiResponse<EstudianteRemotoDTO> buscarPorId(@PathVariable("id") Long id);
}
