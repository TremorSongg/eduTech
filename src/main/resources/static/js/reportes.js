document.getElementById('form-reporte').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const datos = {
        // no se le da un número directamente porque puede confundir,
        //  mejor asegurarse de que es un número con esta línea
        usuarioId: parseInt(document.getElementById('usuarioId').value), 
        mensaje: document.getElementById('mensaje').value
    };

    try {
        const response = await fetch('/api/v1/reportes/crear', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        if (response.ok) {
            alert("Reporte enviado correctamente.");
            // Esto redirige a notificaciones
            window.location.href = "notificaciones.html"; 
        } else {
            const error = await response.text();
            alert("Error: " + error);
        }
    } catch (error) {
        console.error("Error:", error);
    }
});