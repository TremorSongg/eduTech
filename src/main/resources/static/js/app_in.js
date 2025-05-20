const nombre = sessionStorage.getItem("nombreUsuario");
    if (nombre) {
        document.getElementById("mensaje").textContent = `Bienvenid@, ${nombre}`;
    }
    function cerrarSesion() {
        sessionStorage.clear();
        window.location.href = "/login.html"
    }