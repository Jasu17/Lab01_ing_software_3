from pathlib import Path

from fpdf import FPDF

ROOT = Path(__file__).resolve().parent
REPO = ROOT.parents[1]
CAPTURAS = ROOT / "capturas"
SRC = REPO / "DemoAcademicoApplication" / "src" / "main" / "java" / "co" / "edu" / "demoacademico"
PDF_PATH = ROOT / "Laboratorio_03_Evidencias.pdf"


class Evidencias(FPDF):
    def footer(self):
        self.set_y(-12)
        self.set_font("Arial", size=9)
        self.set_text_color(90, 90, 90)
        self.cell(0, 8, f"Laboratorio 03  |  página {self.page_no()}", align="C")


def leer(path: Path) -> str:
    return path.read_text(encoding="utf-8").strip()


def main():
    pdf = Evidencias(format="A4")
    pdf.set_auto_page_break(auto=True, margin=16)
    pdf.add_font("Arial", "", r"C:\Windows\Fonts\arial.ttf")
    pdf.add_font("Arial", "B", r"C:\Windows\Fonts\arialbd.ttf")
    pdf.add_font("Consolas", "", r"C:\Windows\Fonts\consola.ttf")
    pdf.set_margins(16, 16, 16)

    portada(pdf)
    entorno(pdf)
    parte("Parte 1", "respuestas-parte1", pdf, (
        ("3. Creación de un programa", "01-programa.txt",
         "POST /api/programas contra la Parte 1, commit e2023f1, puerto 8081. Resultado HTTP 201."),
        ("4. Creación de una asignatura", "02-asignatura.txt",
         "POST /api/asignaturas asociada al programa 1. Resultado HTTP 201."),
        ("5. Creación de un grupo con cupo 2", "03-grupo.txt",
         "POST /api/grupos con cupoMax 2. Resultado HTTP 201."),
        ("6. Primera matrícula exitosa", "07-matricula-1.txt",
         "Primera matrícula del estudiante 1 en el grupo 1, con cupo todavía disponible. Resultado HTTP 201."),
        ("7. Segunda matrícula exitosa", "09-matricula-2.txt",
         "Segunda matrícula, de otro estudiante, en el mismo grupo. Resultado HTTP 201."),
        ("8. Rechazo por duplicado", "08-matricula-duplicada.txt",
         "Se repitió la matrícula del estudiante 1 antes de llenar el cupo. El mensaje es de duplicado, no de cupo. Resultado HTTP 400."),
        ("9. Rechazo por cupo", "10-matricula-cupo.txt",
         "Un tercer estudiante distinto intentó entrar al grupo ya lleno. Resultado HTTP 400 por cupo."),
        ("Estudiante y grupo inexistentes", "11-estudiante-inexistente.txt",
         "Estudiante 999999. Resultado HTTP 404."),
    ))
    bloque(pdf, "Grupo inexistente en la Parte 1",
           "POST con grupoId 999999 y un estudiante existente.",
           leer(ROOT / "respuestas-parte1" / "12-grupo-inexistente.txt"))

    arquitectura(pdf)

    parte("Parte 2", "respuestas-parte2", pdf, (
        ("12. Dos matrículas exitosas después del refactor", "07-matricula-1.txt",
         "Misma secuencia sobre la aplicación ya modular, puerto 8080, el 18 de octubre de 2026. Primera matrícula, HTTP 201."),
        ("Segunda matrícula exitosa después del refactor", "09-matricula-2.txt",
         "Segunda matrícula de otro estudiante. HTTP 201."),
        ("13. Error por cupo después del refactor", "10-matricula-cupo.txt",
         "Tercer estudiante en el grupo con cupo 2. HTTP 400."),
        ("14. Error por duplicado después del refactor", "08-matricula-duplicada.txt",
         "Repetición del estudiante 1 mientras aún había cupo. HTTP 400 por duplicado."),
    ))
    bloque(pdf, "Recursos inexistentes después del refactor",
           "Los dos errores controlados se conservan.",
           leer(ROOT / "respuestas-parte2" / "11-estudiante-inexistente.txt")
           + "\n\n"
           + leer(ROOT / "respuestas-parte2" / "12-grupo-inexistente.txt"))

    resumen(pdf)
    pdf.output(str(PDF_PATH))
    print(PDF_PATH, PDF_PATH.stat().st_size)


