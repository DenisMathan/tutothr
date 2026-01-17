/*!
* Start Bootstrap - Bare v5.0.9 (https://startbootstrap.com/template/bare)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-bare/blob/master/LICENSE)
*/

// Scroll Position Handling
let isExplicitNavigation = false;

document.addEventListener("DOMContentLoaded", function () {
    const key = 'scrollpos-' + window.location.href;
    const storedY = sessionStorage.getItem(key);
    
    // Check for navigation type to avoid restoring on fresh tab opens if desired, 
    // though sessionStorage is per-tab anyway.
    
    if (storedY) {
        // Use 'instant' to avoid smooth scrolling
        window.scrollTo({
            top: parseInt(storedY),
            behavior: 'instant' 
        });
    }
    
    // Restore visibility after scroll jump (if hidden by layout.html script)
    document.documentElement.style.visibility = '';
    
    // Listen for clicks on links to detect explicit navigation
    document.body.addEventListener('click', function(e) {
        const link = e.target.closest('a');
        if (link) {
            // If it's a real link navigation (not #, not javascript:)
            const href = link.getAttribute('href');
            if (href && !href.startsWith('#') && !href.startsWith('javascript:')) {
                isExplicitNavigation = true;
            }
        }
    });
});

window.addEventListener("beforeunload", function () {
   //store scrollpos if submit click 
    if (!isExplicitNavigation) {
        const key = 'scrollpos-' + window.location.href;
        sessionStorage.setItem(key, window.scrollY);
    } else {
        const key = 'scrollpos-' + window.location.href;
        sessionStorage.removeItem(key);
    }
});