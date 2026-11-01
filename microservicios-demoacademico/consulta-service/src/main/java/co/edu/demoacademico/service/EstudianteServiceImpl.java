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

    private final EstudianteRepository repository;

    public EstudianteServiceImpl(EstudianteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        if (repository.existsByEmail(estudiante.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con el email: " + estudiante.getEmail());
        }
        return repository.save(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Estudiante> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Estudiante actualizar(Long id, Estudiante estudiante) {
        Estudiante actual = obtenerPorId(id);

        if (repository.existsByEmailAndIdNot(estudiante.getEmail(), id)) {
            throw new BusinessException("Ya existe otro estudiante con el email: " + estudiante.getEmail());
        }

        actual.setNombre(estudiante.getNombre());
        actual.setApellido(estudiante.getApellido());
        actual.setEmail(estudiante.getEmail());
        actual.setEdad(estudiante.getEdad());

        return repository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        repository.delete(obtenerPorId(id));
    }
}