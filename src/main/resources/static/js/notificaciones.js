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
    tbody.innerHTML = ''; // Limpiar el contenido del tbody

    // Agregar reportes
    reportes.forEach(reporte => {
        const tr = document.createElement('tr');

        const tipoBadge = document.createElement('span');
        tipoBadge.className = 'badge bg-primary';
        tipoBadge.textContent = `Reporte #${reporte.id}`;

        const estadoBadge = document.createElement('span');
        estadoBadge.className = reporte.estado === 'PENDIENTE' ? 'badge bg-warning' : 'badge bg-success';
        estadoBadge.textContent = reporte.estado;

        tr.innerHTML = `
            <td>${tipoBadge.outerHTML}</td>
            <td>${reporte.mensaje || "Sin descripción"}</td>
            <td>${reporte.fechaCreacion || "Fecha no disponible"}</td>
            <td>${estadoBadge.outerHTML}</td>
        `;

        tbody.appendChild(tr);
    });

    // Mostrar mensaje si no hay datos
    if (reportes.length === 0 && filaVacia) {
        filaVacia.style.display = '';
    } else if (filaVacia) {
        filaVacia.style.display = 'none'; // Ocultar fila vacía si hay reportes
    }
}
