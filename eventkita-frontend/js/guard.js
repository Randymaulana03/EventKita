// event-kita-frontend/js/guard.js

// Check if user is authenticated
function checkAuth() {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    
    if (!token || !user) {
        window.location.href = 'login.html';
        return false;
    }
    
    return true;
}

// Redirect based on role
function redirectByRole() {
    const role = localStorage.getItem('role');
    const currentPage = window.location.pathname;
    
    // Whitelist pages that should NOT trigger redirect
    const whitelistedPages = ['dashboard', 'payment', 'ticket', 'event-detail'];
    const isWhitelisted = whitelistedPages.some(page => currentPage.includes(page));
    
    if (isWhitelisted) {
        // Jika di whitelisted page, biarkan
        return;
    }
    
    // Redirect ke dashboard berdasarkan role
    if (role === 'ORGANIZER' && !currentPage.includes('organizer')) {
        window.location.href = 'organizer-dashboard.html';
    } else if (role === 'ADMIN' && !currentPage.includes('admin')) {
        window.location.href = 'admin-dashboard.html';
    } else if (role === 'PESERTA' && !currentPage.includes('user')) {
        window.location.href = 'user-dashboard.html';
    }
}

// Auto-check auth on page load
document.addEventListener('DOMContentLoaded', function() {
    // Skip auto-redirect for payment, ticket, event-detail pages
    const currentUrl = window.location.href;
    const skipRedirect = ['payment.html', 'ticket.html', 'event-detail.html'].some(page => currentUrl.includes(page));
    
    console.log('[guard.js] DOMContentLoaded - currentUrl:', currentUrl, 'skipRedirect:', skipRedirect);
    
    if (checkAuth()) {
        if (!skipRedirect) {
            console.log('[guard.js] calling redirectByRole()');
            redirectByRole();
        } else {
            console.log('[guard.js] skipping redirectByRole() for payment/ticket/event-detail');
        }
    }
});