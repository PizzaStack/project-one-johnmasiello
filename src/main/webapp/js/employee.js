// https://www.the-art-of-web.com/javascript/doublesubmit/ section 4

(function() {
    function employeeClicksLogin(e) {
        console.log("ditty ditty");
        if (true)
        e.preventDefault();
        window.location.replace('employee_dashboard.html');
    };

    let elem = document.getElementById('employee-submit-login');
    if (elem)
        elem.addEventListener('submit', employeeClicksLogin);
})();

(function() {
    function logoutEmployee(e) {
        console.log("da da da");
        let result = window.confirm('Sure you want to log out?');
        if (result)
            window.location.replace('../index.html');
    }

    let elem = document.getElementById('employeeLogout');
    if (elem)
        elem.addEventListener('click', logoutEmployee, true);
})();
