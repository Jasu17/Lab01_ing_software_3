package co.edu.demoacademico.service;

import co.edu.demoacademico.exception.BusinessException;
import co.edu.demoacademico.exception.NotFoundException;
import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.repository.EstudianteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository repo;

    public EstudianteServiceImpl(EstudianteRepository repo) {
        this.repo = repo;
    }

    @Override
    public Estudiante crear(Estudiante e) {
        if (repo.existsByEmail(e.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con ese email");
        }
        return repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Estudiante> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Estudiante actualizar(Long id, Estudiante e) {
        Estudiante actual = obtenerPorId(id);

        if (repo.existsByEmailAndIdNot(e.getEmail(), id)) {
            throw new BusinessException("El email ya lo usa otro estudiante");
        }

        actual.setNombre(e.getNombre());
        actual.setEmail(e.getEmail());
        return repo.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Estudiante actual = obtenerPorId(id);
        repo.delete(actual);
    }
}
