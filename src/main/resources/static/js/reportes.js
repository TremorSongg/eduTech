document.getElementById('form-reporte').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const datos = {
        usuarioId: 1, 
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
            document.getElementById('mensaje').value = ""; // Limpiar campo
        } else {
            const error = await response.text();
            alert("Error: " + error);
        }
    } catch (error) {
        console.error("Error:", error);
    }
});