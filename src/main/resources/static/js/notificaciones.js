document.addEventListener('DOMContentLoaded', async () => {
    const usuarioId = 1; 
    
    try {
        const response = await fetch(`/api/v1/reportes/usuario/${usuarioId}`);
        if (!response.ok) throw new Error("Error al cargar reportes");
        
        const reportes = await response.json();
        renderizarNotificaciones(reportes);
    } catch (error) {
        console.error("Error:", error);
        document.getElementById('tabla-notificaciones').innerHTML = `
            <tr><td colspan="4" class="text-center text-danger">Error al cargar notificaciones.</td></tr>
        `;
    }
});

function renderizarNotificaciones(reportes = []) {
    const tbody = document.getElementById('tabla-notificaciones');
    const filaVacia = document.getElementById('sin-notificaciones');
    
    // Limpiar tabla (excepto fila vacía)
    tbody.querySelectorAll('tr:not(#sin-notificaciones)').forEach(row => row.remove());
    
    // Agregar reportes
    reportes.forEach(reporte => {
        if (filaVacia) filaVacia.style.display = 'none';
        
        tbody.innerHTML += `
            <tr>
                <td><span class="badge bg-primary">Reporte #${reporte.id}</span></td>
                <td>${reporte.mensaje || "Sin descripción"}</td>
                <td>${reporte.fechaCreacion || "Fecha no disponible"}</td>
                <td><span class="badge ${reporte.estado === 'PENDIENTE' ? 'bg-warning' : 'bg-success'}">
                    ${reporte.estado}
                </span></td>
            </tr>
        `;
    });

    // Mostrar mensaje si no hay datos
    if (reportes.length === 0 && filaVacia) {
        filaVacia.style.display = '';
    }
}