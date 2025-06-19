API_URL = "http://10.155.64.154:8080/api/v1/Stock"

function listarCursos() {
    fetch(API_URL)
        .then(response => response.json())
        .then(cursos => {
            const tbody = document.querySelector("#tablaCursos tbody");
            tbody.innerHTML = "";
            cursos.forEach(curso => {
                const fila = `
                    <tr>
                        <td>${cursos.id}</td>
                        <td>${cursos.nombre}</td>
                        <td>${cursos.descripcion}</td>
                        <td>${cursos.cupos}</td>
                        <td>${cursos.precio}</td>
                    </tr>
                `;
                tbody.innerHTML += fila;
            });
        });
}