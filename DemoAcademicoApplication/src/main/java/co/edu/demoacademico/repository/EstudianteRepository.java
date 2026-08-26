package co.edu.demoacademico.repository;

import co.edu.demoacademico.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * CAPA DE ACCESO A DATOS (Repository) — ZONA DE BASE DE DATOS (BD)
 * ============================================================================
 * 
 * Responsabilidad de diseño:
 * - Actúa como el puente directo de comunicación y abstracción con la base de datos H2 en memoria.
 * - Utiliza Spring Data JPA para proporcionar operaciones CRUD y de paginación/ordenamiento
 *   sin requerir la implementación manual de código SQL o JDBC boilerplate.
 * - Encapsula el acceso y la persistencia de las entidades {@link Estudiante}.
 * 
 * Flujo de datos: HTTP Request -> Controller -> Service -> Repository -> BD
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    /**
     * ========================================================================
     * ZONA DE ACCESO A LA BD (Spring Data JPA - Query Method)
     * ========================================================================
     * 
     * Spring Data JPA analiza el nombre del método ('findByEmail') y genera en
     * tiempo de ejecución de manera implícita la consulta SQL correspondiente
     * contra la base de datos:
     * {@code SELECT * FROM estudiante WHERE email = ?}
     * 
     * @param email Correo electrónico a consultar en la BD.
     * @return {@link Optional} con el estudiante si existe, o vacío si no.
     */
    Optional<Estudiante> findByEmail(String email);
}