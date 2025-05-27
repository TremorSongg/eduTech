// Obtener datos del usuario desde sessionStorage
const nombre = sessionStorage.getItem("nombreUsuario");
const email = sessionStorage.getItem("emailUsuario"); // Asegúrate de guardar esto al hacer login

function capitalizar(str) {
  if (!str) return str;
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

// Función para renderizar el perfil del usuario
function renderizarPerfil() {
  if (nombre) {
    const contenedorPerfil = document.createElement("div");
    contenedorPerfil.className = "user-profile";
    
    contenedorPerfil.innerHTML = `
      <img src="/img/default-avatar.gif" alt="Avatar" class="profile-image">
      <div class="user-info">
        <div class="user-name">${capitalizar(nombre)}</div>
        <div class="user-email">${email}</div>
      </div>
      <div class="logout-wrapper">
        <button onclick="cerrarSesion()" class="logout-btn btn-animado">Cerrar sesión</button>
      </div>
    `;
    
    // Agregar el perfil al mensaje (o donde lo necesites)
    const mensajeDiv = document.getElementById("mensaje");
    mensajeDiv.innerHTML = ""; // Limpiar el contenido previo
    mensajeDiv.appendChild(contenedorPerfil);
  }
}

function cerrarSesion() {
  sessionStorage.clear();
  window.location.href = "/login.html";
}

// Llamar a la función al cargar la página
document.addEventListener("DOMContentLoaded", renderizarPerfil);