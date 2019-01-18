(function() {
    // https://stackoverflow.com/questions/11563638/how-do-i-get-the-value-of-text-input-field-using-javascript
    var username = document.getElementById('managerInputUsername');
    var password = document.getElementById('managerInputPassword');

    // https://stackoverflow.com/questions/2906582/how-to-create-an-html-button-that-acts-like-a-link
    function managerClicksLogin() {
        // console.log(username.value);
        // makeAJAXCall();
        if (username.value && password.value)
            loginManager(username.value, password.value);
        else
            window.alert('Username or password is left blank');

            // to receive a response back as JSON, refer to the following link...
            // https://openclassrooms.com/en/courses/3523261-use-javascript-in-your-web-projects/3759266-extend-your-ajax-request
    };

    function loginManager(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log(this.status);
                switch (this.status) {
                    case 200:
                        if (this.responseText == 'success')
                            window.location.href = 'manager_dashboard.html';
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
        console.log(username);
        console.log(password);
        xhttp.open('GET', `../login-manager?username=${username}&password=${password}`, true);
        xhttp.send();
    }

    let elem = document.getElementById('manager-submit-button');
    if (elem)
        elem.addEventListener('click', managerClicksLogin);
})();