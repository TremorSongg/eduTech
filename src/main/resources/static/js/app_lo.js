// Define la URL para el login de la API v1.
// Se mantiene 'API_URL' separado de 'BASE_API_URL' según tu estructura actual.
const API_URL = "http://192.168.18.41:8080/api/v1/usuarios/login";

function register() {
    const nombre = document.getElementById("regName").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;

    // Se mantiene la ruta de la API v1 explícitamente para el registro, según tu solicitud.
    fetch("http://192.168.18.41:8080/api/v1/usuarios/registrar", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            nombre: nombre,
            email: email,
            password: password
        })
    })
    .then(res => {
        // Verifica si la respuesta HTTP es exitosa (código 2xx)
        if (!res.ok) {
            // Si la respuesta no es OK, intenta leer el cuerpo como JSON para obtener el mensaje de error del backend.
            // Si falla al leer JSON, devuelve un error genérico con el estado HTTP.
            return res.json().then(errData => {
                throw new Error(errData.message || 'Error en la respuesta del servidor');
            }).catch(() => {
                throw new Error(`Error en la respuesta del servidor: ${res.status} ${res.statusText}`);
            });
        }
        return res.json();
    })
    .then(data => {
        // Loguea la respuesta completa del servidor para depuración.
        console.log("Respuesta del servidor al registrar:", data);

        // Intenta obtener el ID directamente (data.id) o desde una propiedad común si el backend lo nombra diferente (ej: data.userId).
        // La propiedad 'content' es más común en respuestas HATEOAS (v2), no tanto en v1.
        let userId = data.id || data.userId; // Buscar 'id' o 'userId'

        if (userId) {
            alert(`Usuario registrado con ID: ${userId}`);
            
            // Cambiar automáticamente al formulario de login después del registro
            document.getElementById('authToggle').checked = false;
            
            // Limpiar los campos del formulario de registro
            document.getElementById("regName").value = "";
            document.getElementById("regEmail").value = "";
            document.getElementById("regPassword").value = "";
        } else {
            // Si no se encontró el ID, pero la operación fue exitosa, muestra una advertencia
            // y aún así procede con las actualizaciones de la UI. Esto es para cuando el registro
            // es exitoso pero la respuesta del ID no es la esperada.
            console.warn("Registro exitoso, pero no se pudo obtener el ID del usuario de la respuesta. Revisar la estructura de la respuesta del backend.");
            alert("Usuario registrado exitosamente. Revisa la consola para más detalles si el ID no se mostró.");
            
            // Aún así, cambia automáticamente al formulario de login y limpia los campos,
            // ya que el registro en el backend fue exitoso.
            document.getElementById('authToggle').checked = false;
            document.getElementById("regName").value = "";
            document.getElementById("regEmail").value = "";
            document.getElementById("regPassword").value = "";
        }
    })
    .catch(error => {
        console.error('Error durante el registro:', error);
        alert("Error al registrar el usuario: " + error.message);
    });
}

function login(){
    fetch(API_URL,{ // Se utiliza la URL de login definida al inicio (v1).
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        })
    })
    .then(response => {
        // Manejo de errores similar al de register, para capturar más detalles del backend.
        if (!response.ok) {
            return response.json().then(errData => {
                throw new Error(errData.message || 'Error en la respuesta del servidor');
            }).catch(() => {
                throw new Error(`Error en la respuesta del servidor: ${response.status} ${response.statusText}`);
            });
        }
        return response.json();
    })
    .then(data => {
        // Asumiendo que 'data' es el Map<String,String> directamente del backend v1.
        if (data.result === "OK") {
            sessionStorage.setItem("nombreUsuario", data.nombre);
            sessionStorage.setItem("emailUsuario", data.email);
            sessionStorage.setItem("userId", data.id);
            window.location.href = "/index.html";
        } else {
            // El mensaje de error del backend estará en data.message si lo enviaste así.
            alert("Acceso denegado: " + (data.message || "Credenciales inválidas."));
        }
    })
    .catch(error => {
        console.error('Error durante el login:', error);
        alert("Error al intentar iniciar sesión: " + error.message);
    });
}
