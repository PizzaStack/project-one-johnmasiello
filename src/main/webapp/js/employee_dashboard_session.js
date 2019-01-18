(function () {
    function authenticateEmployee(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        if (this.responseText != 'success')
                        window.location.replace('../index.html');
                        break;

                    case 400:
                    console.log("FAIL");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('GET', '../validate-employee', true);
        xhttp.send();
    }
    authenticateEmployee();
})();

(function() {
    function logoutEmployee(e) {
        let result = window.confirm('Sure you want to log out?');
        if (result)
            logoutSession();
    }

    function logoutSession() {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log(this.status);
                switch (this.status) {
                    case 200:
                        window.location.replace('../index.html');
                        break;
    
                    case 400:
                    console.log("FAIL");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('GET', '../login-employee', true);
        xhttp.send();
    }

    let elem = document.getElementById('employeeLogout');
    if (elem)
        elem.addEventListener('click', logoutEmployee, true);
})();