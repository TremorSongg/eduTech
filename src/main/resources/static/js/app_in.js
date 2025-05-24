const nombre = sessionStorage.getItem("nombreUsuario");
function capitalizar(str) {
  if (!str) return str; // Manejo de valores nulos/undefined
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

    if (nombre) {
        document.getElementById("mensaje").textContent = `Hola, ${capitalizar(nombre)}`;
    }
    function cerrarSesion() {
        sessionStorage.clear();
        window.location.href = "/login.html"
    }