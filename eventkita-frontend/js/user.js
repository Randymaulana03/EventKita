// event-kita-frontend/js/user.js

function initUser() {
    // Check authentication
    if (!checkAuth()) return;
    
    // Display user info
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        document.getElementById('userRole').textContent = user.role;
        // Tambahkan info user lainnya jika perlu
    }
    
    // Load events
    loadEvents();
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
    window.location.href = 'login.html';
}

async function loadEvents() {
    try {
        const events = await apiGet('/events');
        displayEvents(events);
    } catch (error) {
        console.error('Error loading events:', error);
        alert('Gagal memuat events');
    }
}

function displayEvents(events) {
    const grid = document.getElementById('eventsGrid');
    
    if (!events || events.length === 0) {
        grid.innerHTML = '<p>Tidak ada events ditemukan</p>';
        return;
    }
    
    grid.innerHTML = events.map(event => `
        <div class="event-card">
            <h3>${event.name}</h3>
            <p>${event.description || 'No description'}</p>
            <p><strong>Date:</strong> ${event.date}</p>
            <p><strong>Location:</strong> ${event.location}</p>
            <button onclick="viewEvent(${event.id})">View Details</button>
        </div>
    `).join('');
}

function viewEvent(eventId) {
    window.location.href = `event-detail.html?id=${eventId}`;
}