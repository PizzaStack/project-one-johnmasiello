(function() {
    // https://stackoverflow.com/questions/11563638/how-do-i-get-the-value-of-text-input-field-using-javascript
    var username = document.getElementById('employeeInputUsername');
    var password = document.getElementById('employeeInputPassword');

    // https://stackoverflow.com/questions/2906582/how-to-create-an-html-button-that-acts-like-a-link
    function employeeClicksLogin() {
        // console.log(username.value);
        // makeAJAXCall();
        if (username.value && password.value)
            authenticateEmployee(username.value, password.value);

            // to receive a response back as JSON, refer to the following link...
            // https://openclassrooms.com/en/courses/3523261-use-javascript-in-your-web-projects/3759266-extend-your-ajax-request
    };

    function authenticateEmployee(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log(this.status);
                switch (this.status) {
                    case 200:
                        console.log("SUCCESS_SERVLET");
                        if (this.responseText == 'success')
                            window.location.href='./employee_dashboard.html'
                        else
                            window.alert("Wrong username or password");
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
        
        xhttp.open('GET', `../login-employee?username=${username}&password=${password}`, true);
        xhttp.send();
    }

    let elem = document.getElementById('employee-submit-login');
    if (elem)
        elem.addEventListener('click', employeeClicksLogin);
})();