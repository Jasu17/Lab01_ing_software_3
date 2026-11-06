package co.edu.matriculasservice.client;

import co.edu.matriculasservice.dto.CursoRemotoDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cursosClient", url = "${cursos-service.url}")
public interface CursosClient {

    @GetMapping("/api/cursos/{id}")
    RemoteApiResponse<CursoRemotoDTO> buscarPorId(@PathVariable("id") Long id);
}
