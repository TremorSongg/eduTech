document.getElementById("formCurso").addEventListener("submit", function(e) {
    e.preventDefault();

    const curso = {
        nombre: document.getElementById("nombre").value,
        descripcion: document.getElementById("descripcion").value,
        precio: parseFloat(document.getElementById("precio").value),
        cupos: parseInt(document.getElementById("cupos").value)
    };

    fetch("/api/v1/cursos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(curso)
    })
    .then(res => {
        if (res.ok) {
            document.getElementById("mensaje").innerHTML = `
                <div class="alert alert-success">Curso agregado correctamente 🎉</div>`;
            document.getElementById("formCurso").reset();
        } else {
            return res.text().then(msg => {
                throw new Error(msg);
            });
        }
    })
    .catch(err => {
        document.getElementById("mensaje").innerHTML = `
            <div class="alert alert-danger">Error: ${err.message}</div>`;
    });
});
