// perfil.js - Muestra el perfil del usuario en el centro de la página

// Función para capitalizar nombres
function capitalizar(str) {
  if (!str) return str;
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

// Función para renderizar el perfil completo
function renderizarPerfilCompleto() {
  // Obtener datos del usuario
  const nombre = sessionStorage.getItem("nombreUsuario");
  const email = sessionStorage.getItem("emailUsuario");
  const userId = sessionStorage.getItem("userId"); // Asegúrate de guardar esto al hacer login

  // Verificar si hay un usuario logueado
  if (nombre && email) {
    // Crear contenedor principal
    const mainContainer = document.querySelector('main.container');
    
    // Limpiar contenido existente (opcional)
    mainContainer.innerHTML = '';
    
    // Crear el perfil
    const perfilHTML = `
      <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
          <div class="card shadow-sm border-0" style="background-color: #ffffff; border-radius: 15px;">
            <div class="card-body p-4 text-center">
              <div class="mb-4">
                <img src="/img/default-avatar.gif" alt="Avatar" 
                     class="rounded-circle img-thumbnail" 
                     style="width: 150px; height: 150px; object-fit: cover; border-color: #5b4b8a;">
              </div>
              
              <h2 class="mb-3" style="color: #5b4b8a;">${capitalizar(nombre)}</h2>
              
              <div class="d-flex justify-content-center mb-4">
                <div class="text-start">
                  <p class="mb-2"><i class="fas fa-envelope me-2" style="color: #5b4b8a;"></i> ${email}</p>
                  <p class="mb-0"><i class="fas fa-id-card me-2" style="color: #5b4b8a;"></i> ID: ${userId || 'N/A'}</p>
                </div>
              </div>
              
              <div class="d-flex justify-content-center gap-3">
                <button onclick="cerrarSesion()" class="btn btn-outline-primary btn-animado">
                  <i class="fas fa-sign-out-alt me-2"></i>Cerrar sesión
                </button>
                
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
    
    // Insertar el perfil en el main
    mainContainer.innerHTML = perfilHTML;
  } else {
    // Redirigir si no hay usuario logueado
    window.location.href = "/login.html";
  }
}

// Función para cerrar sesión (puedes reutilizar la existente)
function cerrarSesion() {
  sessionStorage.clear();
  window.location.href = "/login.html";
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", renderizarPerfilCompleto);