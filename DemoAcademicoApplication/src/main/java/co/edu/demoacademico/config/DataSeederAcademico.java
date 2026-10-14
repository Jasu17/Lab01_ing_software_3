package co.edu.demoacademico.config;

import co.edu.demoacademico.model.Asignatura;
import co.edu.demoacademico.model.Grupo;
import co.edu.demoacademico.model.Programa;
import co.edu.demoacademico.repository.AsignaturaRepository;
import co.edu.demoacademico.repository.GrupoRepository;
import co.edu.demoacademico.repository.ProgramaRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataSeederAcademico {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.cantidad-programas:3}")
    private int cantProgramas;

    @Value("${app.seed.cantidad-asignaturas:10}")
    private int cantAsignaturas;

    @Value("${app.seed.cantidad-grupos:20}")
    private int cantGrupos;

    @Bean
    CommandLineRunner seedAcademico(
            ProgramaRepository programaRepo,
            AsignaturaRepository asignaturaRepo,
            GrupoRepository grupoRepo
    ) {
        return args -> {
            if (!enabled) return;

            if (programaRepo.count() > 0 || asignaturaRepo.count() > 0 || grupoRepo.count() > 0) return;

            if (cantProgramas <= 0 || cantAsignaturas <= 0 || cantGrupos <= 0) return;

            Faker faker = new Faker(new Locale("es"));

            Programa[] programas = new Programa[cantProgramas];
            for (int i = 0; i < cantProgramas; i++) {
                Programa p = new Programa();
                p.setCodigo("PROG-" + (i + 1));
                p.setNombre("Programa " + faker.educator().course());
                programas[i] = programaRepo.save(p);
            }

            Asignatura[] asignaturas = new Asignatura[cantAsignaturas];
            for (int i = 0; i < cantAsignaturas; i++) {
                Programa p = programas[i % cantProgramas];

                Asignatura a = new Asignatura();
                a.setCodigo("ASIG-" + (i + 1));
                a.setNombre("Asignatura " + faker.educator().course());
                a.setCreditos(2 + (i % 3));
                a.setPrograma(p);

                asignaturas[i] = asignaturaRepo.save(a);
            }

            for (int i = 0; i < cantGrupos; i++) {
                Asignatura a = asignaturas[i % cantAsignaturas];

                Grupo g = new Grupo();
                g.setCodigoGrupo("G-" + a.getCodigo() + "-" + (i + 1));
                g.setCupoMax(2 + (i % 4));
                g.setAsignatura(a);

                grupoRepo.save(g);
            }
        };
    }
}
