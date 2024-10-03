const apiUrl = 'http://localhost:8080/api/properties';

document.getElementById('propertyForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const property = {
        address: document.getElementById('address').value,
        price: parseFloat(document.getElementById('price').value),
        size: parseFloat(document.getElementById('size').value),
        description: document.getElementById('description').value
    };

    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(property)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Propiedad creada:', data);
        loadProperties(); 
        document.getElementById('propertyForm').reset(); 
    })
    .catch(error => console.error('Error:', error));
});

function loadProperties() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const propertyList = document.getElementById('propertyList');
            propertyList.innerHTML = ''; 
            data.forEach(property => {
                const li = document.createElement('li');
                li.textContent = `${property.address} - $${property.price} - ${property.size}m² - ${property.description}`;
                
                // Botón de actualizar
                const updateButton = document.createElement('button');
                updateButton.textContent = 'Actualizar';
                updateButton.onclick = () => updateProperty(property.id);
                
                // Botón de eliminar
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Eliminar';
                deleteButton.onclick = () => deleteProperty(property.id);
                
                li.appendChild(updateButton);
                li.appendChild(deleteButton);
                propertyList.appendChild(li);
            });
        })
        .catch(error => console.error('Error:', error));
}

function deleteProperty(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE'
    })
    .then(() => loadProperties())
    .catch(error => console.error('Error:', error));
}

function updateProperty(id) {
    const property = {
        address: prompt("Nueva dirección:"),
        price: parseFloat(prompt("Nuevo precio:")),
        size: parseFloat(prompt("Nuevo tamaño (m²):")),
        description: prompt("Nueva descripción:")
    };
    
    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(property)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Propiedad actualizada:', data);
        loadProperties(); 
    })
    .catch(error => console.error('Error:', error));
}

// Cargar propiedades al iniciar
loadProperties();