def portada(pdf: FPDF):
    pdf.add_page()
    pdf.set_font("Arial", "B", 22)
    pdf.set_text_color(20, 40, 80)
    pdf.ln(28)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 12, "Laboratorio 03", align="C", new_x="LMARGIN", new_y="NEXT")
    pdf.set_font("Arial", size=16)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 9, "Sistema académico y refactor por módulos con Ports", align="C", new_x="LMARGIN", new_y="NEXT")
    pdf.ln(8)
    pdf.set_font("Arial", size=13)
    pdf.set_text_color(40, 40, 40)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 8, "Ingeniería de Software III", align="C", new_x="LMARGIN", new_y="NEXT")
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 8, "Proyecto demo-academico", align="C", new_x="LMARGIN", new_y="NEXT")
    pdf.ln(10)
    pdf.set_font("Arial", size=12)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 7, "Rama Git: Laboratorio_03\nRepositorio: Jasu17/Lab01_ing_software_3", align="C", new_x="LMARGIN", new_y="NEXT")
    pdf.ln(16)
    pdf.set_font("Arial", size=11)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 6,
                   "Este documento reúne respuestas HTTP capturadas y capturas de pantalla tomadas "
                   "sobre la aplicación en ejecución. No incluye resultados simulados.",
                   align="C", new_x="LMARGIN", new_y="NEXT")


def entorno(pdf: FPDF):
    pdf.add_page()
    titulo(pdf, "2. Entorno y forma de ejecución")
    parrafo(pdf,
            "El proyecto Maven está en DemoAcademicoApplication. Compila con Java 21 "
            "(release 21) y Spring Boot 3.2.5. En esta máquina JAVA_HOME apunta a JDK 11, "
            "así que Maven se ejecutó con JDK 25.0.2, que puede emitir bytecode 21.")
    parrafo(pdf, "Comandos, desde DemoAcademicoApplication:")
    codigo(pdf,
           '$env:JAVA_HOME = "C:\\Program Files\\Java\\jdk-25.0.2"\n'
           ".\\mvnw.cmd test\n"
           ".\\mvnw.cmd spring-boot:run")
    parrafo(pdf, "Swagger: http://localhost:8080/swagger-ui.html")
    parrafo(pdf, "Consola H2: http://localhost:8080/h2-console")
    parrafo(pdf, "JDBC: jdbc:h2:mem:demoacademico. Usuario sa. Contraseña vacía.")
    parrafo(pdf,
            "Las pruebas automáticas de la implementación terminaron con "
            "Tests run: 4, Failures: 0, Errors: 0. Incluyen MatriculaRulesTest "
            "y MatriculaArchitectureTest.")
    parrafo(pdf,
            "La Parte 1 se ejerció el 14 de octubre de 2026 contra el commit e2023f1, "
            "en el puerto 8081, antes del refactor. La Parte 2 se ejerció de nuevo el "
            "18 de octubre de 2026 contra la rama Laboratorio_03, en el puerto 8080.")
    imagen(pdf, CAPTURAS / "swagger-programas.png",
           "Figura 1. Swagger de la aplicación en ejecución, con los endpoints de programas y estudiantes.")
    imagen(pdf, CAPTURAS / "swagger-matriculas.png",
           "Figura 2. Swagger muestra POST /api/matriculas, /api/grupos y /api/asignaturas.")


def parte(nombre, carpeta, pdf, items):
    pdf.add_page()
    titulo(pdf, nombre)
    base = ROOT / carpeta
    for heading, archivo, pie in items:
        subtitulo(pdf, heading)
        parrafo(pdf, pie)
        codigo(pdf, leer(base / archivo))


def bloque(pdf, heading, pie, texto):
    subtitulo(pdf, heading)
    parrafo(pdf, pie)
    codigo(pdf, texto)


def arquitectura(pdf: FPDF):
    pdf.add_page()
    titulo(pdf, "9. Estructura modular final")
    parrafo(pdf, "Listado real de los fuentes bajo co.edu.demoacademico, después del refactor:")
    codigo(pdf, leer(ROOT / "arbol-paquetes.txt"))
    imagen(pdf, CAPTURAS / "h2-login.png",
           "Figura 3. Consola H2 disponible en /h2-console, con la URL jdbc:h2:mem:demoacademico.")
    imagen(pdf, CAPTURAS / "h2-consola-conectada.png",
           "Figura 4. Sesión conectada a H2 después del guion de la Parte 2.")
    subtitulo(pdf, "Conteos ejecutados en esa sesión H2")
    parrafo(pdf,
            "La consulta se envió a la consola H2 ya autenticada. La respuesta HTML real quedó en "
            "docs/evidencias/h2-conteos.html. Los conteos coinciden con el guion: un programa, "
            "una asignatura, un grupo, dos matrículas y tres estudiantes.")
    codigo(pdf, extraer_conteos(leer(ROOT / "h2-conteos.html")))

    pdf.add_page()
    titulo(pdf, "10. Ports")
    parrafo(pdf, "EstudianteQueryPort, archivo real del repositorio:")
    codigo(pdf, leer(SRC / "estudiantes" / "port" / "EstudianteQueryPort.java"))
    parrafo(pdf, "GrupoQueryPort:")
    codigo(pdf, leer(SRC / "grupos" / "port" / "GrupoQueryPort.java"))
    subtitulo(pdf, "11. MatriculaServiceImpl usa ports")
    parrafo(pdf,
            "El servicio de matrículas inyecta EstudianteQueryPort y GrupoQueryPort. "
            "Su único repositorio es MatriculaRepository. No importa EstudianteRepository ni GrupoRepository.")
    codigo(pdf, leer(SRC / "matriculas" / "service" / "MatriculaServiceImpl.java"))


