// Función principal para cargar el carrito
async function loadCarrito() {
  try {
    const container = document.getElementById("carrito-container");
    
    // Mostrar spinner de carga
    container.innerHTML = `
      <div class="text-center my-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>
        <p>Cargando tu carrito...</p>
      </div>
    `;

    const response = await fetch("/api/v1/carrito");
    if (!response.ok) throw new Error("Error al cargar el carrito");
    
    const data = await response.json();
    const items = data.items || [];
    const total = data.total || 0;

    // Si el carrito está vacío
    if (items.length === 0) {
      container.innerHTML = `
        <div class="alert alert-info">
          <h4 class="alert-heading">Tu carrito está vacío</h4>
          <p>Agrega cursos desde nuestra tienda para comenzar.</p>
          <a href="cursos.html" class="btn btn-primary">Ver Cursos</a>
        </div>
      `;
      return;
    }

    // Generar HTML para cada item del carrito
    let itemsHTML = items.map(item => `
      <div class="carrito-item">
        <div class="d-flex justify-content-between">
          <div>
            <h4>${item.nombre}</h4>
            <p class="mb-1">Cantidad: ${item.cantidad}</p>
            <p class="mb-1">Precio unitario: $${item.precioUnitario?.toFixed(2) || '0.00'}</p>
            <p class="fw-bold">Subtotal: $${item.subtotal?.toFixed(2) || '0.00'}</p>
          </div>
          <img src="${item.imagen || 'img/curso-default.png'}" alt="${item.nombre}" 
               style="width: 100px; height: auto; object-fit: cover; border-radius: 5px;">
        </div>
        <div class="item-actions">
          <button onclick="updateQuantity(${item.cursoId}, ${item.cantidad - 1})" 
                  class="btn btn-outline-secondary" ${item.cantidad <= 1 ? 'disabled' : ''}>
            ➖
          </button>
          <button onclick="updateQuantity(${item.cursoId}, ${item.cantidad + 1})" 
                  class="btn btn-outline-secondary">
            ➕
          </button>
          <button onclick="removeFromCart(${item.cursoId})" 
                  class="btn btn-outline-danger ms-auto">
            Eliminar
          </button>
        </div>
      </div>
    `).join('');

    // Agregar total y botones de acción
    container.innerHTML = itemsHTML + `
      <div class="total-section">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h3 class="mb-0">Total:</h3>
          <h3 class="mb-0">$${total?.toFixed(2) || '0.00'}</h3>
        </div>
        <div class="d-flex justify-content-between">
          <button onclick="clearCart()" class="btn btn-outline-danger">
            Vaciar Carrito
          </button>
          <button onclick="checkout()" class="btn btn-success btn-lg">
            Finalizar Compra
          </button>
        </div>
      </div>
    `;

  } catch (error) {
    console.error("Error:", error);
    document.getElementById("carrito-container").innerHTML = `
      <div class="alert alert-danger">
        <h4 class="alert-heading">Error al cargar el carrito</h4>
        <p>${error.message}</p>
        <button onclick="loadCarrito()" class="btn btn-warning mt-2">
          Reintentar
        </button>
      </div>
    `;
  }
}

// Actualizar cantidad de un item
async function updateQuantity(cursoId, newQuantity) {
  if (newQuantity < 1) return;
  
  try {
    const response = await fetch(`/api/v1/carrito/actualizar/${cursoId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cantidad: newQuantity })
    });
    
    if (!response.ok) throw new Error(await response.text());
    loadCarrito();
  } catch (error) {
    Swal.fire("Error", error.message, "error");
  }
}

// Eliminar item del carrito
async function removeFromCart(cursoId) {
  const { isConfirmed } = await Swal.fire({
    title: "¿Eliminar este curso?",
    text: "Esta acción no se puede deshacer",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Sí, eliminar",
    cancelButtonText: "Cancelar"
  });
  
  if (!isConfirmed) return;
  
  try {
    const response = await fetch(`/api/v1/carrito/eliminar/${cursoId}`, {
      method: "DELETE"
    });
    
    if (!response.ok) throw new Error(await response.text());
    loadCarrito();
    Swal.fire("Eliminado", "El curso fue removido del carrito", "success");
  } catch (error) {
    Swal.fire("Error", error.message, "error");
  }
}

// Vaciar todo el carrito
async function clearCart() {
  const { isConfirmed } = await Swal.fire({
    title: "¿Vaciar el carrito?",
    text: "Se eliminarán todos los cursos",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Sí, vaciar",
    cancelButtonText: "Cancelar"
  });
  
  if (!isConfirmed) return;
  
  try {
    const response = await fetch("/api/v1/carrito/vaciar", {
      method: "DELETE"
    });
    
    if (!response.ok) throw new Error(await response.text());
    loadCarrito();
    Swal.fire("Carrito vaciado", "Todos los cursos fueron removidos", "success");
  } catch (error) {
    Swal.fire("Error", error.message, "error");
  }
}

// Finalizar compra (checkout)
async function checkout() {
  const { isConfirmed } = await Swal.fire({
    title: "¿Finalizar compra?",
    html: `
      <p>Estás a punto de confirmar tu compra.</p>
      <p>Se descontarán los cursos de nuestro inventario.</p>
    `,
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Confirmar compra",
    cancelButtonText: "Seguir comprando"
  });
  
  if (!isConfirmed) return;
  
  try {
    const response = await fetch("/api/v1/carrito/comprar", {
      method: "POST",
      headers: { "Content-Type": "application/json" }
    });
    
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error);
    }
    
    const data = await response.json();
    
    // Mostrar mensaje de éxito
    await Swal.fire({
      title: "¡Compra exitosa!",
      html: `
        <div class="text-center">
          <i class="fas fa-check-circle fa-5x text-success mb-4"></i>
          <h3>Gracias por tu compra</h3>
          <p class="mt-3">Los cursos han sido reservados para ti.</p>
          <p>Recibirás un correo con los detalles de tu compra.</p>
        </div>
      `,
      confirmButtonText: "Aceptar",
      customClass: {
        popup: 'border-success'
      }
    });
    
    // Recargar el carrito vacío
    loadCarrito();
    
  } catch (error) {
    Swal.fire({
      title: "Error en la compra",
      text: error.message,
      icon: "error",
      confirmButtonText: "Entendido"
    });
  }
}

// Cargar el carrito cuando la página se abre
document.addEventListener("DOMContentLoaded", loadCarrito);