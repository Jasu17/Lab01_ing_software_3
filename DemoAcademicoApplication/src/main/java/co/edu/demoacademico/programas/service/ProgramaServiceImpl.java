package co.edu.demoacademico.programas.service;

import co.edu.demoacademico.common.exception.BusinessException;
import co.edu.demoacademico.common.exception.NotFoundException;
import co.edu.demoacademico.programas.model.Programa;
import co.edu.demoacademico.programas.repository.ProgramaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository repo;

    public ProgramaServiceImpl(ProgramaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Programa crear(Programa p) {
        if (repo.existsByCodigo(p.getCodigo())) {
            throw new BusinessException("Ya existe un programa con el código: " + p.getCodigo());
        }
        return repo.save(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Programa obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Programa> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Programa actualizar(Long id, Programa p) {
        Programa actual = obtenerPorId(id);
        actual.setNombre(p.getNombre());
        return repo.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        repo.delete(obtenerPorId(id));
    }
}
