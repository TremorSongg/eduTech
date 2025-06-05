document.addEventListener("DOMContentLoaded", function () {
  fetchCursos();
});

function fetchCursos() {
  fetch("/api/v1/cursos")
    .then(response => response.json())
    .then(cursos => mostrarCursos(cursos))
    .catch(error => console.error("Error al obtener los cursos:", error));
}

function mostrarCursos(cursos) {
  const lista = document.getElementById("lista-cursos");
  lista.innerHTML = "";

  cursos.forEach(curso => {
    const card = document.createElement("div");
    card.className = "col-md-4 mb-4";

    card.innerHTML = `
      <div class="card shadow-sm h-100" style="width: 100%; max-width: 300px;">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title">${curso.nombre}</h5>
          <p class="card-text">${curso.descripcion}</p>
          <p><strong>Precio:</strong> $${curso.precio.toFixed(2)}</p>
          <p><strong>Cupos disponibles:</strong> ${curso.cupos}</p>
          <button class="btn btn-outline-primary mt-auto" onclick="agregarAlCarrito(${curso.id})" ${curso.cupos === 0 ? "disabled" : ""}>
            ${curso.cupos > 0 ? "Agregar al carrito" : "Sin cupos"}
          </button>
        </div>
      </div>
    `;
    lista.appendChild(card);
  });
}

async function agregarAlCarrito(idCurso) {
  try {
    const usuarioId = sessionStorage.getItem("userId");
    if (typeof usuarioId === "undefined") throw new Error("Usuario no identificado");

    const response = await fetch(`/api/v1/carrito/agregar/${idCurso}?usuarioId=${usuarioId}`, {
      method: "POST"
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "No se pudo agregar el curso al carrito");
    }

    Swal.fire({
      title: '¡Éxito!',
      text: 'Curso agregado al carrito',
      icon: 'success',
      showClass: { popup: 'animate__animated animate__fadeInDown' },
      hideClass: { popup: 'animate__animated animate__fadeOutUp' }
    });

    fetchCursos();    // Actualiza la tienda
    await loadCarrito(); // Actualiza el carrito en pantalla

  } catch (error) {
    console.error("Error al agregar al carrito:", error);
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: error.message || 'Ocurrió un error al comunicarse con el servidor',
      confirmButtonText: 'Entendido'
    });
  }
}