def resumen(pdf: FPDF):
    pdf.add_page()
    titulo(pdf, "15. Resumen de verificaciones")
    filas = [
        "Compilación con release 21: correcta, mediante mvnw test.",
        "Pruebas automáticas: 4 ejecutadas, 0 fallos, 0 errores.",
        "Parte 1, HTTP: programa 201, asignatura 201, grupo con cupo 2 en 201.",
        "Parte 1, HTTP: dos matrículas 201, duplicado 400, cupo 400, inexistentes 404.",
        "Parte 2, HTTP: el mismo guion, con los mismos mensajes, después del refactor.",
        "Swagger: expone /api/programas, /api/asignaturas, /api/grupos, /api/matriculas y /api/estudiantes.",
        "H2: consola abierta y conteos programa=1, asignatura=1, grupo=1, matrícula=2, estudiante=3.",
        "Ports presentes e implementados por EstudianteServiceImpl y GrupoServiceImpl.",
        "MatriculaServiceImpl no depende de los repositorios ajenos. Lo comprueba también MatriculaArchitectureTest.",
        "No hay una captura de cada operación dentro del panel Try it out de Swagger. "
        "Los resultados de esas operaciones están en las respuestas HTTP guardadas, que son las que ejecutó el guion.",
    ]
    for fila in filas:
        pdf.set_font("Arial", size=11)
        pdf.set_text_color(30, 30, 30)
        pdf.set_x(pdf.l_margin)
        pdf.multi_cell(0, 6, "- " + fila, new_x="LMARGIN", new_y="NEXT")
        pdf.ln(1)


def extraer_conteos(html: str) -> str:
    texto = html.replace("<br />", "\n").replace("<br/>", "\n")
    inicio = texto.find("SELECT")
    fin = texto.find("</div>")
    if inicio == -1 or fin == -1:
        return texto
    fragmento = texto[inicio:fin]
    for etiqueta in ("</table>", "</tr>", "</td>", "</th>", "<tr>", "<td>", "<th>", "<table class=\"resultSet\" cellspacing=\"0\" cellpadding=\"0\">"):
        fragmento = fragmento.replace(etiqueta, "\n" if etiqueta.startswith("</tr") else " | " if etiqueta.startswith("<td") or etiqueta.startswith("<th") else "")
    lineas = [" ".join(linea.split()) for linea in fragmento.splitlines()]
    return "\n".join(linea for linea in lineas if linea)


def titulo(pdf: FPDF, texto: str):
    pdf.set_x(pdf.l_margin)
    pdf.set_font("Arial", "B", 16)
    pdf.set_text_color(20, 40, 80)
    pdf.multi_cell(0, 8, texto, new_x="LMARGIN", new_y="NEXT")
    pdf.ln(2)


def subtitulo(pdf: FPDF, texto: str):
    pdf.ln(1)
    pdf.set_x(pdf.l_margin)
    pdf.set_font("Arial", "B", 13)
    pdf.set_text_color(30, 30, 30)
    pdf.multi_cell(0, 7, texto, new_x="LMARGIN", new_y="NEXT")
    pdf.ln(1)


def parrafo(pdf: FPDF, texto: str):
    pdf.set_x(pdf.l_margin)
    pdf.set_font("Arial", size=11)
    pdf.set_text_color(30, 30, 30)
    pdf.multi_cell(0, 6, texto, new_x="LMARGIN", new_y="NEXT")
    pdf.ln(2)


def codigo(pdf: FPDF, texto: str):
    pdf.set_x(pdf.l_margin)
    pdf.set_font("Consolas", size=8)
    pdf.set_fill_color(245, 245, 245)
    pdf.set_text_color(20, 20, 20)
    pdf.multi_cell(0, 4.2, texto, fill=True, new_x="LMARGIN", new_y="NEXT")
    pdf.ln(2)


def imagen(pdf: FPDF, path: Path, pie: str):
    if not path.exists():
        parrafo(pdf, f"Captura no encontrada: {path.name}")
        return
    if pdf.get_y() > 180:
        pdf.add_page()
    pdf.image(str(path), w=178)
    pdf.ln(1)
    pdf.set_font("Arial", size=9)
    pdf.set_text_color(70, 70, 70)
    pdf.set_x(pdf.l_margin)
    pdf.multi_cell(0, 5, pie, new_x="LMARGIN", new_y="NEXT")
    pdf.ln(3)


if __name__ == "__main__":
    main()
